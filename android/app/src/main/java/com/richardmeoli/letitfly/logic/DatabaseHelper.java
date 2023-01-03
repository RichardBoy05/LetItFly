package com.richardmeoli.letitfly.logic;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import com.richardmeoli.letitfly.ui.main.MainActivity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    /*
     * - Helper class that handles all the databases values and methods
     *
     * - A lot of static fields/methods because we share a unique instance
     * of the database through the whole application (so it's class related not object related)
     *
     * - Singleton Pattern implementation to prevent this class from being instatiated more than once
     *
     */

    // static class instance

    private static DatabaseHelper instance;

    // database general values

    public static final String DATABASE_NAME = "letitfly.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_ROUTINES = "routines";
    public static final String TABLE_STATS = "stats";
    public static final String R_S_P_COLUMN_ID = "id";

    // minimum and maximum lengths

    public static final int ROUTINE_NAME_MIN_LENGTH = 3;
    public static final int ROUTINE_NAME_MAX_LENGTH = 25;
    public static final int USERNAME_MIN_LENGTH = 3;
    public static final int USERNAME_MAX_LENGTH = 15;
    public static final int R_NOTES_MAX_LENGTH = 100;
    public static final int P_NOTES_MAX_LENGTH = 15;
    public static final int S_OUTCOME_MAX_LENGTH = 5000;
    public static final int SMALLINT_MAX_VALUE = 32767;

    // routine table values

    public static final String R_COLUMN_NAME = "name";
    public static final String R_COLUMN_AUTHOR = "author";
    public static final String R_COLUMN_COLOR = "color";
    public static final String R_COLUMN_UUID = "uuid";
    public static final String R_COLUMN_TIME = "time";
    public static final String R_COLUMN_IS_PUBLIC = "is_public";
    public static final String R_COLUMN_NOTES = "notes";
    public static final String[] R_COLUMNS = {R_COLUMN_NAME, R_COLUMN_AUTHOR, R_COLUMN_COLOR,
            R_COLUMN_UUID, R_COLUMN_TIME, R_COLUMN_IS_PUBLIC, R_COLUMN_NOTES}; // id excluded

    private static final String R_CREATION_STATEMENT = "CREATE TABLE '" + TABLE_ROUTINES + "' (\n" +
            "\t'" + R_S_P_COLUMN_ID + "' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
            "\t'" + R_COLUMN_NAME + "' VARCHAR(" + ROUTINE_NAME_MAX_LENGTH + ") UNIQUE NOT NULL,\n" +
            "\t'" + R_COLUMN_AUTHOR + "' VARCHAR(" + USERNAME_MAX_LENGTH + ") NOT NULL,\n" +
            "\t'" + R_COLUMN_COLOR + "' CHAR(7) NOT NULL,\n" +
            "\t'" + R_COLUMN_UUID + "' VARCHAR(36) UNIQUE NOT NULL,\n" +
            "\t'" + R_COLUMN_TIME + "' SMALLINT,\n" +
            "\t'" + R_COLUMN_IS_PUBLIC + "' BOOLEAN NOT NULL,\n" +
            "\t'" + R_COLUMN_NOTES + "' VARCHAR(" + R_NOTES_MAX_LENGTH + ")" +
            ");";

    // stats table values

    public static final String S_COLUMN_DATE = "date";
    public static final String S_COLUMN_ROUTINE = "routine";
    public static final String S_COLUMN_REPS = "reps";
    public static final String S_COLUMN_OUTCOME = "outcome";
    public static final String[] S_COLUMNS = {S_COLUMN_DATE, S_COLUMN_ROUTINE,
            S_COLUMN_REPS, S_COLUMN_OUTCOME}; // id excluded

    private static final String S_CREATION_STATEMENT = "CREATE TABLE '" + TABLE_STATS +  "'(\n" +
            "\t'" + R_S_P_COLUMN_ID + "' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
            "\t'" + S_COLUMN_DATE + "' DATETIME UNIQUE NOT NULL,\n" +
            "\t'" + S_COLUMN_ROUTINE + "' VARCHAR(" + ROUTINE_NAME_MAX_LENGTH + ") NOT NULL,\n" +
            "\t'" + S_COLUMN_REPS + "' TINYINT NOT NULL,\n" +
            "\t'" + S_COLUMN_OUTCOME + "' VARCHAR(" + S_OUTCOME_MAX_LENGTH + ") NOT NULL" +
            ");";

    // position [nameRoutine] table values -> the name is not constant,
    // but depends on the routine associated to that table

    public static final String P_COLUMN_X_POS = "x_pos";
    public static final String P_COLUMN_Y_POS = "y_pos";
    public static final String P_COLUMN_SHOTS = "shots";
    public static final String P_COLUMN_PTS_PER_SHOT = "pts_per_shot";
    public static final String P_COLUMN_PTS_PER_LAST_SHOT = "pts_per_last_shot";
    public static final String P_COLUMN_NOTES = "notes";
    public static final String[] P_COLUMNS = {P_COLUMN_X_POS, P_COLUMN_Y_POS, P_COLUMN_SHOTS,
            P_COLUMN_PTS_PER_SHOT, P_COLUMN_PTS_PER_LAST_SHOT, P_COLUMN_NOTES}; // id excluded

    // cannot create a universal creation statement for this table because
    // its name is variable, so use the method "getPositionTableCreationStatement" (below)

    private DatabaseHelper(@Nullable Context context) {  // private so it's only accessible within the class
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
        // code executed at the first creation of the database
        
        db.execSQL(R_CREATION_STATEMENT);
        db.execSQL(S_CREATION_STATEMENT);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // TODO: consider implementing its functionalities in the future
    }

    private static ContentValues buildContentValues(String[] columns, ArrayList<Object> values){

        ContentValues record = new ContentValues(columns.length);

        int index = 0;
        for (String key: columns){

            if (values.get(index) instanceof Integer){
                record.put(key, (int)(values.get(index)));
            } else if (values.get(index) instanceof Boolean){
                record.put(key, (boolean) (values.get(index)));
            } else {
                record.put(key, (String) (values.get(index)));
            }

            index ++;
        }

        return record;
    }

    public static boolean insertRecord(String table, ArrayList<Object> values){
        // code to insert a new record (row) into a specific table of the database

        String[] columns;

        switch (table){ // check if the sizes match the columns

            case TABLE_ROUTINES:
                if (values.size() != R_COLUMNS.length) {
                    return false;
                }
                columns = R_COLUMNS;
                break;

            case TABLE_STATS:
                if (values.size() != S_COLUMNS.length) {
                    return false;
                }
                columns = S_COLUMNS;
                break;

            default:
                if (values.size() != P_COLUMNS.length) {
                    return false;
                }
                columns = P_COLUMNS;
                break;
        }

        ContentValues record = buildContentValues(columns, values);

        return MainActivity.getDbHelper().insert(table, null, record) != -1;
    }

