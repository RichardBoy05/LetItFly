package com.richardmeoli.letitfly.logic.database.online.callbacks;

import com.richardmeoli.letitfly.logic.database.online.firestore.FirestoreError;

public interface FirestoreOnTransactionCallback {
    // Interface for managing the outcome of Firestore transactions.

    void onSuccess();
    void onFailure(FirestoreError error);
}