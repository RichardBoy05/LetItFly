package com.richardmeoli.letitfly.logic.database.online;

import android.util.Log;

import androidx.annotation.NonNull;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.richardmeoli.letitfly.logic.database.online.callbacks.FirestoreOnTransactionCallback;
import com.richardmeoli.letitfly.logic.database.online.callbacks.FirestoreOnSingleQueryCallback;
import com.richardmeoli.letitfly.logic.database.online.callbacks.FirestoreOnMultipleQueryCallback;

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
                callback.onFailure(FirestoreError.NO_SUCH_COLLECTION);
                return;
        }

        if (keys.length != values.length) { // keys and value must have the same length
            callback.onFailure(FirestoreError.KEYS_VALUES_MISMATCH);
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
                        Log.w(TAG, FirestoreError.ERROR_WRITING_DOCUMENT.toString(), e);
                        callback.onFailure(FirestoreError.ERROR_WRITING_DOCUMENT);
                    });

        } else { // uploads Document with given id

            db.collection(collection).document(id)
                    .set(document)
                    .addOnSuccessListener(aVoid -> {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + id);
                        callback.onSuccess();
                    })
                    .addOnFailureListener(e -> {
                        Log.w(TAG, FirestoreError.ERROR_WRITING_DOCUMENT.toString(), e);
                        callback.onFailure(FirestoreError.ERROR_WRITING_DOCUMENT);
                    });
        }

    }

    public void deleteDocumentById(String collection, @NonNull String id, final FirestoreOnTransactionCallback callback) {
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
                    callback.onFailure(FirestoreError.ERROR_DELETING_DOCUMENT);
                });


    }

    @Override
    public void deleteDocumentsByField(String collection, String whereField, Object value, FirestoreOnTransactionCallback callback) {
        // deletes a Document from the Firestore Database
        // where the field 'whereField' (pass null to delete all the documents)
        // matches the given parameter 'value'.

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query query;

        if (whereField == null) {
            query = db.collection(collection);
        } else {
            query = db.collection(collection).whereEqualTo(whereField, value);
        }

        query.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            document.getReference().delete();
                            Log.d(TAG, "DocumentSnapshot with ID " + document.getId() + " successfully deleted!");
                        }

                        callback.onSuccess();
                    } else {
                        Log.d("TAG", FirestoreError.ERROR_DELETING_DOCUMENTS.toString(), task.getException());
                        callback.onFailure(FirestoreError.ERROR_DELETING_DOCUMENTS);
                    }
                });

    }

    @Override
    public void updateDocumentById(String collection, String[] fieldsToUpdate, Object[] values, String id, FirestoreOnTransactionCallback callback) {
        // updates a Document of the Firestore Database with the new field-value pairs, by its 'id' ;

        if (fieldsToUpdate.length != values.length) { // keys and value must have the same length
            callback.onFailure(FirestoreError.KEYS_VALUES_MISMATCH);
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> updates = new HashMap<>();
        int index = 0;

        for (String field : fieldsToUpdate) {
            updates.put(field, values[index]);
            index++;
        }

        db.collection(collection).document(id)
                .update(updates)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "DocumentSnapshot successfully updated!");
                    callback.onSuccess();
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, FirestoreError.ERROR_UPDATING_DOCUMENT.toString(), e);
                    callback.onFailure(FirestoreError.ERROR_UPDATING_DOCUMENT);
                });

    }

    @Override
    public void updateDocumentsByField(String collection, String[] fieldsToUpdate, Object[] values, String whereField, Object value, FirestoreOnTransactionCallback callback) {
        // updates a Document of the Firestore Database with the new field-value pairs,
        // where the field 'whereField' (pass null to delete all the documents)
        // matches the given parameter 'value'.

        if (fieldsToUpdate.length != values.length) { // keys and value must have the same length
            callback.onFailure(FirestoreError.KEYS_VALUES_MISMATCH);
            return;
        }

        Map<String, Object> updates = new HashMap<>();
        int index = 0;

        for (String field : fieldsToUpdate) {
            updates.put(field, values[index]);
            index++;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query query;

        if (whereField == null) {
            query = db.collection(collection);
        } else {
            query = db.collection(collection).whereEqualTo(whereField, value);
        }

        query.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            document.getReference().update(updates);
                            Log.d(TAG, "DocumentSnapshot with ID " + document.getId() + " successfully updated!");
                        }

                        callback.onSuccess();
                    } else {
                        Log.d("TAG", FirestoreError.ERROR_UPDATING_DOCUMENTS.toString(), task.getException());
                        callback.onFailure(FirestoreError.ERROR_UPDATING_DOCUMENTS);
                    }
                });

    }

    @Override
    public void selectDocumentById(String collection, ArrayList<String> fieldsToSelect, @NonNull String id, final FirestoreOnSingleQueryCallback callback) {
        // selects the values of the selected fields 'fieldsToSelect'
        // (pass null to select all fields)
        // from the given 'collection', where the id matches the given parameter 'id'.
        // IMPORTANT: if one of the elements of 'fieldsToSelect' is not an actual field of
        // the 'collection', it just won't be added in the resulting hashMap, but it won't
        // produce any errors.

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(collection).document(id)
                .get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();

                        if (document.exists()) {

                            Map<String, Object> rawResult = document.getData();

                            if (rawResult == null) {
                                Log.d(TAG, FirestoreError.NO_SUCH_DOCUMENT.toString());
                                callback.onFailure(FirestoreError.NO_SUCH_DOCUMENT);
                                return;
                            }

                            Map<String, Object> result = new HashMap<>();

                            if (fieldsToSelect != null) {

                                for (String key : rawResult.keySet()) {
                                    if (fieldsToSelect.contains(key)) {
                                        result.put(key, rawResult.get(key));
                                    }
                                }

                            } else {
                                result = rawResult;

                            }

                            result.put("id", document.getId());

                            Log.d(TAG, "DocumentSnapshot values: " + result);
                            callback.onSuccess(result);
                            return;

                        }

                        Log.d(TAG, FirestoreError.NO_SUCH_DOCUMENT.toString());
                        callback.onFailure(FirestoreError.NO_SUCH_DOCUMENT);

                    } else {
                        Log.w(TAG, FirestoreError.ERROR_GETTING_DOCUMENT.toString(), task.getException());
                        callback.onFailure(FirestoreError.ERROR_GETTING_DOCUMENT);
                    }
                });
    }

    @Override
    public void selectDocumentsByField(String collection, ArrayList<String> fieldsToSelect, String whereField, Object value, final FirestoreOnMultipleQueryCallback callback) {
        // selects the values of the selected fields 'fieldsToSelect'
        // (pass null to select all fields)
        // from the given 'collection', where the field 'whereField' (pass null to select all the documents)
        // matches the given parameter 'value'.
        // IMPORTANT: if one of the elements of 'fieldsToSelect' is not an actual field of
        // the 'collection', it just won't be added in the resulting hashMap, but it won't
        // produce any errors.

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query query;

        if (whereField == null) {
            query = db.collection(collection);
        } else {
            query = db.collection(collection).whereEqualTo(whereField, value);
        }

        query.get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        List<Map<String, Object>> result = new ArrayList<>();

                        for (QueryDocumentSnapshot document : task.getResult()) {

                            Map<String, Object> rawDocumentMap = document.getData();
                            Map<String, Object> documentMap = new HashMap<>();

                            if (fieldsToSelect != null) {

                                for (String key : rawDocumentMap.keySet()) {
                                    if (fieldsToSelect.contains(key)) {
                                        documentMap.put(key, rawDocumentMap.get(key));
                                    }
                                }

                            } else {
                                documentMap = rawDocumentMap;

                            }

                            documentMap.put("id", document.getId());
                            result.add(documentMap);
                        }

                        Log.d(TAG, "DocumentSnapshot values: " + result);
                        callback.onSuccess(result);

                    } else {

                        Log.w(TAG, FirestoreError.ERROR_GETTING_DOCUMENTS.toString(), task.getException());
                        callback.onFailure(FirestoreError.ERROR_GETTING_DOCUMENTS);
                    }
                });
    }


}