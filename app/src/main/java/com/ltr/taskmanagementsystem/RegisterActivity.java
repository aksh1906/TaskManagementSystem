package com.ltr.taskmanagementsystem;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity
        implements View.OnClickListener {

    private TextView tvHaveAccount, tvPasswordsMatch;
    private EditText etUsername, etPassword, etConfirmPassword;
    private String username, password, confirmPassword;
    private Button btnRegister;
    private DatabaseHelper databaseHelper;
    private User user;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        tvHaveAccount = findViewById(R.id.tvAlreadyHaveAccount);
        btnRegister = findViewById(R.id.btnRegister);
        tvPasswordsMatch = findViewById(R.id.tvPasswordsMatch);
        tvPasswordsMatch.setVisibility(View.INVISIBLE);

        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Registering");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        btnRegister.setOnClickListener(this);
        tvHaveAccount.setOnClickListener(this);

        etConfirmPassword.setEnabled(false); // Disable the confirm password field until the password field is filled

        etUsername.addTextChangedListener(usernameWatcher);
        etPassword.addTextChangedListener(passwordWatcher);
        etConfirmPassword.addTextChangedListener(confirmPasswordWatcher);

        databaseHelper = new DatabaseHelper(this);
        user = new User();

        checkAllFieldsForEmptyValues();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRegister:
                register();
                break;
            case R.id.tvAlreadyHaveAccount:
                finish();
                break;
        }
    }

    private void register() {
        mProgress.show();
        username = etUsername.getText().toString();
        password = etPassword.getText().toString();
        confirmPassword = etConfirmPassword.getText().toString();

        if(!databaseHelper.checkUser(username.trim())) {
            user.setName(username.trim());
            user.setPassword(password.trim());

            databaseHelper.addUser(user);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    mProgress.dismiss();
                }
            }, 3000); // 3000 milliseconds delay

            this.mProgress.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    Toast toast = Toast.makeText(getApplicationContext(), "User successfully added!", Toast.LENGTH_LONG);
                    toast.show();
                }
            });


        } else {


            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    mProgress.dismiss();
                }
            }, 3000); // 3000 milliseconds delay
            this.mProgress.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    etUsername.setText(null);
                    etPassword.setText(null);
                    etConfirmPassword.setText(null);
                    tvPasswordsMatch.setVisibility(View.INVISIBLE);
                    tvPasswordsMatch.setText("Passwords do not Match.");
                    etConfirmPassword.setEnabled(false);
                    Toast toast = Toast.makeText(getApplicationContext(), "Username already exists! Please choose a different username.", Toast.LENGTH_LONG);
                    toast.show();
                }
            });

        }
    }

    TextWatcher usernameWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

        @Override
        public void afterTextChanged(Editable editable) {
            checkUsernameIsEmpty();
        }
    };

    TextWatcher passwordWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            checkPasswordLength();
        }

        @Override
        public void afterTextChanged(Editable editable) {
            checkPassWordIsEmpty();
        }
    };

    TextWatcher confirmPasswordWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            checkIfPasswordsMatch();
        }

        @Override
        public void afterTextChanged(Editable editable) {
            checkConfirmPasswordIsEmpty();
        }
    };

    private void checkUsernameIsEmpty() {
        username = etUsername.getText().toString();

        if(username.isEmpty()) {
            etUsername.setError("Field cannot be empty");
        }

        checkAllFieldsForEmptyValues();
    }

    private void checkPasswordLength() {
        password = etPassword.getText().toString();
        int length = password.length();
        if(length >= 6) {
            enableConfirmPasswordField();
        }
    }

    private void checkPassWordIsEmpty() {
        password = etPassword.getText().toString();

        if(password.isEmpty()) {
            etPassword.setError("Field cannot be empty");
            enableConfirmPasswordField();
        }

        enableConfirmPasswordField();
        checkAllFieldsForEmptyValues();
    }

    private void checkConfirmPasswordIsEmpty() {
        confirmPassword = etConfirmPassword.getText().toString();

        if(confirmPassword.isEmpty()) {
            etConfirmPassword.setError("Field cannot be empty");
        }

        checkAllFieldsForEmptyValues();
    }

    private void enableConfirmPasswordField() {
        password = etPassword.getText().toString();
        if(password.length() >= 6 && !(password.equals(""))) {
            etConfirmPassword.setEnabled(true);
            tvPasswordsMatch.setVisibility(View.VISIBLE);
        }
    }

    private void checkIfPasswordsMatch() {
        password = etPassword.getText().toString();
        confirmPassword = etConfirmPassword.getText().toString();

        if(confirmPassword.equals(password)) {
            tvPasswordsMatch.setText("Passwords Match.");
        } else {
            tvPasswordsMatch.setText("Passwords do not Match.");
        }
    }

    private void checkAllFieldsForEmptyValues() {
        username = etUsername.getText().toString();
        password = etPassword.getText().toString();
        confirmPassword = etConfirmPassword.getText().toString();

        if(username.equals("") || password.equals("") || confirmPassword.equals("")) {
            btnRegister.setBackgroundResource(R.drawable.button_rounded_corners_disabled);
            btnRegister.setEnabled(false);
        } else {
            btnRegister.setBackgroundResource(R.drawable.button_rounded_corners_enabled);
            btnRegister.setEnabled(true);
        }
    }
}
