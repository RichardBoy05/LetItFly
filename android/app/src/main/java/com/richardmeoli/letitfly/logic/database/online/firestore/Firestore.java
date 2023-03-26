package com.richardmeoli.letitfly.logic.database.online.firestore;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.richardmeoli.letitfly.logic.database.online.callbacks.FirestoreOnSingleQueryCallback;
import com.richardmeoli.letitfly.logic.database.online.callbacks.FirestoreOnTransactionCallback;
import com.richardmeoli.letitfly.logic.database.online.callbacks.FirestoreDocumentExistsCallback;
import com.richardmeoli.letitfly.logic.database.online.callbacks.FirestoreOnMultipleQueryCallback;

import java.util.Map;
import java.util.List;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;

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

    //--------------- Database operations ---------------//

    @Override
    public void addDocument(@NonNull String collection, String id, @NonNull Object[] values, final FirestoreOnTransactionCallback callback) {
        // Store a document to the Firestore database with the specified "id" and values.
        // Pass a null value for the "id" to allow Firestore to automatically generate an ID.

        String[] keys;

        switch (collection) {

            case ROUTINES_COLLECTION:
                keys = R_FIELDS;
                break;

            case POSITIONS_COLLECTION:
                keys = P_FIELDS;
                break;

            case USERS_COLLECTION:
                keys = U_FIELDS;
                break;

            default: // non-existent collection
                callback.onFailure(FirestoreError.NO_SUCH_COLLECTION);
                return;
        }

        if (keys.length != values.length) { // keys and values must have the same length
            callback.onFailure(FirestoreError.KEYS_VALUES_MISMATCH);
            return;
        }

        checkIfDocumentExists(collection, id, exists -> {

            if (exists) { // checks if document already exists
                callback.onFailure(FirestoreError.DOCUMENT_ALREADY_EXISTS);
                return;
            }

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Map<String, Object> document = new HashMap<>();
            int index = 0;

            for (String key : keys) {
                document.put(key, values[index]);
                index++;
            }

            if (id == null) { // uploads Document with Firestore generated ID

                db.collection(collection).add(document)
                        .addOnSuccessListener(documentReference -> {
                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            callback.onSuccess();
                        })
                        .addOnFailureListener(e -> {
                            Log.e(TAG, FirestoreError.ERROR_WRITING_DOCUMENT.toString(), e);
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
                            Log.e(TAG, FirestoreError.ERROR_WRITING_DOCUMENT.toString(), e);
                            callback.onFailure(FirestoreError.ERROR_WRITING_DOCUMENT);
                        });

            }

        });

    }

    public void deleteDocument(@NonNull String collection, @NonNull String id, final FirestoreOnTransactionCallback callback) {
        // Remove a document from the Firestore database using its unique "id".

        checkIfDocumentExists(collection, id, exists -> {
            if (!exists) {
                callback.onFailure(FirestoreError.NO_SUCH_DOCUMENT);
                return;
            }

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection(collection).document(id)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        Log.d(TAG, "DocumentSnapshot with ID " + id + " successfully deleted!");
                        callback.onSuccess();
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Error deleting document", e);
                        callback.onFailure(FirestoreError.ERROR_DELETING_DOCUMENT);
                    });
        });

    }

    @Override
    public void updateDocument(@NonNull String collection, @NonNull String[] fieldsToUpdate, @NonNull Object[] values, @NonNull String id, FirestoreOnTransactionCallback callback) {
        // Update a document in the Firestore database with new field-value pairs using its unique 'id'.

        if (fieldsToUpdate.length != values.length) { // keys and values must have the same length
            callback.onFailure(FirestoreError.KEYS_VALUES_MISMATCH);
            return;
        }

        if (areFieldsInvalid(collection, fieldsToUpdate)) {
            callback.onFailure(FirestoreError.INVALID_FIELDS);
            return;
        }

        checkIfDocumentExists(collection, id, exists ->

        {
            if (!exists) {
                callback.onFailure(FirestoreError.NO_SUCH_DOCUMENT);
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
                        Log.d(TAG, "DocumentSnapshot with ID " + id + " successfully updated!");
                        callback.onSuccess();
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, FirestoreError.ERROR_UPDATING_DOCUMENT.toString(), e);
                        callback.onFailure(FirestoreError.ERROR_UPDATING_DOCUMENT);
                    });
        });


    }

    @Override
    public void selectDocumentById(@NonNull String collection, String[] fieldsToSelect, @NonNull String id, final FirestoreOnSingleQueryCallback callback) {
        // Retrieve selected values of fields 'fieldsToSelect' from the specified 'collection',
        // where the 'id' matches the given parameter. Pass a null value for 'fieldsToSelect'
        // to select all fields.

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if (areFieldsInvalid(collection, fieldsToSelect)) {
            callback.onFailure(FirestoreError.INVALID_FIELDS);
            return;
        }

        db.collection(collection).document(id)
                .get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();

                        if (!document.exists()) { // non-existent document
                            callback.onFailure(FirestoreError.NO_SUCH_DOCUMENT);
                            return;
                        }

                        Map<String, Object> rawResult = document.getData();

                        if (rawResult == null) { // prevent NullPointerExcpetion
                            Log.d(TAG, FirestoreError.NO_SUCH_DOCUMENT.toString());
                            callback.onFailure(FirestoreError.NO_SUCH_DOCUMENT);
                            return;
                        }

                        Map<String, Object> result = new HashMap<>();

                        if (fieldsToSelect != null) {

                            for (String key : rawResult.keySet()) {
                                if (Arrays.asList(fieldsToSelect).contains(key)) {
                                    result.put(key, rawResult.get(key));
                                }
                            }

                        } else {
                            result = rawResult;
                        }

                        result.put("id", document.getId());

                        Log.d(TAG, "DocumentSnapshot values: " + result);
                        callback.onSuccess(result);


                    } else {
                        Log.e(TAG, FirestoreError.ERROR_GETTING_DOCUMENT.toString(), task.getException());
                        callback.onFailure(FirestoreError.ERROR_GETTING_DOCUMENT);
                    }
                });

    }

    @Override
    public void selectDocumentsByField(@NonNull String collection, String[] fieldsToSelect, String whereField, Object value, final FirestoreOnMultipleQueryCallback callback) {
        // Retrieve selected values of fields 'fieldsToSelect' from the specified 'collection',
        // where the value of field 'whereField' matches the given parameter 'value'.
        // Pass a null value for 'fieldsToSelect' to select all fields, or for 'whereField'
        // to select from all documents.

        if (areFieldsInvalid(collection, fieldsToSelect)) {
            callback.onFailure(FirestoreError.INVALID_FIELDS);
            return;
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

                        List<Map<String, Object>> result = new ArrayList<>();

                        for (QueryDocumentSnapshot document : task.getResult()) {

                            // QueryDocumentSnapshot instances are guaranteed to exist,
                            // so checking for document existence is unnecessary here.

                            Map<String, Object> rawDocumentMap = document.getData();
                            Map<String, Object> documentMap = new HashMap<>();

                            if (fieldsToSelect != null) {

                                for (String key : rawDocumentMap.keySet()) {
                                    if (Arrays.asList(fieldsToSelect).contains(key)) {
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

                        Log.e(TAG, FirestoreError.ERROR_GETTING_DOCUMENTS.toString(), task.getException());
                        callback.onFailure(FirestoreError.ERROR_GETTING_DOCUMENTS);
                    }
                });
    }

    //-------------------- Utilities --------------------//

    @Override
    public void checkIfDocumentExists(@NonNull String collection, String id, FirestoreDocumentExistsCallback callback) {
        // Determine if a document with the given 'id' exists in the specified 'collection' in Firestore.

        if (!collection.equals(USERS_COLLECTION)) {
            // Note: The only collection that must be checked for id uniqueness is
            // 'users'. Other collections use UUIDs, which makes them already unique.
            callback.onDocumentExists(false);
            return;
        }

        if (id == null) { // if 'id' is null, Firestore will automatically generate a unique identifier for it.
            callback.onDocumentExists(false);
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(collection).document(id)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    Log.d(TAG, "Found document '" + documentSnapshot.getId() + "'");
                    callback.onDocumentExists(documentSnapshot.exists());
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, FirestoreError.NETWORK_ERROR.toString(), e);
                    callback.onDocumentExists(true);


                });

    }

    public boolean areFieldsInvalid(String collection, String[] fields) {
        // Check if the 'fields' are invalid for the specified 'collection'.

        switch (collection) {

            case ROUTINES_COLLECTION:
                if (fields == null) { // null refers to all the fields
                    return false;
                }

                for (String field : fields) {
                    if (!Arrays.asList(R_FIELDS).contains(field)) {
                        return true;
                    }
                }
                return false;

            case POSITIONS_COLLECTION:
                if (fields == null) { // null refers to all the fields
                    return false;
                }

                for (String field : fields) {
                    if (!Arrays.asList(P_FIELDS).contains(field)) {
                        return true;
                    }
                }
                return false;

            case USERS_COLLECTION:
                if (fields == null) { // null refers to all the fields
                    return false;
                }

                for (String field : fields) {
                    if (!Arrays.asList(U_FIELDS).contains(field)) {
                        return true;
                    }
                }
                return false;

            default:
                return true;
        }

    }

    public void waitForPendingWrites() {
        // Waits for all pending writes to be uploaded to Firestore and logs a message when there are no more pending writes.

        FirebaseFirestore.getInstance().waitForPendingWrites().addOnSuccessListener(aVoid -> {
            Log.d(TAG, "All pending writes have been uploaded to Firestore.");
        }).addOnFailureListener(e -> {
            Log.e(TAG, "Error while waiting for pending writes to be uploaded to Firestore: " + e.getMessage());
        });
    }

}