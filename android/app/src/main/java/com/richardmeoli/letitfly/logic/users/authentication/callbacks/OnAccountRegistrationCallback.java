package com.richardmeoli.letitfly.logic.users.authentication.callbacks;

import com.google.firebase.auth.FirebaseUser;

public interface OnAccountRegistrationCallback {
    // Interface for managing the outcome of a Firebase authentication.

    void onSuccess(FirebaseUser user);
    void onFailure(String error);
}
