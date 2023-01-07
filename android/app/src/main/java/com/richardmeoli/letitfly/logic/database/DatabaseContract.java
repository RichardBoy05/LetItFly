package com.richardmeoli.letitfly.logic.database;

import java.util.ArrayList;

public interface DatabaseContract extends DatabaseAttributes, RoutinesTable, StatsTable, PositionsTable {

    boolean insertRecord(String table, ArrayList<Object> values);

    boolean deleteRecords(String table, String whereColumn, Object value);

    boolean updateRecords(String table,
                          String whereColumn,
                          Object value, String[] columnsToUpdate,
                          ArrayList<Object> newValues);

    ArrayList<ArrayList<Object>> selectRecords(String table,
                                               String[] columns,
                                               String whereColumn,
                                               Object value,
                                               String sortingColumn,
                                               Boolean ascendent);
}