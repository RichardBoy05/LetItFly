package com.richardmeoli.letitfly.logic.database.online.callbacks;

public interface FirestoreDocumentExistsCallback {
    // Interface for checking the existence of a document in Firestore.

    void onDocumentExists (boolean exists);
}