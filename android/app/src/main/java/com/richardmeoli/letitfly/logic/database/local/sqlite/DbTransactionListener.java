package com.richardmeoli.letitfly.logic.database.local.sqlite;

import android.util.Log;
import android.content.Context;
import androidx.annotation.NonNull;
import android.database.sqlite.SQLiteTransactionListener;

import com.richardmeoli.letitfly.logic.users.backup.BackupError;
import com.richardmeoli.letitfly.logic.users.backup.BackupManager;
import com.richardmeoli.letitfly.logic.users.backup.callbacks.BackupOnEventCallback;

public class DbTransactionListener implements SQLiteTransactionListener {

    private static final String TAG = "DbTransactionListener";
    private final Context context;

    public DbTransactionListener(@NonNull Context context) {
        this.context = context;
    }

    @Override
    public void onBegin() { // This method is called when a transaction begins
        Log.d(TAG, "Transaction begins!");
    }

    @Override
    public void onCommit() { // This method is called when a transaction is committed

        Log.d(TAG, "Transaction committed!");

        // performing backup

        BackupManager bm = BackupManager.getInstance();
        bm.backupDatabase(context, new BackupOnEventCallback() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "Database backed up successfully after transaction!");
            }

            @Override
            public void onFailure(BackupError error) {
                Log.e(TAG, error.toString());
            }
        });


    }

    @Override
    public void onRollback() { // This method is called when a transaction is rolled back
        Log.d(TAG, "Transaction rolled back!");
    }
}