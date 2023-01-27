package com.richardmeoli.letitfly.logic.database.online;

public interface FirestoreContract extends FirestoreAttributes {

    String TAG = "Firestore";

    void storeDocument(String collection, String id, Object[] value, final FirestoreCallback callback);
    void deleteDocument(String collection, String id, final FirestoreCallback callback);
    void readDocument(String collection, String id, final FirestoreCallback callback);

}
