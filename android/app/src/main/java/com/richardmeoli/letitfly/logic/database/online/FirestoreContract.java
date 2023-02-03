package com.richardmeoli.letitfly.logic.database.online;

import com.richardmeoli.letitfly.logic.database.online.callbacks.FirestoreDocumentExistsCallback;
import com.richardmeoli.letitfly.logic.database.online.callbacks.FirestoreOnTransactionCallback;
import com.richardmeoli.letitfly.logic.database.online.callbacks.FirestoreOnSingleQueryCallback;
import com.richardmeoli.letitfly.logic.database.online.callbacks.FirestoreOnMultipleQueryCallback;

import java.util.ArrayList;

public interface FirestoreContract extends FirestoreAttributes {

    String TAG = "Firestore";

    void storeDocument(String collection, String id, Object[] values, final FirestoreOnTransactionCallback callback);
    void checkIfDocumentExists(String collection, String id, final FirestoreDocumentExistsCallback callback);
    void deleteDocumentById(String collection, String id, final FirestoreOnTransactionCallback callback);
    void deleteDocumentsByField(String collection, String whereField, Object value, final FirestoreOnTransactionCallback callback);
    void updateDocumentById(String collection, String[] fieldsToUpdate, Object[] values, String id, final FirestoreOnTransactionCallback callback);
    void updateDocumentsByField(String collection, String[] fieldsToUpdate, Object[] values, String whereField, Object value, final FirestoreOnTransactionCallback callback);
    void selectDocumentById(String collection, ArrayList<String> fieldsToSelect, String id, final FirestoreOnSingleQueryCallback callback);
    void selectDocumentsByField(String collection, ArrayList<String> fieldsToSelect, String whereField, Object value, final FirestoreOnMultipleQueryCallback callback);

}
