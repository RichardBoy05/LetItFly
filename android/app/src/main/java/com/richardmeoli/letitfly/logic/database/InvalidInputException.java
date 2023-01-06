package com.richardmeoli.letitfly.logic.database;

public class InvalidInputException extends Exception{

    public InvalidInputException(String errorMessage){super(errorMessage);}
    // checked exception which is thrown in case Routine
    // or Position constructor's parameters aren't valid

}
