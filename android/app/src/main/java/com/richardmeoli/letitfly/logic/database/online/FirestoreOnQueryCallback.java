package com.richardmeoli.letitfly.logic.database.online;

import java.util.List;

public interface FirestoreOnQueryCallback {
    // interface to manage Firestore queries outcome

    void onQuery(List<Object> list);
    void onQuery(Object[] list);
}
