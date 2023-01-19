package com.richardmeoli.letitfly.logic.database.online;

public interface FirestoreContract extends FirestoreAttributes {

    void storeDocument(String uuid, String collection, Object[] values, final FirebaseCallback callback);

}
