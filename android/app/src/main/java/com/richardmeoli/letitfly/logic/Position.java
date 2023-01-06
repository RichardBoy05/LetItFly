package com.richardmeoli.letitfly.logic;

import com.richardmeoli.letitfly.logic.database.InvalidInputException;
import com.richardmeoli.letitfly.logic.database.PositionsTable;

public class Position implements PositionsTable { // abstraction of the concept of Position

    // fields

    private final int xPos;
    private final int yPos;
    private final int shotsCount;
    private final int pointsPerShot;
    private final int pointsPerLastShot;
    private final String notes;

    // constructor

    public Position(int xPos, int yPos, int shotsCount, int pointsPerShot, int pointsPerLastShot, String notes) throws InvalidInputException {

        if (shotsCount < 1 || shotsCount > P_SHOTS_COUNT_MAX_VALUE){
            throw new InvalidInputException("Invalid Shots count!");
        }

        if (pointsPerShot < 1 || pointsPerShot > P_POINTS_PER_SHOT_MAX_VALUE){
            throw new InvalidInputException("Invalid Points per shot value!");
        }

        if (pointsPerLastShot < 1 || pointsPerLastShot > P_POINTS_PER_LAST_SHOT_MAX_VALUE){
            throw new InvalidInputException("Invalid Points per last shot value!");
        }

        if (notes == null || notes.length() > P_NOTES_MAX_LENGTH){
            throw new InvalidInputException("Invalid notes!");
        }

        this.xPos = xPos;
        this.yPos = yPos;
        this.shotsCount = shotsCount;
        this.pointsPerShot = pointsPerShot;
        this.pointsPerLastShot = pointsPerLastShot;
        this.notes = notes;

    }

    // getters

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
