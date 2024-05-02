package com.example.as1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * The page that is visible upon startup. Two buttons will take you to sign up or login
 * @author Noah Ross
 */
public class StartupScreen extends AppCompatActivity {

    Button SignupButton;

    Button LoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startupscreen);

        SignupButton = findViewById(R.id.toSignupBtn);
        LoginButton = findViewById(R.id.toLoginBtn);

        SignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(StartupScreen.this, SignupPage.class);
                startActivity(intent);
            }
        });

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(StartupScreen.this, LoginPage.class);
                startActivity(intent);
            }
        });

    }

}