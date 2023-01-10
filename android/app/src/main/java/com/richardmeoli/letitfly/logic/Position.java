package com.richardmeoli.letitfly.logic;

import androidx.annotation.NonNull;

import com.richardmeoli.letitfly.logic.database.InvalidInputException;
import com.richardmeoli.letitfly.logic.database.PositionsTable;

public class Position implements PositionsTable { // abstraction of the concept of Position

    // fields

    private final int xPos;
    private final int yPos;
    private final int shotsCount;


    private final Integer pointsPerShot;
    private final Integer pointsPerLastShot;
    private final String notes;

    // constructor

    public Position(int xPos, int yPos, int shotsCount, Integer pointsPerShot, Integer pointsPerLastShot, String notes) throws InvalidInputException {

        if (shotsCount < 1 || shotsCount > P_SHOTS_COUNT_MAX_VALUE){
            throw new InvalidInputException("Invalid Shots count!");
        }

        if (pointsPerShot != null && (pointsPerShot < 1 || pointsPerShot > P_POINTS_PER_SHOT_MAX_VALUE)){
            throw new InvalidInputException("Invalid Points per shot value!");
        }

        if (pointsPerLastShot != null && (pointsPerLastShot < 1 || pointsPerLastShot > P_POINTS_PER_LAST_SHOT_MAX_VALUE)){
            throw new InvalidInputException("Invalid Points per last shot value!");
        }

        if (notes != null && (notes.length() > P_NOTES_MAX_LENGTH)){
            throw new InvalidInputException("Invalid notes!");
        }

        this.xPos = xPos;
        this.yPos = yPos;
        this.shotsCount = shotsCount;
        this.pointsPerShot = pointsPerShot == null ? 1 : pointsPerShot;
        this.pointsPerLastShot = pointsPerLastShot == null ? 1 : pointsPerLastShot;
        this.notes = notes;

    }

    // methods

    @NonNull
    @Override
    public String toString() {
        return "Position{" +
                "xPos=" + xPos +
                ", yPos=" + yPos +
                ", shotsCount=" + shotsCount +
                ", pointsPerShot=" + pointsPerShot +
                ", pointsPerLastShot=" + pointsPerLastShot +
                ", notes='" + notes + '\'' +
                '}';
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

    public Integer getPointsPerShot() {
        return pointsPerShot;
    }

    public Integer getPointsPerLastShot() {
        return pointsPerLastShot;
    }

    public String getNotes() {
        return notes;
    }
}
