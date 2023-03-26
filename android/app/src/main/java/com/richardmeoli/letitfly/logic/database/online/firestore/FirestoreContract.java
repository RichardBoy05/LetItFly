package com.richardmeoli.letitfly.logic.database.online.firestore;

import com.richardmeoli.letitfly.logic.database.online.callbacks.FirestoreOnTransactionCallback;
import com.richardmeoli.letitfly.logic.database.online.callbacks.FirestoreOnSingleQueryCallback;
import com.richardmeoli.letitfly.logic.database.online.callbacks.FirestoreDocumentExistsCallback;
import com.richardmeoli.letitfly.logic.database.online.callbacks.FirestoreOnMultipleQueryCallback;

public interface FirestoreContract extends FirestoreAttributes {

    String TAG = "Firestore";

    //--------------- Database operations ---------------//

    void addDocument(String collection, String id, Object[] values, final FirestoreOnTransactionCallback callback);
    void deleteDocument(String collection, String id, final FirestoreOnTransactionCallback callback);
    void updateDocument(String collection, String[] fieldsToUpdate, Object[] values, String id, final FirestoreOnTransactionCallback callback);
    void selectDocumentById(String collection, String[] fieldsToSelect, String id, final FirestoreOnSingleQueryCallback callback);
    void selectDocumentsByField(String collection, String[] fieldsToSelect, String whereField, Object value, final FirestoreOnMultipleQueryCallback callback);

    //-------------------- Utilities --------------------//

    boolean areFieldsInvalid(String collection, String[] fields);
    void checkIfDocumentExists(String collection, String id, final FirestoreDocumentExistsCallback callback);
    void waitForPendingWrites();

}