//    public static boolean deleteRecordById(String table, int id){
//        // delete a record (row) where column id matches the given parameter "id"
//
//        return MainActivity.getDbHelper().delete(table, R_S_P_COLUMN_ID + " LIKE ?", new String[]{Integer.toString(id)}) != 0;
//
//    }

    public static boolean deleteRecord(String table, String parameter, Object value){
        // delete a record (row) where column id matches the given parameter "id"

        return MainActivity.getDbHelper().delete(table, parameter + " LIKE ?", new String[]{value.toString()}) != 0;

    }

//    public static boolean deleteRoutinesRecordByName(String name){
//        // delete a record (row) where column name matches the given parameter "name"
//
//        return MainActivity.getDbHelper().delete(TABLE_ROUTINES, R_COLUMN_NAME + " LIKE ?", new String[]{name}) != 0;
//
//    }
//
//    public static boolean deleteRoutinesRecordByUuid(String uuid){
//        // delete a record (row) where column uuid matches the given parameter "uuid"
//
//        return MainActivity.getDbHelper().delete(TABLE_ROUTINES, R_COLUMN_UUID + " LIKE ?", new String[]{uuid}) != 0;
//
//    }
//
//    public static boolean deleteStatsRecordByDate(String date){
//        // delete a record (row) where column date matches the given parameter "date"
//
//        return MainActivity.getDbHelper().delete(TABLE_STATS, S_COLUMN_DATE + " LIKE ?", new String[]{date}) != 0;
//
//    }
//
//    public static boolean updateRecordById(String table, String[] columns, ArrayList<Object> newValues, int id){
//        // update a record (row) where column id matches the given parameter "id"
//
//        if (columns.length != newValues.size() || Arrays.asList(columns).contains(R_S_P_COLUMN_ID)){
//            return false;
//        }
//
//        return MainActivity.getDbHelper().update(table, buildContentValues(columns, newValues),
//                R_S_P_COLUMN_ID + " LIKE ?", new String[]{Integer.toString(id)}) != 0;
//
//    }

    public static boolean updateRecord(String table, String[] columns, ArrayList<Object> newValues, String parameter, Object value){
        // update a record (row) where column id matches the given parameter "id"

        if (columns.length != newValues.size() || Arrays.asList(columns).contains(R_S_P_COLUMN_ID)){
            return false;
        }

        return MainActivity.getDbHelper().update(table, buildContentValues(columns, newValues),
                parameter + " LIKE ?", new String[]{value.toString()}) != 0;

    }

