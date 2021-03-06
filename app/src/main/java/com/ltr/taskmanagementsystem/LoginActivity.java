package com.ltr.taskmanagementsystem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;


public class LoginActivity extends AppCompatActivity
        implements OnClickListener {

    private EditText username, password;
    private TextView incorrectDetails, forgotPassword;
    private Button submitButton;
    private CheckBox rememberMe;
    private String uname, passwd;
    private boolean saveLogin;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPreferencesEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        submitButton = findViewById(R.id.btnSubmit);
        submitButton.setOnClickListener(this);

        username = findViewById(R.id.etUsername);
        password = findViewById(R.id.etPassword);
        incorrectDetails = findViewById(R.id.tvIncorrectUsernamePassword);
        forgotPassword = findViewById(R.id.tvForgotUsernamePassword);
        rememberMe = findViewById(R.id.chkRememberMe);
        loginPreferences = getSharedPreferences("loginPreferences", MODE_PRIVATE);
        loginPreferencesEditor = loginPreferences.edit();

        username.addTextChangedListener(usernameWatcher); //TextWatcher for the username EditText
        password.addTextChangedListener(passwordWatcher); //TextWatcher for the password EditText

        checkFieldsForEmptyValues();  // disables the submitButton when the activity is created, as fields are empty by default

        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if(saveLogin) {
            username.setText(loginPreferences.getString("username", ""));
            password.setText(loginPreferences.getString("password", ""));
            rememberMe.setChecked(true);
        }
    }

    private TextWatcher usernameWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) { }

        @Override
        public void afterTextChanged(Editable editable) {
            checkUsernameIsEmpty();  // checks if the username EditText is empty
        }
    };

    private final TextWatcher passwordWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) { }

        @Override
        public void afterTextChanged(Editable editable) {

            checkPasswordIsEmpty();  // checks if the password EditText is empty
        }
    };


    void checkUsernameIsEmpty() {

        // Used to check if the username EditText is empty
        uname = username.getText().toString();

        if(uname.isEmpty()) {
            username.setError("Username cannot be empty");
        }
        checkFieldsForEmptyValues(); // check both username and password EditTexts
    }

    void checkPasswordIsEmpty() {

        // used to check if the password EditText is empty
        passwd = password.getText().toString();
        if(passwd.isEmpty()) {
            password.setError("Password cannot be empty");
        }

        checkFieldsForEmptyValues(); // check both username and password EditTexts
    }
//
    void checkFieldsForEmptyValues() {

        // this method checks both the username and password EditTexts,
        // and if neither are empty, it enables the submitButton

        submitButton = findViewById(R.id.btnSubmit);

        uname = username.getText().toString();
        passwd = password.getText().toString();

        if (uname.equals("") || passwd.equals("")) {
            submitButton.setBackgroundResource(R.drawable.button_rounded_corners_disabled);
            submitButton.setEnabled(false);
        } else {
            submitButton.setBackgroundResource(R.drawable.button_rounded_corners_enabled); // change the color of the button to show the user that it's enabled
            submitButton.setEnabled(true);
        }
    }

    public void onClick(View view) {
        if(view == submitButton) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(username.getWindowToken(), 0);

            uname = username.getText().toString();
            passwd = password.getText().toString();

            if(rememberMe.isChecked()) {
                loginPreferencesEditor.putBoolean("saveLogin", true);
                loginPreferencesEditor.putString("username", uname);
                loginPreferencesEditor.putString("password", passwd);
                loginPreferencesEditor.commit();
            } else {
                loginPreferencesEditor.clear();
                loginPreferencesEditor.commit();
            }

            login();
        }
    }

    public void login() {

        // used to check the user's login details

        uname = username.getText().toString();
        passwd = password.getText().toString();

        if(uname.equals("admin") && passwd.equals("password")) {  // placeholder, not final
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            LoginActivity.this.finish();
        } else {
            incorrectDetails.setVisibility(View.VISIBLE);
            incorrectDetails.setText(R.string.text_incorrect_username_password);
            incorrectDetails.setError("");
            forgotPassword.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        // used to clear focus from an EditText when the user clicks outside of it

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }
}



