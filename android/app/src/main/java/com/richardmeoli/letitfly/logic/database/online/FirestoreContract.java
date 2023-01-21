package com.richardmeoli.letitfly.logic.database.online;

public interface FirestoreContract extends FirestoreAttributes {

    String TAG = "Firestore";

    boolean storeDocument(String collection, String id, Object[] value);
    boolean deleteDocument(String collection, String id);

}
