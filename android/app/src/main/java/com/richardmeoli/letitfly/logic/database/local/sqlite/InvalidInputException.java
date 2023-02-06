package com.richardmeoli.letitfly.logic.database.local.sqlite;

public class InvalidInputException extends Exception {

    /*
     *  InvalidInputException is a checked exception that is thrown in case the parameters
     *  passed to the constructor of classes such as Routine or Position are invalid.
     */

    public InvalidInputException(String errorMessage) {
        super(errorMessage);
    }

}