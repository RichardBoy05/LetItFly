package com.richardmeoli.letitfly.logic.users.authentication.callbacks;

import com.richardmeoli.letitfly.logic.users.authentication.AuthenticationError;

public interface AuthRetrieveEmailCallback {
    // Interface to retrieve the email address of a username and manage its result.

    void onSuccess(String email);
    void onFailure(AuthenticationError error);

}
