package com.coders.goodest.scannbuy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    EditText mPassword1EditText;
    EditText mPassword2EditText;
    Button mRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mPassword1EditText = (EditText) findViewById(R.id.password);
        mPassword2EditText = (EditText) findViewById(R.id.password2);
        mRegisterButton = (Button) findViewById(R.id.register_button);

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String password1string = mPassword1EditText.getText().toString();
                final String password2string = mPassword2EditText.getText().toString();

                if ( !(password1string.equals(password2string))){

                    String invalidPassword = "Passwords are not the same.";

                    Toast.makeText(getApplicationContext(), invalidPassword, Toast.LENGTH_SHORT ).show();
                    mPassword1EditText.setText("");
                    mPassword2EditText.setText("");
                }

                else{
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
            }
        });
    }
}