//    public static boolean updateRoutinesRecordByName(String name, String[] columns, ArrayList<Object> newValues){
//        // update a record (row) where column name matches the given parameter "name"
//
//        if (columns.length != newValues.size() || Arrays.asList(columns).contains(R_S_P_COLUMN_ID)){
//            return false;
//        }
//
//        return MainActivity.getDbHelper().update(TABLE_ROUTINES, buildContentValues(columns, newValues),
//                R_COLUMN_NAME + " LIKE ?", new String[]{name}) != 0;
//
//    }
//
//    public static boolean updateRoutinesRecordByUuid(String uuid, String[] columns, ArrayList<Object> newValues){
//        // update a record (row) where column uuid matches the given parameter "uuid"
//
//        if (columns.length != newValues.size() || Arrays.asList(columns).contains(R_S_P_COLUMN_ID)){
//            return false;
//        }
//
//        return MainActivity.getDbHelper().update(TABLE_ROUTINES, buildContentValues(columns, newValues),
//                R_COLUMN_UUID + " LIKE ?", new String[]{uuid}) != 0;
//
//    }
//
//    public static boolean updateStatsRecordByDate(String date, String[] columns, ArrayList<Object> newValues){
//        // update a record (row) where column date matches the given parameter "date"
//
//        if (columns.length != newValues.size() || Arrays.asList(columns).contains(R_S_P_COLUMN_ID)){
//            return false;
//        }
//
//        return MainActivity.getDbHelper().update(TABLE_STATS, buildContentValues(columns, newValues),
//                S_COLUMN_DATE + " LIKE ?", new String[]{date}) != 0;
//
//    }



    public static String getPositionsTableCreationStatement(String routineName){
        // get the creation statement for the positions table

        return "CREATE TABLE '" + routineName + "' (\n" +
                "\t'" + R_S_P_COLUMN_ID + "' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                "\t'" + P_COLUMN_X_POS + "' MEDIUMINT NOT NULL,\n" +
                "\t'" + P_COLUMN_Y_POS + "' MEDIUMINT NOT NULL,\n" +
                "\t'" + P_COLUMN_SHOTS + "' SMALLINT NOT NULL,\n" +
                "\t'" + P_COLUMN_PTS_PER_SHOT + "' SMALLINT,\n" +
                "\t'" + P_COLUMN_PTS_PER_LAST_SHOT + "' SMALLINT,\n" +
                "\t'" + P_COLUMN_NOTES + "' VARCHAR(" + P_NOTES_MAX_LENGTH + ")" +
                ");";
    }

}
