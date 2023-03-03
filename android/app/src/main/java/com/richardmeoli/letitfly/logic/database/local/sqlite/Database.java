package com.richardmeoli.letitfly.logic.database.local.sqlite;

import android.database.SQLException;
import android.util.Log;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

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


    //------------------------ Tables ------------------------//

    // Routines Table

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

    // Positions Table

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

    // Stats Table

    private static final String S_CREATION_STATEMENT = "CREATE TABLE IF NOT EXISTS '" + STATS_TABLE + "'(\n" +
            "\t'" + S_COLUMN_ID + "' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
            "\t'" + S_COLUMN_ROUTINE + "' VARCHAR(" + R_NAME_MAX_LENGTH + ") NOT NULL,\n" +
            "\t'" + S_COLUMN_DATE + "' DATETIME UNIQUE NOT NULL,\n" +
            "\t'" + S_COLUMN_REPS + "' TINYINT NOT NULL,\n" +
            "\t'" + S_COLUMN_OUTCOME + "' VARCHAR(" + S_OUTCOME_MAX_LENGTH + ") NOT NULL,\n" +
            "\t FOREIGN KEY (" + S_COLUMN_ROUTINE + ") REFERENCES " + ROUTINES_TABLE + "(" + R_COLUMN_NAME + ") ON UPDATE CASCADE ON DELETE CASCADE" +
            ");";


    //-------------------- Database initialization --------------------//

    public static Database getInstance(@Nullable Context context) {
        // Singleton Pattern

        if (instance == null) {
            instance = new Database(context);
            dbHelper = instance.getWritableDatabase();
        }
        return instance;
    }

    private Database(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creating tables

        db.execSQL(R_CREATION_STATEMENT);
        db.execSQL(P_CREATION_STATEMENT);
        db.execSQL(S_CREATION_STATEMENT);

    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        // Configuration

        db.execSQL("PRAGMA foreign_keys = ON;");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // TODO: Implement this method only if changes to the database schema are expected in the future.
    }


    //---------------- Database methods -----------------//

    @Override
    public boolean insertRecord(@NonNull String table, @NonNull Object[] values, @NonNull Context context) {
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
        DbTransactionListener listener = new DbTransactionListener(context);

        try { // returns true if the insert operation was successful, false otherwise.

            dbHelper.beginTransactionWithListener(listener);

            if (dbHelper.insert(table, null, record) != -1) {
                dbHelper.setTransactionSuccessful();
                return true;
            }
            return false;

        } catch (SQLiteException e) {
            Log.e(TAG, e.toString(), e);
            return false;

        } finally {
            dbHelper.endTransaction();
        }
    }

    @Override
    public boolean deleteRecords(String table, String whereColumn, Object value, @NonNull Context context) {
        // Deletes rows from the 'table' where the value in the 'whereColumn'
        // column matches the specified 'value'.
        // Pass 'null' as the 'value' parameter to delete all rows in the table.

        String whereClause = whereColumn == null ? null : whereColumn + " LIKE ?";
        String[] valuesClause = value == null ? null : new String[]{value.toString()};

        DbTransactionListener listener = new DbTransactionListener(context);

        try { // returns true if the delete operation was successful, false otherwise.

            dbHelper.beginTransactionWithListener(listener);

            if (dbHelper.delete(table, whereClause, valuesClause) != 0) {

                dbHelper.setTransactionSuccessful();
                return true;
            }
            return false;

        } catch (SQLiteException e) {
            Log.e(TAG, e.toString(), e);
            return false;

        } finally {
            dbHelper.endTransaction();
        }
    }

    @Override
    public boolean updateRecords(String table, String[] columnsToUpdate, Object[] newValues, String whereColumn, Object value, @NonNull Context context) {
        // Updates rows in the table where the 'whereColumn' column
        // matches the specified 'value' parameter.
        // Pass 'null' as the 'value' parameter to update all rows in the table.
        // The matching rows will have their old values replaced with the values in 'newValues'
        // for the columns specified in 'columnsToUpdate'.


        if (columnsToUpdate.length != newValues.length || Arrays.asList(columnsToUpdate).contains(R_S_P_COLUMN_ID)) {
            // columns and values must have the same length

            return false;
        }

        String whereClause = whereColumn == null ? null : whereColumn + " LIKE ?";
        String[] valuesClause = value == null ? null : new String[]{value.toString()};

        DbTransactionListener listener = new DbTransactionListener(context);

        try { // returns true if the update operation was successful, false otherwise.

            dbHelper.beginTransactionWithListener(listener);

            if (dbHelper.update(table, buildContentValues(columnsToUpdate, newValues), whereClause, valuesClause) != 0) {

                dbHelper.setTransactionSuccessful();
                return true;
            }
            return false;

        } catch (SQLiteException e) {
            Log.e(TAG, e.toString(), e);
            return false;

        } finally {
            dbHelper.endTransaction();
        }

    }

    @Override
    public List<List<Object>> selectRecords(String table, String[] columnsToSelect, String whereColumn, Object value, String sortingColumn, Boolean ascendent) {
        // Selects the values of the specified 'columns'
        // (pass 'null' to select all columns) from the given 'table'.
        // The 'whereColumn' is used to filter the results, where
        // its value matches the specified 'value' (pass 'null' to select all rows).
        // The 'sortingColumn' parameter is used to sort the results, with
        // 'ascendent' sorting if set to 'true' and 'descendent' sorting if set to 'false'
        // (pass 'null' if sorting is not required).
        // Note: Grouping and having clauses are excluded for increased readability
        // and as they are not applicable in this application.

        String whereClause = whereColumn == null ? null : whereColumn + " = ?";
        String[] valuesClause = value == null ? null : new String[]{value.toString()};
        String sortOrder = null;

        if (sortingColumn != null && ascendent != null) {
            sortOrder = sortingColumn + (ascendent ? " ASC" : " DESC");
        }

        try (Cursor cursor = dbHelper.query(
                table,
                columnsToSelect,
                whereClause,
                valuesClause,
                null,
                null,
                sortOrder)) {

            List<List<Object>> result = new ArrayList<>();

            if (columnsToSelect == null) {

                switch (table) { // columns and values must have the same length

                    case ROUTINES_TABLE:
                        columnsToSelect = R_COLUMNS_ID_INCLUDED;
                        break;

                    case STATS_TABLE:
                        columnsToSelect = S_COLUMNS_ID_INCLUDED;
                        break;

                    case POSITIONS_TABLE:
                        columnsToSelect = P_COLUMNS_ID_INCLUDED;
                        break;

                    default: // non-existent table
                        return null;
                }
            }

            while (cursor.moveToNext()) {

                List<Object> item = new ArrayList<>();
                for (String i : columnsToSelect) {

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
            Log.w(TAG, e.toString(), e);
            return null;

        }
    }

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

    public SQLiteDatabase getDbHelper() {
        return dbHelper;
    }

    public void wipeDatabase(Context context) {

        try {

            dbHelper.delete(ROUTINES_TABLE, null, null);
            dbHelper.delete(POSITIONS_TABLE, null, null);
            dbHelper.delete(STATS_TABLE, null, null);

            dbHelper.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + ROUTINES_TABLE + "'");
            dbHelper.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + POSITIONS_TABLE + "'");
            dbHelper.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + STATS_TABLE + "'");


        } catch (SQLException e) {

            Log.e(TAG, "SQl error", e);
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

}