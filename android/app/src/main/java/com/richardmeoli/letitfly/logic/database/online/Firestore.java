package com.richardmeoli.letitfly.logic.database.online;

import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Firestore implements FirestoreContract {

    private static Firestore instance;
    private static final String TAG = "Firestore";

    private Firestore() {
    }

    public static Firestore getInstance() {

        if (instance == null) {
            instance = new Firestore();
        }

        return instance;
    }


    @Override
    public boolean storeDocument(String collection, String uuid, Object[] values) {

        String[] keys;

        switch (collection) {

            case ROUTINES_COLLECTION:
                keys = R_FIELDS;
                break;

            case POSITIONS_COLLECTION:
                keys = P_FIELDS;
                break;

            default:
                return false;
        }

        if (keys.length != values.length) {
            return false;
        }

        Map<String, Object> document = new HashMap<>();
        int index = 0;

        for (String key : keys) {
            document.put(key, values[index]);
            index++;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Task<Void> task = db.collection(collection).document(uuid)
                .set(document)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "DocumentSnapshot added with ID: " + uuid);
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error adding document", e);
                });

        return task.isSuccessful();

    }

    @Override
    public boolean deleteDocument(String collection, String uuid) {

        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        Task<Void> task =  fs.collection("collections").document(uuid).delete()
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Collection successfully deleted!");
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error deleting collection", e);
                });

        return task.isSuccessful();

    }
}
