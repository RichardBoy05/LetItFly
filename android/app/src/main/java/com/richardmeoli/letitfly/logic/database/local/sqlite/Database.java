package com.richardmeoli.letitfly.logic.database.local.sqlite;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;


public class Database extends SQLiteOpenHelper implements DatabaseContract {

    /*
     * Helper class that handles all Sqlite database operations.
     * Implements the Singleton Pattern to ensure that the class is instantiated only once.
     */


    private static Database instance;
    private static SQLiteDatabase dbHelper;

    private static final String R_CREATION_STATEMENT = "CREATE TABLE IF NOT EXISTS '" + ROUTINES_TABLE + "' (\n" +
            "\t'" + R_COLUMN_ID + "' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
            "\t'" + R_COLUMN_NAME + "' VARCHAR(" + R_NAME_MAX_LENGTH + ") UNIQUE NOT NULL,\n" +
            "\t'" + R_COLUMN_AUTHOR + "' VARCHAR(" + R_AUTHOR_MAX_LENGTH + ") NOT NULL,\n" +
            "\t'" + R_COLUMN_COLOR + "' CHAR(7) NOT NULL,\n" +
            "\t'" + R_COLUMN_UUID + "' VARCHAR(36) UNIQUE NOT NULL,\n" +
            "\t'" + R_COLUMN_TIME + "' SMALLINT,\n" +
            "\t'" + R_COLUMN_IS_PUBLIC + "' BOOLEAN NOT NULL,\n" +
            "\t'" + R_COLUMN_NOTES + "' VARCHAR(" + R_NOTES_MAX_LENGTH + ")" +
            ");";

    private static final String S_CREATION_STATEMENT = "CREATE TABLE IF NOT EXISTS '" + STATS_TABLE + "'(\n" +
            "\t'" + S_COLUMN_ID + "' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
            "\t'" + S_COLUMN_ROUTINE + "' VARCHAR(" + R_NAME_MAX_LENGTH + ") NOT NULL,\n" +
            "\t'" + S_COLUMN_DATE + "' DATETIME UNIQUE NOT NULL,\n" +
            "\t'" + S_COLUMN_REPS + "' TINYINT NOT NULL,\n" +
            "\t'" + S_COLUMN_OUTCOME + "' VARCHAR(" + S_OUTCOME_MAX_LENGTH + ") NOT NULL,\n" +
            "\t FOREIGN KEY (" + S_COLUMN_ROUTINE + ") REFERENCES " + ROUTINES_TABLE + "(" + R_COLUMN_NAME + ") ON UPDATE CASCADE ON DELETE CASCADE" +
            ");";

    private static final String P_CREATION_STATEMENT = "CREATE TABLE IF NOT EXISTS '" + POSITIONS_TABLE + "' (\n" +
            "\t'" + P_COLUMN_ID + "' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
            "\t'" + P_COLUMN_ROUTINE + "' VARCHAR(" + R_NAME_MAX_LENGTH + ") NOT NULL,\n" +
            "\t'" + P_COLUMN_X_POS + "' MEDIUMINT NOT NULL,\n" +
            "\t'" + P_COLUMN_Y_POS + "' MEDIUMINT NOT NULL,\n" +
            "\t'" + P_COLUMN_IMG_WIDTH + "' MEDIUMINT NOT NULL,\n" +
            "\t'" + P_COLUMN_IMG_HEIGHT + "' MEDIUMINT NOT NULL,\n" +
            "\t'" + P_COLUMN_SHOTS + "' SMALLINT NOT NULL,\n" +
            "\t'" + P_COLUMN_PTS_PER_SHOT + "' SMALLINT NOT NULL,\n" +
            "\t'" + P_COLUMN_PTS_PER_LAST_SHOT + "' SMALLINT NOT NULL,\n" +
            "\t'" + P_COLUMN_NOTES + "' VARCHAR(" + P_NOTES_MAX_LENGTH + "),\n" +
            "\t FOREIGN KEY (" + P_COLUMN_ROUTINE + ") REFERENCES " + ROUTINES_TABLE + "(" + R_COLUMN_NAME + ") ON UPDATE CASCADE ON DELETE CASCADE" +
            ");";


