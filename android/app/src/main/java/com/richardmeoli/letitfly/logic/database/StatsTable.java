package com.richardmeoli.letitfly.logic.database;

public interface StatsTable {

    String STATS_TABLE = "stats";

    // columns

    String S_COLUMN_ID = "id";
    String S_COLUMN_ROUTINE = "routine";
    String S_COLUMN_DATE = "date";
    String S_COLUMN_REPS = "reps";
    String S_COLUMN_OUTCOME = "outcome";

    String[] S_COLUMNS = {S_COLUMN_ROUTINE, S_COLUMN_DATE, S_COLUMN_REPS, S_COLUMN_OUTCOME};
    String[] S_COLUMNS_ID_INCLUDED = {S_COLUMN_ID, S_COLUMN_ROUTINE, S_COLUMN_DATE, S_COLUMN_ROUTINE, S_COLUMN_REPS, S_COLUMN_OUTCOME};

    // values

    int S_OUTCOME_MAX_LENGTH = 5000;

}