package com.example.womensempowerment;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
// UI components
    private EditText etUsername;
    private EditText etEmail;
    private EditText etMobile;
    private EditText etPass;
    private EditText etRepeatPass;
    private EditText etConfCode;
    private Button btnSignUp;
    private Button btnVerify;

    // Cognito
    Cognito authentication;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        authentication = new Cognito(getApplicationContext());
        initViewComponents();
    }

    private void initViewComponents(){
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etMobile = findViewById(R.id.etMobile);
        etPass = findViewById(R.id.etPass);
        etRepeatPass = findViewById(R.id.etRepeatPass);
        etConfCode = findViewById(R.id.etConfCode);

        btnSignUp = findViewById(R.id.btnSignUp);
        btnVerify = findViewById(R.id.btnVerify);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSignUp:
                if (etPass.getText().toString().endsWith(etRepeatPass.getText().toString())) {
                    userId = etUsername.getText().toString().replace(" ","" );
                    authentication.addAttribute("name", userId);
                    authentication.addAttribute("phone_number", etMobile.getText().toString().replace(" ", ""));
                    authentication.addAttribute("email", etEmail.getText().toString().replace(" ", ""));
                    authentication.signInUser(userId, etPass.getText().toString());
                } else {

                }
                break;
            case R.id.btnVerify:
                authentication.confirmUser(userId, etConfCode.getText().toString().replace(" ", ""));
                break;
        }


    }
}
