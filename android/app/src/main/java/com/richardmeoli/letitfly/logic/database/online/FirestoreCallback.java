package com.richardmeoli.letitfly.logic.database.online;

public interface FirestoreCallback {
    // interface to manage Firestore onSuccess and onFailure callbacks

    void onSuccess();
    void onFailure();
    void onRead(Has);
}
