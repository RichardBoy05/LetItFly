package com.richardmeoli.letitfly.logic;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    // static class instance

    private static DatabaseHelper instance;

    // database values

    public static final String DATABASE_NAME = "letitfly.db";
    public static final int DATABASE_VERSION = 1;

    // minimum and maximum lengths

    public static final int ROUTINE_NAME_MIN_LENGTH = 3;
    public static final int ROUTINE_NAME_MAX_LENGTH = 30;
    public static final int USERNAME_MAX_LENGTH = 20;
    public static final int USERNAME_MIN_LENGTH = 3;
    public static final int R_NOTES_MAX_LENGTH = 100;
    public static final int P_NOTES_MAX_LENGTH = 15;
    public static final int S_OUTCOME_MAX_LENGTH = 5000;

    // routine table values

    public static final String TABLE_ROUTINES = "routines";
    public static final String R_COLUMN_NAME = "name";
    public static final String R_COLUMN_AUTHOR = "author";
    public static final String R_COLUMN_COLOR = "color";
    public static final String R_COLUMN_UUID = "uuid";
    public static final String R_COLUMN_TIME = "time";
    public static final String R_COLUMN_IS_PUBLIC = "is_public";
    public static final String R_COLUMN_NOTES = "notes";
    private static final String R_CREATION_STATEMENT = "CREATE TABLE '" + TABLE_ROUTINES + "' (\n" +
            "\t'" + R_COLUMN_NAME + "' VARCHAR(" + ROUTINE_NAME_MAX_LENGTH + ") PRIMARY KEY NOT NULL,\n" +
            "\t'" + R_COLUMN_AUTHOR + "' VARCHAR(" + USERNAME_MAX_LENGTH + ") NOT NULL,\n" +
            "\t'" + R_COLUMN_COLOR + "' CHAR(7) NOT NULL,\n" +
            "\t'" + R_COLUMN_UUID + "' BINARY(16) UNIQUE NOT NULL,\n" +
            "\t'" + R_COLUMN_TIME + "' SMALLINT,\n" +
            "\t'" + R_COLUMN_IS_PUBLIC + "' BOOLEAN NOT NULL,\n" +
            "\t'" + R_COLUMN_NOTES + "' VARCHAR(" + R_NOTES_MAX_LENGTH + ")" +
            ");";

    // stats table values

    public static final String TABLE_STATS = "stats";
    public static final String S_COLUMN_ID = "id";
    public static final String S_COLUMN_DATE = "date";
    public static final String S_COLUMN_ROUTINE = "routine";
    public static final String S_COLUMN_REPS = "reps";
    public static final String S_COLUMN_OUTCOME = "outcome";
    private static final String S_CREATION_STATEMENT = "CREATE TABLE '" + TABLE_STATS +  "'(\n" +
            "\t'" + S_COLUMN_ID + "' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
            "\t'" + S_COLUMN_DATE + "' DATETIME UNIQUE NOT NULL,\n" +
            "\t'" + S_COLUMN_ROUTINE + "' VARCHAR(" + ROUTINE_NAME_MAX_LENGTH + ") NOT NULL,\n" +
            "\t'" + S_COLUMN_REPS + "' TINYINT NOT NULL DEFAULT 1,\n" +
            "\t'" + S_COLUMN_OUTCOME + "' VARCHAR(" + S_OUTCOME_MAX_LENGTH + ") NOT NULL" +
            ");";

    // position [nameRoutine] table values -> the name is not constant,
    // but depends on the routine associated to that table

    public static final String P_COLUMN_ID = "id";
    public static final String P_COLUMN_X_POS = "x_pos";
    public static final String P_COLUMN_Y_POS = "y_pos";
    public static final String P_COLUMN_SHOTS = "shots";
    public static final String P_COLUMN_PTS_PER_SHOT = "pts_per_shot";
    public static final String P_COLUMN_PTS_PER_LAST_SHOT = "pts_per_last_shot";
    public static final String P_COLUMN_NOTES = "notes";

    // cannot create a universal creation statement for this table because
    // its name is variable, so use the method "getPositionTableCreationStatement" (below)

    private DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DatabaseHelper getInstance(@Nullable Context context){
        if (instance == null){
            instance = new DatabaseHelper(context);
        }
            return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        
        db.execSQL(R_CREATION_STATEMENT);
        db.execSQL(S_CREATION_STATEMENT);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static String getPositionsTableCreationStatement(String routineName){

        return "CREATE TABLE '" + routineName + "' (\n" +
                "\t'" + P_COLUMN_ID + "' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                "\t'" + P_COLUMN_X_POS + "' MEDIUMINT NOT NULL,\n" +
                "\t'" + P_COLUMN_Y_POS + "' MEDIUMINT NOT NULL,\n" +
                "\t'" + P_COLUMN_SHOTS + "' SMALLINT NOT NULL,\n" +
                "\t'" + P_COLUMN_PTS_PER_SHOT + "' SMALLINT DEFAULT 1,\n" +
                "\t'" + P_COLUMN_PTS_PER_LAST_SHOT + "' SMALLINT DEFAULT 1,\n" +
                "\t'" + P_COLUMN_NOTES + "' VARCHAR(" + P_NOTES_MAX_LENGTH + ")" +
                ");";
    }
}
