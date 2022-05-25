package com.example.womensempowerment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity  {
    // UI components
    private Button btnLogin, buttonGetHelpNow;
    private TextView textView;
    private ImageView imageView;
    private Button txtNotAccount;
    private EditText etPassword, etUsername;

    //Login components
    public static Cognito userpool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Login");
        initViewComponents();

        //UI initialization
        buttonGetHelpNow = (Button) findViewById(R.id.buttonGetHelpNow);
        textView = (TextView) findViewById(R.id.textView);
        imageView = (ImageView) findViewById(R.id.imageView);
        buttonGetHelpNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Search.class);
                startActivity(i);
            }
        });

    }

    private void initViewComponents(){
        // More UI initialization
        txtNotAccount = (Button) findViewById(R.id.txtNotAccount);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
// getting the buttons to function
        txtNotAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cognito authentication = new Cognito(getApplicationContext());
                authentication.signInUser(etUsername.getText().toString().replace(" ", ""), etPassword.getText().toString());
            }
        });

    }

    /** This code for the onClick doesn't seem to function.
     * This is what's been done in class, so I don't understand why it's not working.
     * I've commented it out, but kept it in place
     */
    /*
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //case R.id.buttonGetHelpNow:
                //Intent intent = new Intent(MainActivity.this, Search.class);
                //startActivity(intent);
                //break;
            //case R.id.txtNotAccount:
                //Intent intent2 = new Intent(MainActivity.this, RegisterActivity.class);
            //startActivity(intent2);
                //break;
            case R.id.btnLogin:
                Cognito authentication = new Cognito(getApplicationContext());
                authentication.signInUser(etUsername.getText().toString().replace(" ", ""), etPassword.getText().toString());
                break;
        }
    }

     */

}