package com.richardmeoli.letitfly.logic.database.online;

import android.util.Log;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Firestore implements FirestoreContract {

    private static Firestore instance;
    private static final String TAG = "Firestore";

    private Firestore() {}

    public static Firestore getInstance() {

        if (instance == null){
            instance = new Firestore();
        }

        return instance;
    }


    @Override
    public void storeDocument(String uuid, String collection, Object[] values, final FirebaseCallback callback) {

        String[] keys;

        switch (collection){

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

        if (keys.length != values.length){
            callback.onFailure();
            return;
        }

        Map<String, Object> document = new HashMap<>();
        int index = 0;

        for (String key: keys){
            document.put(key, values[index]);
            index ++;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(collection).document(uuid.toString())
                .set(document)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "DocumentSnapshot added with ID: " + uuid);
                    callback.onSuccess();
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error adding document", e);
                    callback.onFailure();
                });

    }
}
