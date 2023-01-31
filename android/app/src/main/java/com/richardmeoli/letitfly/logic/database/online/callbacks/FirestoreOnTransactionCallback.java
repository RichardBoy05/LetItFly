package com.richardmeoli.letitfly.logic.database.online.callbacks;

import com.richardmeoli.letitfly.logic.database.online.FirestoreError;

public interface FirestoreOnTransactionCallback {
    // interface to manage Firestore transactions outcome

    void onSuccess();
    void onFailure(FirestoreError error);
}

