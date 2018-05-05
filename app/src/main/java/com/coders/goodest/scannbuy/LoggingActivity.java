package com.coders.goodest.scannbuy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.coders.goodest.scannbuy.services.UserService;

public class LoggingActivity extends AppCompatActivity {

    private UserService userService;
    private EditText mEmailView;
    private EditText mPasswordView;
    private Button mButtonLogin;
    private Button mButtonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logging);

        userService = new UserService();
        mEmailView = (EditText) findViewById(R.id.login);
        mPasswordView = (EditText) findViewById(R.id.password);
        mButtonLogin = (Button) findViewById(R.id.email_sign_in_button);
        mButtonRegister = (Button) findViewById(R.id.register_button_loggin_activity);


        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String emailtext = mEmailView.getText().toString();
                String passwordtext = mPasswordView.getText().toString();
                if (emailtext.matches("") || passwordtext.matches("")) {
                    Toast.makeText(LoggingActivity.this, R.string.emptyedittext, Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    try {
                        userService.login(getApplicationContext(), emailtext, passwordtext);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(LoggingActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed(){
        finishAffinity();
        System.exit(0);
    }
}

