package com.example.womensempowerment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText username, password, email, code;
    private Button register, confirm;
    public static Cognito userpool;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.editTextTextPassword);
        code = (EditText) findViewById(R.id.editTextNumber);
        email = (EditText) findViewById(R.id.editTextTextEmailAddress2);

        register = (Button) findViewById(R.id.button3);
        confirm = (Button) findViewById(R.id.button4);
        register.setOnClickListener(this);
        confirm.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button3:
                Intent myIntent = new Intent(this, RegisterActivity.class);
                startActivity(myIntent);
                break;
            case R.id.button4:
                MainActivity.userpool.SignUpUser(email.getText().toString(), code.getText().toString());
                break;
        }


    }
}
