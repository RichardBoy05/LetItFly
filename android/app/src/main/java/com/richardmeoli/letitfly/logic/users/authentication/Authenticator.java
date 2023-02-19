package com.richardmeoli.letitfly.logic.users.authentication;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.richardmeoli.letitfly.logic.users.authentication.callbacks.OnAccountRegistrationCallback;

public class Authenticator {

    private static Authenticator instance;
    private static final String TAG = "Authenticator";

    private Authenticator() {
    }

    public static Authenticator getInstance() {

        if (instance == null) {
            instance = new Authenticator();
        }

        return instance;
    }

    public void registerUser(String username, String email, String password, final OnAccountRegistrationCallback callback) {

        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    FirebaseUser user = auth.getCurrentUser();
                    callback.onSuccess(user);
                })
                .addOnFailureListener(e -> {
                    callback.onFailure(e.getMessage());
                });

    }

    public void loginUser(String emailOrUsername, String password) {
    }

    public void sendVerficationEmail(String email) {
        System.out.println("Sending auth mail");
    }

    public void resetPassword(String emailOrUsername) {
    }

}
