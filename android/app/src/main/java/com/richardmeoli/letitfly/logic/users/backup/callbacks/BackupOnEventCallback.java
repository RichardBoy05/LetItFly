package com.richardmeoli.letitfly.logic.users.backup.callbacks;

import com.richardmeoli.letitfly.logic.users.backup.BackupError;

public interface BackupOnEventCallback {
    // Interface for managing the outcome of a backup operation with Firebase Storage.

    void onSuccess();
    void onFailure(BackupError error);

}