package com.richardmeoli.letitfly.logic.users.authentication.callbacks;

public interface AuthUserExistsCallback {
    // Interface for checking the existence of a user in the authentication system.
    void onUserExists(boolean exists);
}