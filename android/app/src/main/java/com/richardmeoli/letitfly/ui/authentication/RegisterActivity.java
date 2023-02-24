package com.richardmeoli.letitfly.ui.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.richardmeoli.letitfly.R;
import com.richardmeoli.letitfly.logic.database.local.tables.RoutinesTable;
import com.richardmeoli.letitfly.logic.users.authentication.AuthenticationError;
import com.richardmeoli.letitfly.logic.users.authentication.Authenticator;
import com.richardmeoli.letitfly.logic.users.authentication.callbacks.AuthOnActionCallback;

public class RegisterActivity extends AppCompatActivity implements RoutinesTable {

    private EditText usernameField;
    private EditText emailField;
    private EditText passwordField;
    private EditText confirmPasswordField;
    private Button alreadyHaveAccountButton;
    private Button registerButton;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameField = findViewById(R.id.registerUsernameField);
        emailField = findViewById(R.id.registerEmailField);
        passwordField = findViewById(R.id.registerPasswordField);
        confirmPasswordField = findViewById(R.id.registerConfirmPasswordField);
        alreadyHaveAccountButton = findViewById(R.id.alreadyHaveAccountButton);
        registerButton = findViewById(R.id.registerButton);
        progressDialog = new ProgressDialog(this);

        registerButton.setOnClickListener(v -> {

//            String username = usernameField.getText().toString();
//            String email = emailField.getText().toString();
//            String password = passwordField.getText().toString();
//            String confirmPassword = confirmPasswordField.getText().toString();
//
//            if (username.equals("") || email.equals("") || password.equals("") || confirmPassword.equals("")){
//                Toast.makeText(this, "Fill all the fields", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            if (username.length() < R_AUTHOR_MIN_LENGTH || username.length() > R_AUTHOR_MAX_LENGTH){
//                Toast.makeText(this, "Username length must be within 3 and 20 characters!", Toast.LENGTH_SHORT).show();
//            }
//
//            if (!username.matches(R_AUTHOR_VALID_CHARACTERS){
//                Toast.makeText(this, "Username contains invalid characters!", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
//                Toast.makeText(this, "This email ain't valid!", Toast.LENGTH_SHORT).show();
//                return;
//            }

//            if (password.length < 6){
//                Toast.makeText(this, "Password must be at least 6 characters long!", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            if (!password.equals(confirmPassword)){
//                Toast.makeText(this, "The two password do not match!", Toast.LENGTH_SHORT).show();
//                return;
//            }

            String username = "username";
            String email = "email";
            String password = "password";

            Authenticator auth = Authenticator.getInstance();

            auth.checkUsernameUnique(username, exists -> {

                if (exists) {
                    Toast.makeText(this, "The username '" + username + "' is already in use!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressDialog.setTitle("Registrazione");
                progressDialog.setMessage("Per favore attendi mentre vieni registrato...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                auth.registerUser(username, email, password, new AuthOnActionCallback() {
                    @Override
                    public void onSuccess() {

                        progressDialog.dismiss();

                        auth.sendVerificationEmail(new AuthOnActionCallback() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(RegisterActivity.this, "Ottimo! Verifica la mail ora!", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(AuthenticationError error) {
                                Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_SHORT).show();

                            }
                        });



                    }

                    @Override
                    public void onFailure(AuthenticationError error) {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_SHORT).show();

                    }
                });

            });


        });

        alreadyHaveAccountButton.setOnClickListener(v -> {

            Intent myIntent = new Intent(RegisterActivity.this, LoginActivity.class);
            RegisterActivity.this.startActivity(myIntent);
        });


    }
}