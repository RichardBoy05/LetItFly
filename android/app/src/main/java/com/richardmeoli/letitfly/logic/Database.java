package com.richardmeoli.letitfly.logic;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {

    public static final String ROUTINES_TABLE = "routines";
    public static final String STATS_TABLE = "stats";

    public Database(@Nullable Context context) {
        super(context, "letitfly.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // generate routines table

        String routinesStatement = "CREATE TABLE '" + ROUTINES_TABLE + "' (\n" +
                "\t'name' VARCHAR(30) PRIMARY KEY,\n" +
                "\t'author' VARCHAR(20) NOT NULL,\n" +
                "\t'color' CHAR(7) NOT NULL,\n" +
                "\t'uuid' BINARY(16) NOT NULL,\n" +
                "\t'time' SMALLINT,\n" +
                "\t'isPublic' BOOLEAN NOT NULL,\n" +
                "\t'notes' VARCHAR(100)" +
                ");";
        
        db.execSQL(routinesStatement);

        // generate stats table

        String statsStatement = "CREATE TABLE '" + STATS_TABLE +  "'(\n" +
                "\t'id' INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "\t'date' DATETIME NOT NULL,\n" +
                "\t'routine' VARCHAR(30) NOT NULL,\n" +
                "\t'reps' TINYINT NOT NULL DEFAULT '1',\n" +
                "\t'outcome' VARCHAR(5) NOT NULL" +
                ");";

        db.execSQL(statsStatement);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
