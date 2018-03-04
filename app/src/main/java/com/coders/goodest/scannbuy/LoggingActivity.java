package com.coders.goodest.scannbuy;

import android.app.Activity;
import android.content.Intent;

import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoggingActivity extends AppCompatActivity {

    private EditText mEmailView;
    private EditText mPasswordView;
    private Button mButtonLogin;
    private Button mButtonRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logging);

        mEmailView = (EditText) findViewById(R.id.login);
        mPasswordView = (EditText) findViewById(R.id.password);
        mButtonLogin = (Button) findViewById(R.id.email_sign_in_button);


        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String emailtext = mEmailView.getText().toString();
                String passwordtext = mPasswordView.getText().toString();
                if (emailtext.matches("") || passwordtext.matches("")) {
                    Toast.makeText(LoggingActivity.this, R.string.emptyedittext, Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    Intent intent = new Intent(LoggingActivity.this, MainActivity.class);
                    startActivity(intent);
                }

            }
        });

//        mButtonRegister.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//
//                Intent intent = new Intent(LoggingActivity.this, REGISTERCLASS.class);
//
//                startActivity(intent);
//            }
//        });

    }
}

