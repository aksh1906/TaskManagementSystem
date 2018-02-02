package com.ltr.taskmanagementsystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText password = (EditText) findViewById(R.id.etPassword);
        password.setTransformationMethod(new PasswordTransformationMethod());
    }

    public void login(View view) {
        EditText username = (EditText) findViewById(R.id.etUsername);
        EditText password = (EditText) findViewById(R.id.etPassword);
        TextView incorrectDetails = (TextView) findViewById(R.id.tvIncorrectUsernamePassword);
        TextView forgotPassword = (TextView) findViewById(R.id.tvForgotUsernamePassword);

        if(username.getText().toString().equals("admin") & password.getText().toString().equals("password")) {
            // goto other activity
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            incorrectDetails.setVisibility(View.VISIBLE);
            forgotPassword.setVisibility(View.VISIBLE);
        }
    }
}
