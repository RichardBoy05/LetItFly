package com.richardmeoli.letitfly.ui.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.richardmeoli.letitfly.R;
import com.richardmeoli.letitfly.ui.main.MainActivity;

public class LoginActivity extends AppCompatActivity {

    private Button createNewAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        createNewAccountButton = findViewById(R.id.createNewAccountButton);
        createNewAccountButton.setOnClickListener(v -> {

            Intent myIntent = new Intent(LoginActivity.this, RegisterActivity.class);
            LoginActivity.this.startActivity(myIntent);

        });
    }
}