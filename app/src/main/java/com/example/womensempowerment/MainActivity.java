package com.example.womensempowerment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
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

    }

    private void initViewComponents(){
        txtNotAccount = (Button) findViewById(R.id.txtNotAccount);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonGetHelpNow:
                startActivity(new Intent(MainActivity.this, Search.class));
                // TODO Add code to take the user to the Search

                break;
            case R.id.txtNotAccount:
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
                // TODO add code to go to RegisterActivity
                break;
            case R.id.btnLogin:
                Cognito authentication = new Cognito(getApplicationContext());
                authentication.signInUser(etUsername.getText().toString().replace(" ", ""), etPassword.getText().toString());
                break;
        }
    }

}