package com.richardmeoli.letitfly.logic.database.online.callbacks;

import com.richardmeoli.letitfly.logic.database.online.FirestoreError;

import java.util.List;
import java.util.Map;

public interface FirestoreOnMultipleQueryCallback {
    // interface to manage Firestore multiple queries outcome

    void onSuccess(List<Map<String, Object>> list);
    void onFailure(FirestoreError error);
}
