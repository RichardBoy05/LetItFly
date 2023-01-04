package com.richardmeoli.letitfly.logic;

public interface DatabaseContract {

    // database general attributes

    String DATABASE_NAME = "letitfly.db";
    int DATABASE_VERSION = 1;
    String ROUTINES_TABLE = "routines";
    String STATS_TABLE = "stats";
    // positions tables don't have a unique name
    String R_S_P_COLUMN_ID = "id";

    // database max and min values for specific parameters

    int ROUTINE_NAME_MIN_LENGTH = 3;
    int ROUTINE_NAME_MAX_LENGTH = 25;
    int USERNAME_MIN_LENGTH = 3;
    int USERNAME_MAX_LENGTH = 15;
    int R_NOTES_MAX_LENGTH = 100;
    int P_NOTES_MAX_LENGTH = 15;
    int S_OUTCOME_MAX_LENGTH = 5000;
    int SMALLINT_MAX_VALUE = 32767;

    // routines table attributes

    String R_COLUMN_NAME = "name";
    String R_COLUMN_AUTHOR = "author";
    String R_COLUMN_COLOR = "color";
    String R_COLUMN_UUID = "uuid";
    String R_COLUMN_TIME = "time";
    String R_COLUMN_IS_PUBLIC = "is_public";
    String R_COLUMN_NOTES = "notes";
    String[] R_COLUMNS = {R_COLUMN_NAME, R_COLUMN_AUTHOR, R_COLUMN_COLOR,
            R_COLUMN_UUID, R_COLUMN_TIME, R_COLUMN_IS_PUBLIC, R_COLUMN_NOTES}; // id excluded
    String[] R_COLUMNS_ID_INCLUDED = {R_S_P_COLUMN_ID, R_COLUMN_NAME, R_COLUMN_AUTHOR,
            R_COLUMN_COLOR, R_COLUMN_UUID, R_COLUMN_TIME, R_COLUMN_IS_PUBLIC, R_COLUMN_NOTES}; // id included

    String R_CREATION_STATEMENT = "CREATE TABLE '" + ROUTINES_TABLE + "' (\n" +
            "\t'" + R_S_P_COLUMN_ID + "' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
            "\t'" + R_COLUMN_NAME + "' VARCHAR(" + ROUTINE_NAME_MAX_LENGTH + ") UNIQUE NOT NULL,\n" +
            "\t'" + R_COLUMN_AUTHOR + "' VARCHAR(" + USERNAME_MAX_LENGTH + ") NOT NULL,\n" +
            "\t'" + R_COLUMN_COLOR + "' CHAR(7) NOT NULL,\n" +
            "\t'" + R_COLUMN_UUID + "' VARCHAR(36) UNIQUE NOT NULL,\n" +
            "\t'" + R_COLUMN_TIME + "' SMALLINT,\n" +
            "\t'" + R_COLUMN_IS_PUBLIC + "' BOOLEAN NOT NULL,\n" +
            "\t'" + R_COLUMN_NOTES + "' VARCHAR(" + R_NOTES_MAX_LENGTH + ")" +
            ");";

    // stats table attributes

    String S_COLUMN_DATE = "date";
    String S_COLUMN_ROUTINE = "routine";
    String S_COLUMN_REPS = "reps";
    String S_COLUMN_OUTCOME = "outcome";
    String[] S_COLUMNS = {S_COLUMN_DATE, S_COLUMN_ROUTINE,
            S_COLUMN_REPS, S_COLUMN_OUTCOME}; // id excluded
    String[] S_COLUMNS_ID_INCLUDED = {R_S_P_COLUMN_ID, S_COLUMN_DATE,
            S_COLUMN_ROUTINE, S_COLUMN_REPS, S_COLUMN_OUTCOME}; // id included

    String S_CREATION_STATEMENT = "CREATE TABLE '" + STATS_TABLE + "'(\n" +
            "\t'" + R_S_P_COLUMN_ID + "' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
            "\t'" + S_COLUMN_DATE + "' DATETIME UNIQUE NOT NULL,\n" +
            "\t'" + S_COLUMN_ROUTINE + "' VARCHAR(" + ROUTINE_NAME_MAX_LENGTH + ") NOT NULL,\n" +
            "\t'" + S_COLUMN_REPS + "' TINYINT NOT NULL,\n" +
            "\t'" + S_COLUMN_OUTCOME + "' VARCHAR(" + S_OUTCOME_MAX_LENGTH + ") NOT NULL" +
            ");";

    // positions table attributes

    String P_COLUMN_X_POS = "x_pos";
    String P_COLUMN_Y_POS = "y_pos";
    String P_COLUMN_SHOTS = "shots";
    String P_COLUMN_PTS_PER_SHOT = "pts_per_shot";
    String P_COLUMN_PTS_PER_LAST_SHOT = "pts_per_last_shot";
    String P_COLUMN_NOTES = "notes";
    String[] P_COLUMNS = {P_COLUMN_X_POS, P_COLUMN_Y_POS, P_COLUMN_SHOTS,
            P_COLUMN_PTS_PER_SHOT, P_COLUMN_PTS_PER_LAST_SHOT, P_COLUMN_NOTES}; // id excluded
    String[] P_COLUMNS_ID_INCLUDED = {R_S_P_COLUMN_ID, P_COLUMN_X_POS, P_COLUMN_Y_POS,
            P_COLUMN_SHOTS, P_COLUMN_PTS_PER_SHOT, P_COLUMN_PTS_PER_LAST_SHOT, P_COLUMN_NOTES}; // id included

    // cannot create a universal creation statement for this table because
    // its name is variable, so use the method "getPositionTableCreationStatement" (below)
}

