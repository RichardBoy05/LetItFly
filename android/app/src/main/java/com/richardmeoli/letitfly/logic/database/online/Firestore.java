package com.richardmeoli.letitfly.logic.database.online;

import android.util.Log;
import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class Firestore implements FirestoreContract {

    private static Firestore instance;

    private Firestore() {
    }

    public static Firestore getInstance() { // singleton pattern

        if (instance == null) {
            instance = new Firestore();
        }
        return instance;
    }


    @Override
    public void storeDocument(String collection, String id, Object[] values, final FirestoreOnTransactionCallback callback) {
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

            default: // non-existent collection
                callback.onFailure();
                return;
        }

        if (keys.length != values.length) { // keys and value must have the same length
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

        if (id == null) { // uploads Document with Firestore generated ID

            db.collection(collection).add(document)
                    .addOnSuccessListener(documentReference -> {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        callback.onSuccess();
                    })
                    .addOnFailureListener(e -> {
                        Log.w(TAG, "Error writing document", e);
                        callback.onFailure();
                    });

        } else { // uploads Document with given id

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
    public void selectDocumentById(String collection, String[] fieldsToSelect, @NonNull String id, final FirestoreOnQueryCallback callback) {
        // selects the values of the selected fields 'fieldsToSelect'
        // (pass null to select from all fields)
        // from the given 'collection', where the id matches the given parameter 'id'

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(collection).document(id)
                .get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        List<Object> result = new ArrayList<>();

                        if (fieldsToSelect != null && fieldsToSelect.length > 0){



                        } else {


                        }


                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
    }

    @Override
    public void selectDocumentsByField(String collection, String[] fieldsToSelect, String whereField, Object value, final FirestoreOnQueryCallback callback) {
        // selects the values of the selected fields 'fieldsToSelect'
        // (pass null to select from all fields)
        // from the given 'collection', where the field 'whereField' matches the given
        // parameter 'value' (pass null to select all the fields).

    }

    public void deleteDocument(String collection, @NonNull String id, final FirestoreOnTransactionCallback callback) {
        // deletes a Document from the Firestore Database by its "id";

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(collection).document(id)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "DocumentSnapshot with ID " + id + " successfully deleted!");
                    callback.onSuccess();
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error deleting document", e);
                    callback.onFailure();
                });


    }


}