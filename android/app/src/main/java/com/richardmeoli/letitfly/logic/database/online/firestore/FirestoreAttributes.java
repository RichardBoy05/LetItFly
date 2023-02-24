package com.richardmeoli.letitfly.logic.database.online.firestore;

public interface FirestoreAttributes {

    //--------------------- Collections ---------------------//

    String ROUTINES_COLLECTION = "routines";
    String POSITIONS_COLLECTION = "positions";
    String USERS_COLLECTION = "users";

    //---------------- "Routines" document ---------------//

    String R_FIELD_NAME = "name";
    String R_FIELD_AUTHOR = "author";
    String R_FIELD_COLOR = "color";
    String R_FIELD_TIME = "time";
    String R_FIELD_NOTES = "notes";
    String[] R_FIELDS = {R_FIELD_NAME, R_FIELD_AUTHOR, R_FIELD_COLOR, R_FIELD_TIME, R_FIELD_NOTES};

    //--------------- "Positions" document ---------------//

    String P_FIELD_X_POS = "x_pos";
    String P_FIELD_Y_POS = "y_pos";
    String P_FIELD_IMG_WIDTH = "img_width";
    String P_FIELD_IMG_HEIGHT = "img_height";
    String P_FIELD_SHOTS = "shots";
    String P_FIELD_PTS_PER_SHOT = "pts_per_shot";
    String P_FIELD_PTS_PER_LAST_SHOT = "pts_per_last_shot";
    String P_FIELD_NOTES = "notes";
    String[] P_FIELDS = {P_FIELD_X_POS, P_FIELD_Y_POS, P_FIELD_IMG_WIDTH, P_FIELD_IMG_HEIGHT,
            P_FIELD_SHOTS, P_FIELD_PTS_PER_SHOT, P_FIELD_PTS_PER_LAST_SHOT, P_FIELD_NOTES};

    //--------------- "Users" document ---------------//

    String U_FIELD_UID = "uid";
    String[] U_FIELDS = {U_FIELD_UID};

}