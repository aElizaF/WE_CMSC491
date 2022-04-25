package com.example.womensempowerment;

import static android.hardware.SensorManager.SENSOR_DELAY_FASTEST;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    // UI components
    private Button buttonRegister, buttonLogin, buttonGetHelpNow;
    private TextView textView;
    private ImageView imageView;
    private EditText password, username;

    //Login components
    public static Cognito userpool;
    //TODO need to add AWS Keys and all the permissions and gradle stuff to make this work

    // Location Details
    private SensorManager sensorManager;
    private Sensor acc, gyro, mag;
    //TODO need GPS for location search to find resources?


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //UI initialization
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonGetHelpNow = (Button) findViewById(R.id.buttonGetHelpNow);
        textView = (TextView) findViewById(R.id.textView);
        imageView = (ImageView) findViewById(R.id.imageView);
        password = (EditText) findViewById(R.id.editTextTextPassword);
        username = (EditText) findViewById(R.id.editTextUserName);

        //Sensors
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        acc = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mag = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorManager.registerListener((SensorEventListener) MainActivity.this, acc, SENSOR_DELAY_FASTEST);
        Log.d("TAG", "onCreate: Registered accelerometer listener");
        sensorManager.registerListener((SensorEventListener) MainActivity.this, gyro, SENSOR_DELAY_FASTEST);
        Log.d("TAG", "onCreate: Registered gyroscope listener");
        sensorManager.registerListener((SensorEventListener) MainActivity.this, mag, SENSOR_DELAY_FASTEST);
        Log.d("TAG", "onCreate: Registered magnetic field listener");

        // AWS
        Cognito userpool = new Cognito(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonGetHelpNow:
                // TODO Add action to send user to resources

                break;
            case R.id.buttonLogin:
                userpool.signInUser(username.getText().toString(), password.getText().toString());

                break;
            case R.id.buttonRegister:
                // TODO Add registration code to this button
        }
    }

}