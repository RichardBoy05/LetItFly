package com.richardmeoli.letitfly.logic.database.online;

public interface FirestoreAttributes {

    // collections

    String ROUTINES_COLLECTION = "routines";
    String POSITIONS_COLLECTION = "positions";

    // routines document (id = routine uuid)

    String R_FIELD_NAME = "name";
    String R_FIELD_AUTHOR = "author";
    String R_FIELD_COLOR = "color";
    String R_FIELD_TIME = "time";
    String R_FIELD_NOTES = "notes";

    String[] R_FIELDS = {R_FIELD_NAME, R_FIELD_AUTHOR, R_FIELD_COLOR, R_FIELD_TIME, R_FIELD_NOTES};

    // positions document (id = routine uuid)

    String P_FIELDS_X_POS = "x_pos";
    String P_FIELDS_Y_POS = "y_pos";
    String P_FIELDS_IMG_WIDTH = "img_width";
    String P_FIELDS_IMG_HEIGHT = "img_height";
    String P_FIELDS_SHOTS = "shots";
    String P_FIELDS_PTS_PER_SHOT = "pts_per_shot";
    String P_FIELDS_PTS_PER_LAST_SHOT = "pts_per_last_shot";
    String P_FIELDS_NOTES = "notes";

    String[] P_FIELDS = {P_FIELDS_X_POS, P_FIELDS_Y_POS, P_FIELDS_IMG_WIDTH, P_FIELDS_IMG_HEIGHT,
            P_FIELDS_SHOTS, P_FIELDS_PTS_PER_SHOT, P_FIELDS_PTS_PER_LAST_SHOT, P_FIELDS_NOTES};

}
