package com.richardmeoli.letitfly.logic.database.online;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Map;
import java.util.HashMap;

public class Firestore implements FirestoreContract {

    private static Firestore instance;

    private Firestore() {
    }

    public static Firestore getInstance() {

        if (instance == null) {
            instance = new Firestore();
        }
        return instance;
    }


    @Override
    public void storeDocument(String collection, @Nullable String id, Object[] values, final FirestoreCallback callback) {
        // stores a Document to the Firestore Database with the specified "id" and values;
        // pass a null id to make it generate directly by Firestore

        String[] keys;

        switch (collection) {

            case ROUTINES_COLLECTION:
                keys = R_FIELDS;
                break;

            case POSITIONS_COLLECTION:
                keys = P_FIELDS;
                break;

            default:
                callback.onFailure();
                return;
        }

        if (keys.length != values.length) {
            callback.onFailure();
            return;
        }

        Map<String, Object> document = new HashMap<>();
        int index = 0;

        for (String key : keys) {
            document.put(key, values[index]);
            index++;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if (id == null) {

            db.collection(collection).add(document)
                    .addOnSuccessListener(documentReference -> {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        callback.onSuccess();
                    })
                    .addOnFailureListener(e -> {
                        Log.w(TAG, "Error writing document", e);
                        callback.onFailure();
                    });

        } else {

            db.collection(collection).document(id)
                    .set(document)
                    .addOnSuccessListener(aVoid -> {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + id);
                        callback.onSuccess();
                    })
                    .addOnFailureListener(e -> {
                        Log.w(TAG, "Error writing document", e);
                        callback.onFailure();
                    });
        }

    }

    @Override
    public void readDocument(String collection, String id, FirestoreCallback callback) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(collection)
                .get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            callback.onRead();
                            Log.d(TAG, document.getId() + " => " + document.getData());
                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });

    }

    public void deleteDocument(String collection, @NonNull String id, final FirestoreCallback callback) {
        // deletes a Document from the Firestore Database by its "id";

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(collection).document(id)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    callback.onSuccess();
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error deleting document", e);
                    callback.onFailure();
                });


    }

}

