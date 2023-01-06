package com.richardmeoli.letitfly.logic;

import android.util.Base64;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import com.richardmeoli.letitfly.logic.database.PositionsTable;
import com.richardmeoli.letitfly.logic.database.RoutinesTable;

public class Position implements PositionsTable, RoutinesTable {

    // abstraction of the concept of Position

    private final int xPos;
    private final int yPos;
    private final int shotsCount;
    private final int pointsPerShot;
    private final int pointsPerLastShot;
    private final String notes;

    public Position(int xPos, int yPos, int shotsCount, int pointsPerShot, int pointsPerLastShot, String notes) {

        if (shotsCount < 1 || shotsCount > R_TIME_MAX_VALUE){
            throw new IllegalArgumentException("Invalid Shots count");
        }

        if (pointsPerShot < 1 || pointsPerShot > R_TIME_MAX_VALUE){
            throw new IllegalArgumentException("Invalid Points per shot value");
        }

        if (pointsPerLastShot < 1 || pointsPerLastShot > R_TIME_MAX_VALUE){
            throw new IllegalArgumentException("Invalid Points per last shot value");
        }

        if (notes == null || notes.length() > P_NOTES_MAX_LENGTH || notes.indexOf('§') != -1){
            throw new IllegalArgumentException("Invalid notes");
        }

        this.xPos = xPos;
        this.yPos = yPos;
        this.shotsCount = shotsCount;
        this.pointsPerShot = pointsPerShot;
        this.pointsPerLastShot = pointsPerLastShot;
        this.notes = notes.replaceAll("'", "§");

    }


    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }

    public int getShotsCount() {
        return shotsCount;
    }

    public int getPointsPerShot() {
        return pointsPerShot;
    }

    public int getPointsPerLastShot() {
        return pointsPerLastShot;
    }

    public String getNotes() {
        return notes;
    }
}
