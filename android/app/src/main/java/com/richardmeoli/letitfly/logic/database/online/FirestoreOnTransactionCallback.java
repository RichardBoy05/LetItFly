package com.richardmeoli.letitfly.logic.database.online;

public interface FirestoreOnTransactionCallback {
    // interface to manage Firestore transactions outcome

    void onSuccess();
    void onFailure();
}

