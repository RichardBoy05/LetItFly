package com.richardmeoli.letitfly.logic.users.authentication;

import androidx.annotation.NonNull;

public enum AuthenticationError {
    // Enum for representing different error types that can occur in the authentication system.

    //--------------- ERRORS ---------------//

    UNEXPECTED_ERROR("UNEXPECTED_ERROR", "An unexpected error has occured!"),
    USER_DOES_NOT_EXIST_ERROR("USER_DOES_NOT_EXIST_ERROR", "Attempt to refer to a non-existent user!"),
    NO_USER_LOGGED_IN_ERROR("NO_USER_LOGGED_IN_ERROR", "No user is currently logged in!"),
    USERNAME_ASSIGNMENT_ERROR("USERNAME_ASSIGNMENT_ERROR", "An error occurred while assigning the username to its account!"),
    USER_COLLISION_ERROR("USER_COLLISION_ERROR", "The email address is already in use by another account!"),
    USER_RELOADING_ERROR("ACCOUNT_RELOADING_ERROR", "An error occurred while reloading the current user data!"),
    EMAIL_VERIFICATION_ERROR("EMAIL_VERIFICATION_ERROR", "Unable to send an email to verify this account!"),
    EMAIL_RESET_ERROR("EMAIL_RESET_ERROR", "Unable to send an email to reset this account password!"),
    FIRESTORE_ERROR("FIRESTORE_ERROR", "An error occurred while interacting with the Firestore database!"),
    USER_NOT_VERIFIED_ERROR("USER_NOT_VERIFIED_ERROR", "This user's email address has not been verified yet!"),
    INVALID_PASSWORD_ERROR("INVALID_PASSWORD_ERROR", "The specified credentials are invalid!"),
    TOO_MANY_REQUESTS_ERROR("TOO_MANY_REQUESTS_ERROR", "Access to this account has been temporarily disabled due to many failed login attempts!");

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