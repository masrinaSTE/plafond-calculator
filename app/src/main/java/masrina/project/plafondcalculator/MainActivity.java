package masrina.project.plafondcalculator;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private Toolbar toolbar;
    private EditText inputLoan, inputDuration   ;
    private TextInputLayout inputLayoutLoan, inputLayoutDuration;
    private Button btnCalculate;
    private TextView monthlyPayment, totalMonthlyPayment;
    private String selected_package;
    private double interest_amount;
    private int monthly_duration;
    private Spinner spinner;
    private FloatingActionButton buttonReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        inputLayoutLoan = (TextInputLayout) findViewById(R.id.input_layout_loan);
        inputLayoutDuration = (TextInputLayout) findViewById(R.id.input_layout_duration);

        inputLoan = (EditText) findViewById(R.id.input_loan);
        inputDuration = (EditText) findViewById(R.id.input_duration);

        btnCalculate = (Button) findViewById(R.id.btn_calculate);
        buttonReset = (FloatingActionButton) findViewById(R.id.reset_button);
        monthlyPayment = (TextView) findViewById(R.id.monthly_payment_label);
        totalMonthlyPayment = (TextView) findViewById(R.id.total_monthly_payment);

//        inputLoan.addTextChangedListener(new MyTextWatcher(inputLoan));
//        inputDuration.addTextChangedListener(new MyTextWatcher(inputDuration));

        spinner = (Spinner) findViewById(R.id.input_package);
        spinner.setOnItemSelectedListener(this);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.package_type, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculateMonthlyPayment();
            }
        });

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputLoan.setText("");
                inputDuration.setText("");
                spinner.setSelection(0);
                totalMonthlyPayment.setText("Rp 0");
            }
        });


    }

    /**
     * Validating form
     */
    private void submitForm() {
        if (!validateName()) {
            return;
        }

        if (!validateEmail()) {
            return;
        }

        Toast.makeText(getApplicationContext(), "Thank You!", Toast.LENGTH_SHORT).show();
    }



    private boolean validateName() {
        if (inputLoan.getText().toString().trim().isEmpty()) {
            inputLayoutLoan.setError(getString(R.string.err_msg_loan));
            requestFocus(inputLoan);
            return false;
        } else {
            inputLayoutLoan.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateEmail() {
        String email = inputDuration.getText().toString().trim();

        if (email.isEmpty()) {
            inputLayoutDuration.setError(getString(R.string.err_msg_duration));
            requestFocus(inputDuration);
            return false;
        } else {
            inputLayoutDuration.setErrorEnabled(false);
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * Calculate Total Monthly Payment
     */
    private void calculateMonthlyPayment() {
        Log.d("PACKAGE TYPE", selected_package);
        if (inputLoan.getText().toString().isEmpty() || inputDuration.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Jumlah pinjaman atau durasi harus diisi", Toast.LENGTH_SHORT).show();

        } else {
            switch (selected_package) {
                case "Paket kp3m":
                    calculatePackagekp3m();
                    break;
                case "Paket SUP":
                    calculatePackageSUP();
                    break;
                case "Paket Reguler":
                    calculatePackageReguler();
                    break;
            }
        }
    }

    private void calculatePackagekp3m () {
        int loan_amount = Integer.parseInt(inputLoan.getText().toString());
        if (loan_amount < 50000000) {
            interest_amount = (1.8/100);
        } else if (loan_amount >= 50000000 && loan_amount <= 100000000) {
            interest_amount = (1.5/100);
        } else {
            interest_amount = (1.4/100);
        }
        monthly_duration = Integer.parseInt(inputDuration.getText().toString());
        double payment = totalMonthlyPaymentFormula(loan_amount, monthly_duration, interest_amount);
        totalMonthlyPayment.setText("Rp " + String.valueOf(payment));
    }

    private void calculatePackageSUP() {
        int loan_amount = Integer.parseInt(inputLoan.getText().toString());
        interest_amount = (1/100);
        monthly_duration = Integer.parseInt(inputDuration.getText().toString());
        double payment = totalMonthlyPaymentFormula(loan_amount, monthly_duration, interest_amount);
        totalMonthlyPayment.setText("Rp " + String.valueOf(payment));
    }

    private void calculatePackageReguler() {
        int loan_amount = Integer.parseInt(inputLoan.getText().toString());
        if (loan_amount < 50000000) {
            interest_amount = (2/100);
        } else if (loan_amount >= 50000000 && loan_amount <= 100000000) {
            interest_amount = (1.8/100);
        } else {
            interest_amount = (1.7/100);
        }
        monthly_duration = Integer.parseInt(inputDuration.getText().toString());
        double payment = totalMonthlyPaymentFormula(loan_amount, monthly_duration, interest_amount);

        totalMonthlyPayment.setText("Rp " + String.valueOf(payment));
    }
    private double totalMonthlyPaymentFormula(int loan, int duration, double interest) {
        double total_payment = (((loan * interest * duration) + loan) / duration);
        return total_payment;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String item = adapterView.getItemAtPosition(i).toString();

        // Showing selected spinner item
//        Toast.makeText(adapterView.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

        selected_package = spinner.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        selected_package = "Paket kp3m";
    }

//    private class MyTextWatcher implements TextWatcher {
//
//        private View view;
//
//        private MyTextWatcher(View view) {
//            this.view = view;
//        }
//
//        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//        }
//
//        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//        }
//
//        public void afterTextChanged(Editable editable) {
//            switch (view.getId()) {
//                case R.id.input_loan:
//                    validateName();
//                    break;
//                case R.id.input_duration:
//                    validateEmail();
//                    break;
//            }
//        }
//    }
}
