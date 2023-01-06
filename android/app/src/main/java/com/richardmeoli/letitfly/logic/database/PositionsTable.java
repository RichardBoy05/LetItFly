package com.richardmeoli.letitfly.logic.database;

public interface PositionsTable {

    // columns

    String P_COLUMN_X_POS = "x_pos";
    String P_COLUMN_Y_POS = "y_pos";
    String P_COLUMN_SHOTS = "shots";
    String P_COLUMN_PTS_PER_SHOT = "pts_per_shot";
    String P_COLUMN_PTS_PER_LAST_SHOT = "pts_per_last_shot";
    String P_COLUMN_NOTES = "notes";

    String[] P_COLUMNS = {P_COLUMN_X_POS, P_COLUMN_Y_POS, P_COLUMN_SHOTS,
            P_COLUMN_PTS_PER_SHOT, P_COLUMN_PTS_PER_LAST_SHOT, P_COLUMN_NOTES};
    String[] P_COLUMNS_ID_INCLUDED = {"id", P_COLUMN_X_POS, P_COLUMN_Y_POS,
            P_COLUMN_SHOTS, P_COLUMN_PTS_PER_SHOT, P_COLUMN_PTS_PER_LAST_SHOT, P_COLUMN_NOTES};

    // values

    int P_NOTES_MAX_LENGTH = 15;

}