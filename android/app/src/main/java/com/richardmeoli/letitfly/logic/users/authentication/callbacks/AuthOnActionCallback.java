package com.richardmeoli.letitfly.logic.users.authentication.callbacks;

import com.richardmeoli.letitfly.logic.users.authentication.AuthenticationError;

public interface AuthOnActionCallback {
    // Interface for managing the outcome of a Firebase account registration.

    void onSuccess();
    void onFailure(AuthenticationError error);
}
