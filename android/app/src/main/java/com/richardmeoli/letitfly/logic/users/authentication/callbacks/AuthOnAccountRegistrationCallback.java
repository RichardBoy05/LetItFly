package com.richardmeoli.letitfly.logic.users.authentication.callbacks;

import com.google.firebase.auth.FirebaseUser;

public interface AuthOnAccountRegistrationCallback {
    // Interface for managing the outcome of a Firebase account registration.

    void onSuccess(FirebaseUser user);
    void onFailure(String error);
}
