package com.richardmeoli.letitfly.logic.database;

public interface RoutinesTable{

    String ROUTINES_TABLE = "routines";

    // columns

    String R_COLUMN_NAME = "name";
    String R_COLUMN_AUTHOR = "author";
    String R_COLUMN_COLOR = "color";
    String R_COLUMN_UUID = "uuid";
    String R_COLUMN_TIME = "time";
    String R_COLUMN_IS_PUBLIC = "is_public";
    String R_COLUMN_NOTES = "notes";

    String[] R_COLUMNS = {R_COLUMN_NAME, R_COLUMN_AUTHOR, R_COLUMN_COLOR,
            R_COLUMN_UUID, R_COLUMN_TIME, R_COLUMN_IS_PUBLIC, R_COLUMN_NOTES};
    String[] R_COLUMNS_ID_INCLUDED = {"id", R_COLUMN_NAME, R_COLUMN_AUTHOR,
            R_COLUMN_COLOR, R_COLUMN_UUID, R_COLUMN_TIME, R_COLUMN_IS_PUBLIC, R_COLUMN_NOTES};

    // values

    int R_NAME_MIN_LENGTH = 3;
    int R_NAME_MAX_LENGTH = 25;
    int R_USERNAME_MIN_LENGTH = 3;
    int R_USERNAME_MAX_LENGTH = 15;
    int R_NOTES_MAX_LENGTH = 100;
    int R_TIME_MAX_VALUE = 32767;

}