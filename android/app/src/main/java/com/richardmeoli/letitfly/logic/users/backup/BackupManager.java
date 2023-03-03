package com.richardmeoli.letitfly.logic.users.backup;

import android.util.Log;
import android.content.Context;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.richardmeoli.letitfly.logic.users.authentication.Authenticator;
import com.richardmeoli.letitfly.logic.users.backup.callbacks.BackupOnEventCallback;
import com.richardmeoli.letitfly.logic.database.local.sqlite.DatabaseAttributes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class BackupManager implements DatabaseAttributes {

    private static BackupManager instance;
    private static final String TAG = "BackupManager";

    private BackupManager() {
    }

    public static BackupManager getInstance() { // singleton pattern

        if (instance == null) {
            instance = new BackupManager();
        }
        return instance;
    }

    //--------------- BackupManager methods ---------------//

    public void backupDatabase(Context context, final BackupOnEventCallback callback) {
        // Backs up the user's local database to the directory associated with their
        // username on the Firebase Storage, after checking if the user is authenticated.

        Authenticator auth = Authenticator.getInstance();

        if (auth.getCurrentUser() == null){ // user not authenticated
            callback.onFailure(BackupError.NO_USER_LOGGED_IN_ERROR);
            return;
        }

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference databaseRef = storageRef.child(auth.getCurrentUser().getDisplayName() + "/" + DATABASE_NAME);

        File databaseFile = context.getDatabasePath(DATABASE_NAME);
        FileInputStream stream;

        try {

            stream = new FileInputStream(databaseFile);

        } catch (FileNotFoundException e) { // local database not found
            Log.e(TAG, BackupError.LOCAL_DATABASE_NOT_FOUND_ERROR.toString(), e);
            callback.onFailure(BackupError.LOCAL_DATABASE_NOT_FOUND_ERROR);
            return;
        }

        databaseRef.putStream(stream)
                .addOnSuccessListener(taskSnapshot -> { // success

                    Log.d(TAG, "SQLite database file uploaded successfully!");
                    callback.onSuccess();

                })
                .addOnFailureListener(e -> { // unexpected error

                    Log.e(TAG, BackupError.UNEXPECTED_ERROR.toString(), e);
                    callback.onFailure(BackupError.UNEXPECTED_ERROR);
                });

    }

    public void donwloadDatabase(Context context, final BackupOnEventCallback callback) {
        // Downloads the database associated with the username and replaces the former
        // local database with the new one, after checking if the user is authenticated.
        // Returns NO_SUCH_FILE_IN_STORAGE_ERROR if the current user does not have any
        // backup of their data online, meaning they still have not interacted with the
        // local database.

        Authenticator auth = Authenticator.getInstance();

        if (auth.getCurrentUser() == null){ // user not authenticated
            callback.onFailure(BackupError.NO_USER_LOGGED_IN_ERROR);
            return;
        }

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference databaseRef = storageRef.child(auth.getCurrentUser().getDisplayName() + "/" + DATABASE_NAME);

        File databaseFile = context.getDatabasePath(DATABASE_NAME);

        databaseRef.getFile(databaseFile)

                .addOnSuccessListener(taskSnapshot -> {
                    Log.d(TAG, "SQLite database file downloaded and saved to internal storage.");
                    callback.onSuccess();

                })
                .addOnFailureListener(e -> {

                    if (e instanceof StorageException) {
                        // the user does not have any data to retrieve

                        Log.w(TAG, BackupError.NO_SUCH_FILE_IN_STORAGE_ERROR.toString(), e);
                        callback.onFailure(BackupError.NO_SUCH_FILE_IN_STORAGE_ERROR);

                    } else { // any other error

                        Log.e(TAG, BackupError.UNEXPECTED_ERROR.toString(), e);
                        callback.onFailure(BackupError.UNEXPECTED_ERROR);
                    }
                });

    }

}