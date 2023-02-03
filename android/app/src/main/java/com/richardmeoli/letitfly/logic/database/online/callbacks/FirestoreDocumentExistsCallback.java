package com.richardmeoli.letitfly.logic.database.online.callbacks;

public interface FirestoreDocumentExistsCallback {
    // interface to check if a document exists in Firestore

    void onDocumentExists (boolean exists);
}