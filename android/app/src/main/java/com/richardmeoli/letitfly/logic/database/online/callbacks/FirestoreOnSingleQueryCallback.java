package com.richardmeoli.letitfly.logic.database.online.callbacks;

import com.richardmeoli.letitfly.logic.database.online.FirestoreError;

import java.util.Map;

public interface FirestoreOnSingleQueryCallback {
    // interface to manage Firestore single queries outcome

    void onSuccess(Map<String, Object> list);
    void onFailure(FirestoreError error);
}