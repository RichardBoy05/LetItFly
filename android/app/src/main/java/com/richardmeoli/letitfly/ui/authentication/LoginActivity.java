package com.richardmeoli.letitfly.ui.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.app.ProgressDialog;

import com.richardmeoli.letitfly.R;
import com.richardmeoli.letitfly.logic.database.local.sqlite.Database;
import com.richardmeoli.letitfly.logic.users.authentication.Authenticator;
import com.richardmeoli.letitfly.logic.users.authentication.AuthenticationError;
import com.richardmeoli.letitfly.logic.users.authentication.callbacks.AuthOnEventCallback;
import com.richardmeoli.letitfly.logic.users.backup.BackupError;
import com.richardmeoli.letitfly.logic.users.backup.BackupManager;
import com.richardmeoli.letitfly.logic.users.backup.callbacks.BackupOnEventCallback;
import com.richardmeoli.letitfly.ui.main.MainActivity;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText emailOrUsernameField = findViewById(R.id.loginEmailorUserField);
        EditText passwordField = findViewById(R.id.loginPasswordField);
        Button loginButton = findViewById(R.id.loginButton);
        Button forgotPasswordButton = findViewById(R.id.forgotPasswordButton);
        Button createNewAccountButton = findViewById(R.id.createNewAccountButton);
        ProgressDialog progressDialog = new ProgressDialog(this);

        Authenticator auth = Authenticator.getInstance();
        auth.signOutUser(); // make sure the previous user is signed out
        Log.d(TAG, "User signed out!");

        loginButton.setOnClickListener(v -> {

            String emailOrUsername = emailOrUsernameField.getText().toString();
            String password = passwordField.getText().toString();

            // fields validation

            if (emailOrUsername.equals("") || password.equals("")) {
                Toast.makeText(this, "Fill all the fields", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.signOutUser(); // make sure the previous user is signed out

            progressDialog.setTitle("Accesso");
            progressDialog.setMessage("Per favore attendi mentre eseguiamo il login...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            auth.signInUser(emailOrUsername, password, new AuthOnEventCallback() {

                @Override
                public void onSuccess() { // user successfully signed in

                    auth.isUserVerified(new AuthOnEventCallback() {
                        @Override
                        public void onSuccess() { // user successfully verified

                            BackupManager bm = BackupManager.getInstance();
                            bm.donwloadDatabase(LoginActivity.this, new BackupOnEventCallback() {

                                @Override
                                public void onSuccess() { // user's database successfully downloaded

                                    progressDialog.dismiss();

                                    Log.d(TAG, "User data retrieved successfully!");
                                    Toast.makeText(LoginActivity.this, "Logged in as " + auth.getCurrentUser().getDisplayName() + ", " + auth.getCurrentUser().getDisplayName(), Toast.LENGTH_SHORT).show();
                                    Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
                                    LoginActivity.this.startActivity(myIntent);

                                }

                                @Override
                                public void onFailure(BackupError error) { // failed to download database

                                    progressDialog.dismiss();

                                    if (error == BackupError.NO_SUCH_FILE_IN_STORAGE_ERROR) {

                                        Log.e(TAG, "User has no data to retrieve");
                                        Database.getInstance(LoginActivity.this).wipeDatabase(LoginActivity.this);

//                                        Toast.makeText(LoginActivity.this, "Logged in as " + auth.getCurrentUser().getDisplayName() + ", " + auth.getCurrentUser().getDisplayName(), Toast.LENGTH_SHORT).show();
                                        Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
                                        LoginActivity.this.startActivity(myIntent);
                                        return;

                                    }

                                    Log.e(TAG, error.toString());
                                    Toast.makeText(LoginActivity.this, "Impossibile recuperare i dati dell'utente", Toast.LENGTH_SHORT).show();
                                    auth.signOutUser();

                                }

                            });

                        }

                        @Override
                        public void onFailure(AuthenticationError error) { // user not verified or erification failed

                            progressDialog.dismiss();
                            Log.e(TAG, "User not verified or verification failed!");
                            Log.e(TAG, error.toString());
                            Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_SHORT).show();

                        }
                    });

                }

                @Override
                public void onFailure(AuthenticationError error) { // user authentication failed

                    progressDialog.dismiss();
                    Log.e(TAG, "User authentication failed!");
                    Log.e(TAG, error.toString());
                    Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_SHORT).show();

                }
            });

        });

        forgotPasswordButton.setOnClickListener(v -> {

            String emailOrUsername = emailOrUsernameField.getText().toString();

            if (emailOrUsername.equals("")) {
                Toast.makeText(this, "Il campo email è vuoto!", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.resetPassword(emailOrUsername, new AuthOnEventCallback() {
                @Override
                public void onSuccess() {
                    Toast.makeText(LoginActivity.this, "Email successfully sent!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(AuthenticationError error) {
                    Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            });

        });


        createNewAccountButton.setOnClickListener(v -> {

            Intent myIntent = new Intent(this, RegisterActivity.class);
            this.startActivity(myIntent);

        });
    }
}