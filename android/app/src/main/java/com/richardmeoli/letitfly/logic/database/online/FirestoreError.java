package com.richardmeoli.letitfly.logic.database.online;

import androidx.annotation.NonNull;

public enum FirestoreError {

    //---------------ERRORS---------------//

    NO_SUCH_COLLECTION("NO_SUCH_COLLECTION", "This collection does not exist!"),
    NO_SUCH_DOCUMENT("NO_SUCH_DOCUMENT", "This document does not exist!"),
    DOCUMENT_ALREADY_EXISTS("DOCUMENT_ALREADY_EXISTS", "This document already exists!"),
    KEYS_VALUES_MISMATCH("KEYS_VALUES_MISMATCH", "The desired values do not match the keys!"),
    ERROR_WRITING_DOCUMENT("ERROR_WRITING_DOCUMENT", "An unexpected error has occured while writing the document to the Firestore Database!"),
    ERROR_DELETING_DOCUMENT("ERROR_DELETING_DOCUMENT", "An unexpected error has occured while deleting the document from the Firestore Database!"),
    ERROR_DELETING_DOCUMENTS("ERROR_DELETING_DOCUMENTS", "An unexpected error has occured while deleting the documents from the Firestore Database!"),
    ERROR_UPDATING_DOCUMENT("ERROR_UPDATING_DOCUMENTS", "An unexpected error has occured while updating the documents of the Firestore Database!"),
    ERROR_UPDATING_DOCUMENTS("ERROR_UPDATING_DOCUMENTS", "An unexpected error has occured while updating the documents of the Firestore Database!"),
    ERROR_GETTING_DOCUMENT("ERROR_GETTING_DOCUMENT", "An unexpected error has occured while getting the document from the Firestore Database!"),
    ERROR_GETTING_DOCUMENTS("ERROR_GETTING_DOCUMENTS", "An unexpected error has occured while getting the documents from the Firestore Database!"),
    LOCAL_DB_ERROR("LOCAL_DB_ERROR", "An unexpected error has occured with the local Database!");

    //---------------ERRORS---------------//

    private final String name;
    private final String description;

    FirestoreError(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @NonNull
    @Override
    public String toString() {
        return name + ": " + description;
    }

}
