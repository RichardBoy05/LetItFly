package com.richardmeoli.letitfly.logic.database.local.sqlite;

import com.richardmeoli.letitfly.logic.database.local.tables.StatsTable;
import com.richardmeoli.letitfly.logic.database.local.tables.RoutinesTable;
import com.richardmeoli.letitfly.logic.database.local.tables.PositionsTable;

import java.util.List;

public interface DatabaseContract extends DatabaseAttributes, RoutinesTable, StatsTable, PositionsTable {

    String TAG = "LocalDatabase";

    //--------------- Database operations ---------------//

    boolean insertRecord(String table, Object[] values);
    boolean deleteRecords(String table, String whereColumn, Object value);
    boolean updateRecords(String table, String whereColumn, Object value, String[] columnsToUpdate, Object[] newValues);
    List<List<Object>> selectRecords(String table, String[] columns, String whereColumn, Object value, String sortingColumn, Boolean ascendent);
}