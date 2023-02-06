package com.richardmeoli.letitfly.logic.database.local.tables;

public interface PositionsTable {

    String POSITIONS_TABLE = "positions";

    //--------------- Columns ---------------//

    String P_COLUMN_ID = "id";
    String P_COLUMN_ROUTINE = "routine";
    String P_COLUMN_X_POS = "x_pos";
    String P_COLUMN_Y_POS = "y_pos";
    String P_COLUMN_IMG_WIDTH = "img_width";
    String P_COLUMN_IMG_HEIGHT = "img_height";
    String P_COLUMN_SHOTS = "shots";
    String P_COLUMN_PTS_PER_SHOT = "pts_per_shot";
    String P_COLUMN_PTS_PER_LAST_SHOT = "pts_per_last_shot";
    String P_COLUMN_NOTES = "notes";

    String[] P_COLUMNS = {P_COLUMN_ROUTINE, P_COLUMN_X_POS, P_COLUMN_Y_POS, P_COLUMN_IMG_WIDTH,
            P_COLUMN_IMG_HEIGHT, P_COLUMN_SHOTS, P_COLUMN_PTS_PER_SHOT, P_COLUMN_PTS_PER_LAST_SHOT, P_COLUMN_NOTES};
    String[] P_COLUMNS_ID_INCLUDED = {P_COLUMN_ID, P_COLUMN_ROUTINE, P_COLUMN_X_POS, P_COLUMN_Y_POS,
            P_COLUMN_IMG_WIDTH, P_COLUMN_IMG_HEIGHT, P_COLUMN_SHOTS, P_COLUMN_PTS_PER_SHOT, P_COLUMN_PTS_PER_LAST_SHOT, P_COLUMN_NOTES};

    //--------------- Values ----------------//

    int P_SHOTS_COUNT_MAX_VALUE = 10000;
    int P_POINTS_PER_SHOT_MAX_VALUE = 10;
    int P_POINTS_PER_LAST_SHOT_MAX_VALUE = 20;
    int P_NOTES_MAX_LENGTH = 20;

}