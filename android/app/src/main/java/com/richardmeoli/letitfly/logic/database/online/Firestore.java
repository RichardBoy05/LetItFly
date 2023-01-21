package com.richardmeoli.letitfly.logic.database.online;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

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
    public boolean storeDocument(String collection, String id, Object[] values) {
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
        AtomicBoolean result = new AtomicBoolean(false);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {


            db.collection(collection).document(id)
                    .set(document)
                    .addOnSuccessListener(aVoid -> {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        result2 = true;
                    })
                    .addOnFailureListener(e -> {
                        Log.w(TAG, "Error writing document", e);
                        result.set(false);
                    });

            handler.post(() -> {

                return null;

            });
        });



    }

    @Override
    public boolean deleteDocument(String collection, String id) {
        // deletes a Document from the Firestore Database by its "id";

        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        Task<Void> task = fs.collection("collections").document(id).delete()
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Collection successfully deleted!");
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error deleting collection", e);
                });

        return task.isSuccessful();

    }
}