    private Database(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static Database getInstance(@Nullable Context context) {
        if (instance == null) {
            instance = new Database(context);
            dbHelper = instance.getWritableDatabase();
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // code executed at the first creation of the database (tables creation)

        db.execSQL(R_CREATION_STATEMENT);
        db.execSQL(S_CREATION_STATEMENT);
        db.execSQL(P_CREATION_STATEMENT);

    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys = ON;");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // TODO: consider implementing its functionalities in the future
    }

    // database methods

    private ContentValues buildContentValues(@NonNull String[] columns, @NonNull Object[] values) {
        // Build a `ContentValues` object from the given column names and values.

        ContentValues record = new ContentValues(columns.length);

        int index = 0;
        for (String key : columns) {

            if (values[index] instanceof Integer) {
                record.put(key, (int) (values[index]));
            } else if (values[index] instanceof Boolean) {
                record.put(key, (boolean) (values[index]));
            } else if (values[index] instanceof String) {
                record.put(key, (String) (values[index]));
            } else {
                record.putNull(key);
            }

            index++;
        }

        return record;
    }

    @Override
    public boolean insertRecord(@NonNull String table, @NonNull Object[] values) {
        // Insert a new record (row) into a specified table
        // of the database based on the given 'values'.

        String[] columns;

        switch (table) { // columns and values must have the same length

            case ROUTINES_TABLE:
                if (values.length != R_COLUMNS.length) {
                    return false;
                }
                columns = R_COLUMNS;
                break;

            case STATS_TABLE:
                if (values.length != S_COLUMNS.length) {
                    return false;
                }
                columns = S_COLUMNS;
                break;

            case POSITIONS_TABLE:
                if (values.length != P_COLUMNS.length) {
                    return false;
                }
                columns = P_COLUMNS;
                break;

            default: // non-existent table
                return false;
        }

        ContentValues record = buildContentValues(columns, values);

        try {
            return dbHelper.insert(table, null, record) != -1;
            // returns true if the insert operation was successful, false otherwise.


        } catch (SQLiteException e) {
            Log.w(TAG, e.toString(), e);
            return false;
        }
    }

    @Override
    public boolean deleteRecords(String table, String whereColumn, Object value) {
        // deletes the records (row) of a 'table' where column specified as 'whereColumn'
        // (pass null to delete all rows)
        // matches the given parameter 'value'

        String whereClause = whereColumn == null ? null : whereColumn + " LIKE ?";
        String[] valuesClause = value == null ? null : new String[]{value.toString()};

        try {

            return dbHelper.delete(table, whereClause, valuesClause) != 0;
            // returns true if the insert operation was successful, false otherwise.

        } catch (SQLiteException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateRecords(String table, String whereColumn, Object value, String[] columnsToUpdate, Object[] newValues) {
        // updates the records (row) where the column 'whereColumn'
        // matches the given parameter 'value',
        // (pass null to update all rows)
        // replacing the old values with the 'newValues'
        // for the matching 'columnsToUpdate'

        if (columnsToUpdate.length != newValues.length || Arrays.asList(columnsToUpdate).contains(R_S_P_COLUMN_ID)) {
            // columns and values must have the same length

            return false;
        }

        String whereClause = whereColumn == null ? null : whereColumn + " LIKE ?";
        String[] valuesClause = value == null ? null : new String[]{value.toString()};

        try {

            return dbHelper.update(table,
                    buildContentValues(columnsToUpdate, newValues),
                    whereClause, valuesClause) != 0;

        } catch (SQLiteException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<List<Object>> selectRecords(String table,
                                            String[] columns,
                                            String whereColumn,
                                            Object value,
                                            String sortingColumn,
                                            Boolean ascendent) {
        // selects the values of the selected 'columns' (pass null to select from all columns)
        // of the given 'table', where the column 'whereColumn' matches the given
        // parameter 'value' (pass null to select all rows).
        // Results are sorted based on the parameter 'sortingColumn' (null if not needed),
        // ascendent if true else descendent (null if not needed).
        // Grouping and having caluses are not included
        // for better readability and because they are not useful in this application)

        String whereClause = whereColumn == null ? null : whereColumn + " = ?";
        String[] valuesClause = value == null ? null : new String[]{value.toString()};
        String sortOrder = null;

        if (sortingColumn != null && ascendent != null) {
            sortOrder = sortingColumn + (ascendent ? " ASC" : " DESC");
        }

        try (Cursor cursor = dbHelper.query(
                table,
                columns,
                whereClause,
                valuesClause,
                null,
                null,
                sortOrder)) {

            List<List<Object>> result = new ArrayList<>();

            if (columns == null) {

                switch (table) { // columns and values must have the same length

                    case ROUTINES_TABLE:
                        columns = R_COLUMNS_ID_INCLUDED;
                        break;

                    case STATS_TABLE:
                        columns = S_COLUMNS_ID_INCLUDED;
                        break;

                    case POSITIONS_TABLE:
                        columns = P_COLUMNS_ID_INCLUDED;
                        break;

                    default: // non-existent table
                        return null;
                }
            }

            while (cursor.moveToNext()) {

                List<Object> item = new ArrayList<>();
                for (String i : columns) {

                    switch (cursor.getType(cursor.getColumnIndexOrThrow(i))) {

                        case Cursor.FIELD_TYPE_INTEGER:
                            item.add(cursor.getInt(cursor.getColumnIndexOrThrow(i)));
                            break;
                        case Cursor.FIELD_TYPE_NULL:
                            item.add(null);
                            break;
                        case Cursor.FIELD_TYPE_STRING:
                            item.add(cursor.getString(cursor.getColumnIndexOrThrow(i)));
                            break;
                        case Cursor.FIELD_TYPE_BLOB:
                            item.add(cursor.getBlob(cursor.getColumnIndexOrThrow(i)));
                            break;
                        case Cursor.FIELD_TYPE_FLOAT:
                            item.add(cursor.getFloat(cursor.getColumnIndexOrThrow(i)));
                            break;
                    }

                }

                result.add(item);
            }

            return result;

        } catch (SQLiteException | IllegalArgumentException e) {
            e.printStackTrace();
            return null;

        }
    }

}
