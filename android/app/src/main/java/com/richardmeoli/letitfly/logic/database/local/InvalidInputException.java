package com.richardmeoli.letitfly.logic.database.local;

public class InvalidInputException extends Exception{

    public InvalidInputException(String errorMessage){super(errorMessage);}
    // checked exception which is thrown in case Routine,
    // Position or others constructor's parameters aren't valid

}
