package com.richardmeoli.letitfly.logic.users.authentication;

import androidx.annotation.NonNull;

public enum AuthenticationError {
    // Enum for representing different error types that can occur in the authentication system.

    //--------------- ERRORS ---------------//

    REGISTRATION_ERROR("REGISTRATION_ERROR", "An error occured during the registration process!");

    //--------------- ERRORS ---------------//

    private final String name;
    private final String description;

    AuthenticationError(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @NonNull
    @Override
    public String toString() {
        return name + ": " + description;
    }

}
