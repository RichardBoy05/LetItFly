package com.richardmeoli.letitfly.logic.users.backup.callbacks;

import com.richardmeoli.letitfly.logic.users.backup.BackupError;

public interface BackupVerifyUserCallback {
    // Interface for managing user verification in the backups system.

    void onSuccess();
    void onFailure(BackupError error);

}