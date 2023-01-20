package com.richardmeoli.letitfly.logic.database.online;

public interface FirestoreContract extends FirestoreAttributes {

    boolean storeDocument(String collection, String uuid, Object[] values);
    boolean deleteDocument(String collection, String uuid);

}
