package com.richardmeoli.letitfly.logic.users.backup;

import androidx.annotation.NonNull;

public enum BackupError {
    // Enum for representing different error types that can occur in the backup system.

    //--------------- ERRORS ---------------//

    UNEXPECTED_ERROR("UNEXPECTED_ERROR", "An unexpected error has occured!"),
    NO_USER_LOGGED_IN_ERROR("NO_USER_LOGGED_IN_ERROR", "No user is currently logged in!"),
    USER_NOT_VERIFIED_ERROR("USER_NOT_VERIFIED_ERROR", "The current user's account has not been verified!"),
    LOCAL_DATABASE_NOT_FOUND_ERROR("LOCAL_DATABASE_NOT_FOUND_ERROR", "The local database file was not found at the specified path!"),
    NO_SUCH_FILE_IN_STORAGE_ERROR("NO_SUCH_FILE_IN_STORAGE_ERROR", "This file does not exist in the Firebase Storage!");

    //--------------- ERRORS ---------------//

    private final String name;
    private final String description;

    BackupError(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @NonNull
    @Override
    public String toString() {
        return name + ": " + description;
    }
}
