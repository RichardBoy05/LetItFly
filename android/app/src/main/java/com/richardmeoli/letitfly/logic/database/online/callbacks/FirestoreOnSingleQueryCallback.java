package com.richardmeoli.letitfly.logic.database.online.callbacks;

import com.richardmeoli.letitfly.logic.database.online.firestore.FirestoreError;

import java.util.Map;

public interface FirestoreOnSingleQueryCallback {
    // Interface for managing the outcome of single queries in Firestore.

    void onSuccess(Map<String, Object> list);
    void onFailure(FirestoreError error);
}