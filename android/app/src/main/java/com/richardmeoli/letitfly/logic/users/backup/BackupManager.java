package com.richardmeoli.letitfly.logic.users.backup;

import android.util.Log;
import android.content.Context;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.richardmeoli.letitfly.logic.users.authentication.Authenticator;
import com.richardmeoli.letitfly.logic.users.authentication.AuthenticationError;
import com.richardmeoli.letitfly.logic.users.authentication.callbacks.AuthOnActionCallback;
import com.richardmeoli.letitfly.logic.users.backup.callbacks.BackupOnActionCallback;
import com.richardmeoli.letitfly.logic.users.backup.callbacks.BackupVerifyUserCallback;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class BackupManager {

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

    public void backupDatabase(Context context, final BackupOnActionCallback callback) {

        Authenticator auth = Authenticator.getInstance();

        verifyUser(new BackupVerifyUserCallback() {
            @Override
            public void onSuccess() { // if user is verified

                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                StorageReference databaseRef = storageRef.child(auth.getCurrentUser().getDisplayName() + "/letitfly.db");

                File databaseFile = context.getDatabasePath("letitfly.db");
                FileInputStream stream;

                try {

                    stream = new FileInputStream(databaseFile);

                } catch (FileNotFoundException e) {
                    Log.e(TAG, BackupError.LOCAL_DATABASE_NOT_FOUND_ERROR.toString(), e);
                    callback.onFailure(BackupError.LOCAL_DATABASE_NOT_FOUND_ERROR);
                    return;
                }

                databaseRef.putStream(stream)
                        .addOnSuccessListener(taskSnapshot -> {

                            Log.d(TAG, "Database uploaded successfully!");
                            callback.onSuccess();

                        })
                        .addOnFailureListener(e -> { // unexpected error

                            Log.e(TAG, BackupError.UNEXPECTED_ERROR.toString(), e);
                            callback.onFailure(BackupError.UNEXPECTED_ERROR);
                        });


            }

            @Override
            public void onFailure(BackupError error) {
                callback.onFailure(error);
            }
        });


    }

    public void donwloadDatabase(Context context, final BackupOnActionCallback callback) {

        Authenticator auth = Authenticator.getInstance();

        verifyUser(new BackupVerifyUserCallback() {
            @Override
            public void onSuccess() {

                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                StorageReference databaseRef = storageRef.child(auth.getCurrentUser().getDisplayName() + "/letitfly.db");

                File databaseFile = context.getDatabasePath("letitfly.db");

                databaseRef.getFile(databaseFile)

                        .addOnSuccessListener(taskSnapshot -> {
                            Log.d(TAG, "SQLite database file downloaded and saved to internal storage.");
                            callback.onSuccess();

                        })
                        .addOnFailureListener(e -> {

                            if (e instanceof StorageException && ((StorageException) e).getErrorCode() == StorageException.ERROR_OBJECT_NOT_FOUND) {

                                Log.w(TAG, BackupError.NO_SUCH_FILE_IN_STORAGE_ERROR.toString(), e);
                                callback.onFailure(BackupError.NO_SUCH_FILE_IN_STORAGE_ERROR);
                                return;
                            }

                            Log.e(TAG, BackupError.UNEXPECTED_ERROR.toString(), e);
                            callback.onFailure(BackupError.UNEXPECTED_ERROR);
                        });
            }

            @Override
            public void onFailure(BackupError error) {
                callback.onFailure(error);
            }
        });

    }

    private void verifyUser(final BackupVerifyUserCallback callback) {

        Authenticator auth = Authenticator.getInstance();

        auth.isAccountVerified(new AuthOnActionCallback() {
            @Override
            public void onSuccess() { // the user is verified
                callback.onSuccess();
            }

            @Override
            public void onFailure(AuthenticationError error) {

                if (error == AuthenticationError.NO_USER_LOGGED_IN_ERROR) {
                    Log.w(TAG, BackupError.NO_USER_LOGGED_IN_ERROR.toString());
                    callback.onFailure(BackupError.NO_USER_LOGGED_IN_ERROR);
                    return;
                }

                if (error == AuthenticationError.ACCOUNT_NOT_VERIFIED_ERROR) {
                    Log.w(TAG, BackupError.USER_NOT_VERIFIED_ERROR.toString());
                    callback.onFailure(BackupError.USER_NOT_VERIFIED_ERROR);
                    return;
                }

                Log.e(TAG, error.toString());
                callback.onFailure(BackupError.UNEXPECTED_ERROR);

            }
        });

    }

}