package com.richardmeoli.letitfly.logic.database.online;

import java.util.UUID;

public interface FirestoreContract extends FirestoreAttributes {

    void storeDocument(UUID uuid, String collection, Object[] values, final FirebaseCallback callback);

}
