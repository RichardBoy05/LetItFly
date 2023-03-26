package com.richardmeoli.letitfly.ui.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.richardmeoli.letitfly.R;
import com.richardmeoli.letitfly.logic.database.local.sqlite.DatabaseAttributes;
import com.richardmeoli.letitfly.logic.database.local.tables.RoutinesTable;
import com.richardmeoli.letitfly.logic.users.authentication.AuthenticationError;
import com.richardmeoli.letitfly.logic.users.authentication.Authenticator;
import com.richardmeoli.letitfly.logic.users.authentication.callbacks.AuthOnEventCallback;
import com.richardmeoli.letitfly.ui.main.MainActivity;

public class RegisterActivity extends AppCompatActivity implements DatabaseAttributes, RoutinesTable {

    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText usernameField = findViewById(R.id.registerUsernameField);
        EditText emailField = findViewById(R.id.registerEmailField);
        EditText passwordField = findViewById(R.id.registerPasswordField);
        EditText confirmPasswordField = findViewById(R.id.registerConfirmPasswordField);
        Button alreadyHaveAccountButton = findViewById(R.id.alreadyHaveAccountButton);
        Button registerButton = findViewById(R.id.registerButton);
        Button resedVerificationEmail = findViewById(R.id.resendVerificationEmailButton);
        Button goButton = findViewById(R.id.registerGoButton);
        ProgressDialog progressDialog = new ProgressDialog(this);

        Authenticator auth = Authenticator.getInstance();

        registerButton.setOnClickListener(v -> {

            String username = usernameField.getText().toString();
            String email = emailField.getText().toString();
            String password = passwordField.getText().toString();
            String confirmPassword = confirmPasswordField.getText().toString();

            // fields validation

            if (username.equals("") || email.equals("") || password.equals("") || confirmPassword.equals("")) {
                Toast.makeText(this, "Fill all the fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (username.length() < R_AUTHOR_MIN_LENGTH || username.length() > R_AUTHOR_MAX_LENGTH) {
                Toast.makeText(this, "Username length must be within " + R_AUTHOR_MIN_LENGTH + " and " + R_AUTHOR_MAX_LENGTH + " characters!", Toast.LENGTH_SHORT).show();
            }

            if (!username.matches(R_AUTHOR_VALID_CHARACTERS)) {
                Toast.makeText(this, "Username contains invalid characters! Only letters, numbers and spaces are allowed!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "This email address is not valid!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 6) {
                Toast.makeText(this, "Password must be at least 6 characters long!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "The two password do not match!", Toast.LENGTH_SHORT).show();
                return;
            }

            // registration

            if (!auth.isUserConnected(this)){
                Toast.makeText(this, "Make sure to be connected to a network in order to register!", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.checkUsernameUnique(username, exists -> {

                if (exists) {
                    Toast.makeText(this, "The username '" + username + "' is already in use!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressDialog.setTitle("Registrazione");
                progressDialog.setMessage("Per favore attendi mentre vieni registrato...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                auth.registerUser(username, email, password, new AuthOnEventCallback() {
                    @Override
                    public void onSuccess() {

                        progressDialog.dismiss();

                        auth.sendVerificationEmail(new AuthOnEventCallback() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(RegisterActivity.this, "Account creato con successo! Ti abbiamo inviato una email di verifica. Segui le istruzioni per completare la registrazione!", Toast.LENGTH_SHORT).show();
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

        resedVerificationEmail.setOnClickListener(v -> {

            if (auth.getCurrentUser() == null) {
                Toast.makeText(this, "No user logged in!", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.sendVerificationEmail(new AuthOnEventCallback() {
                @Override
                public void onSuccess() {
                    Toast.makeText(RegisterActivity.this, "Email reinviata!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(AuthenticationError error) {

                }
            });

        });

        goButton.setOnClickListener(v -> {

            auth.isUserVerified(new AuthOnEventCallback() {
                @Override
                public void onSuccess() {

                    Toast.makeText(RegisterActivity.this, "Logged in as " + auth.getCurrentUser().getDisplayName() + ", " + auth.getCurrentUser().getDisplayName(), Toast.LENGTH_SHORT).show();
                    Intent myIntent = new Intent(RegisterActivity.this, MainActivity.class);
                    RegisterActivity.this.startActivity(myIntent);

                }

                @Override
                public void onFailure(AuthenticationError error) {
                    Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_SHORT).show();

                }
            });


        });

        alreadyHaveAccountButton.setOnClickListener(v -> {

            Intent myIntent = new Intent(this, LoginActivity.class);
            this.startActivity(myIntent);
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        Authenticator auth = Authenticator.getInstance();
        auth.signOutUser(); // make sure the previous user is signed out
        Log.d(TAG, "User signed out!");
    }
}