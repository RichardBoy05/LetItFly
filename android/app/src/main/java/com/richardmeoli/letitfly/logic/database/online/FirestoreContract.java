package com.richardmeoli.letitfly.logic.database.online;

public interface FirestoreContract extends FirestoreAttributes {

    String TAG = "Firestore";

    void storeDocument(String collection, String id, Object[] value, final FirestoreOnTransactionCallback callback);
    void deleteDocument(String collection, String id, final FirestoreOnTransactionCallback callback);
    void selectDocumentById(String collection, String[] fieldsToSelect, String id, final FirestoreOnQueryCallback callback);
    void selectDocumentsByField(String collection, String[] fieldsToSelect, String whereField, Object value, final FirestoreOnQueryCallback callback);

}
