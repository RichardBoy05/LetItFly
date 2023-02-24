package com.richardmeoli.letitfly.ui.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.richardmeoli.letitfly.R;
import com.richardmeoli.letitfly.logic.users.authentication.AuthenticationError;
import com.richardmeoli.letitfly.logic.users.authentication.Authenticator;
import com.richardmeoli.letitfly.logic.users.authentication.callbacks.AuthOnActionCallback;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private Button forgotPasswordButton;
    private Button createNewAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.loginButton);
        forgotPasswordButton = findViewById(R.id.forgotPasswordButton);
        createNewAccountButton = findViewById(R.id.createNewAccountButton);

        loginButton.setOnClickListener(v -> {

            Authenticator auth = Authenticator.getInstance();
            auth.signOutUser();

            auth.signInUser("emailOrUsername", "password", new AuthOnActionCallback() {
                @Override
                public void onSuccess() {
                    Toast.makeText(LoginActivity.this, auth.getCurrentUser().getEmail() + ", " + auth.getCurrentUser().getDisplayName(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(AuthenticationError error) {
                    Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_SHORT).show();

                }
            });

        });

        forgotPasswordButton.setOnClickListener(v -> {

            Authenticator auth = Authenticator.getInstance();

            auth.resetPassword("emailOrUsername", new AuthOnActionCallback() {
                @Override
                public void onSuccess() {
                    Toast.makeText(LoginActivity.this, "Email sent", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(AuthenticationError error) {
                    Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            });

        });


        createNewAccountButton.setOnClickListener(v -> {

            Intent myIntent = new Intent(LoginActivity.this, RegisterActivity.class);
            LoginActivity.this.startActivity(myIntent);

        });
    }
}