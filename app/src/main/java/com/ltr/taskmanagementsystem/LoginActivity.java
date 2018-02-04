package com.ltr.taskmanagementsystem;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class LoginActivity extends AppCompatActivity {

    private EditText username, password;
    private TextView incorrectDetails, forgotPassword;
    private Button submitButton;

    private TextWatcher usernameWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            checkUsernameIsEmpty();
        }
    };

    private final TextWatcher passwordWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

            checkPasswordIsEmpty();
        }
    };

    void checkUsernameIsEmpty() {

        String uname = username.getText().toString();

        if(uname.isEmpty()) {
            username.setError("Username cannot be empty");
        }
        checkFieldsForEmptyValues();
    }

    void checkPasswordIsEmpty() {
        String passwd = password.getText().toString();
        if(passwd.isEmpty()) {
            password.setError("Password cannot be empty");
        }

        checkFieldsForEmptyValues();
    }
//
    void checkFieldsForEmptyValues() {
        submitButton = findViewById(R.id.btnSubmit);

        String uname = username.getText().toString();
        String passwd = password.getText().toString();

        if (uname.equals("") || passwd.equals("")) {
            submitButton.setEnabled(false);
        } else {
            submitButton.setBackgroundResource(R.drawable.button_rounded_corners_enabled);
            submitButton.setEnabled(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.etUsername);
        password = findViewById(R.id.etPassword);
        incorrectDetails = findViewById(R.id.tvIncorrectUsernamePassword);
        forgotPassword = findViewById(R.id.tvForgotUsernamePassword);
        submitButton = findViewById(R.id.btnSubmit);

        username.addTextChangedListener(usernameWatcher);
        password.addTextChangedListener(passwordWatcher);

        checkFieldsForEmptyValues();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // login
            }
        });
    }
}



