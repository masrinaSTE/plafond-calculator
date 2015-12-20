package masrina.project.plafondcalculator;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText inputLoan, inputDuration, inputInterest;
    private TextInputLayout inputLayoutLoan, inputLayoutDuration, inputLayoutInterest;
    private Button btnSignUp;
    private TextView monthlyPayment, totalMonthlyPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        inputLayoutLoan = (TextInputLayout) findViewById(R.id.input_layout_loan);
        inputLayoutDuration = (TextInputLayout) findViewById(R.id.input_layout_duration);
        inputLayoutInterest = (TextInputLayout) findViewById(R.id.input_layout_interest);
        inputLoan = (EditText) findViewById(R.id.input_loan);
        inputDuration = (EditText) findViewById(R.id.input_duration);
        inputInterest = (EditText) findViewById(R.id.input_interest);
        btnSignUp = (Button) findViewById(R.id.btn_calculate);
        monthlyPayment = (TextView) findViewById(R.id.monthly_payment_label);
        totalMonthlyPayment = (TextView) findViewById(R.id.total_monthly_payment);

        inputLoan.addTextChangedListener(new MyTextWatcher(inputLoan));
        inputDuration.addTextChangedListener(new MyTextWatcher(inputDuration));
        inputInterest.addTextChangedListener(new MyTextWatcher(inputInterest));

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
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

        if (!validatePassword()) {
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

    private boolean validatePassword() {
        if (inputInterest.getText().toString().trim().isEmpty()) {
            inputLayoutInterest.setError(getString(R.string.err_msg_interest));
            requestFocus(inputInterest);
            return false;
        } else {
            inputLayoutInterest.setErrorEnabled(false);
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_loan:
                    validateName();
                    break;
                case R.id.input_duration:
                    validateEmail();
                    break;
                case R.id.input_interest:
                    validatePassword();
                    break;
            }
        }
    }
}
