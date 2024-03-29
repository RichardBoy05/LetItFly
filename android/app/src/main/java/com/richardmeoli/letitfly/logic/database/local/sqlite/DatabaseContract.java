package com.richardmeoli.letitfly.logic.database.local.sqlite;

import android.content.Context;

import com.richardmeoli.letitfly.logic.database.local.tables.StatsTable;
import com.richardmeoli.letitfly.logic.database.local.tables.RoutinesTable;
import com.richardmeoli.letitfly.logic.database.local.tables.PositionsTable;

import java.util.List;

public interface DatabaseContract extends DatabaseAttributes, RoutinesTable, StatsTable, PositionsTable {

    String TAG = "LocalDatabase";

    //--------------- Database operations ---------------//

    boolean insertRecord(String table, Object[] values, Context context);
    boolean deleteRecords(String table, String whereColumn, Object value, Context context);
    boolean updateRecords(String table, String[] columnsToUpdate, Object[] newValues, String whereColumn, Object value, Context context);
    List<List<Object>> selectRecords(String table, String[] columnsToSelect, String whereColumn, Object value, String sortingColumn, Boolean ascendent);

}