package com.richardmeoli.letitfly.logic.database.online.callbacks;

import com.richardmeoli.letitfly.logic.database.online.firestore.FirestoreError;

import java.util.Map;
import java.util.List;

public interface FirestoreOnMultipleQueryCallback {
    // Interface for managing the outcome of multiple queries in Firestore.

    void onSuccess(List<Map<String, Object>> list);
    void onFailure(FirestoreError error);
}