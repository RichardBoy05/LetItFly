package com.richardmeoli.letitfly.logic.entities;

import androidx.annotation.NonNull;

import com.richardmeoli.letitfly.logic.database.local.PositionsTable;
import com.richardmeoli.letitfly.logic.database.local.InvalidInputException;

public class Position implements PositionsTable { // abstraction of the concept of Position

    // fields

    private final int xPos;
    private final int yPos;
    private final int imgWidth;
    private final int imgHeight;
    private final int shotsCount;
    private final Integer pointsPerShot;
    private final Integer pointsPerLastShot;
    private final String notes;

    // constructor

    public Position(int xPos, int yPos, int imgWidth, int imgHeight, int shotsCount, Integer pointsPerShot, Integer pointsPerLastShot, String notes) throws InvalidInputException {

        if (shotsCount < 1 || shotsCount > P_SHOTS_COUNT_MAX_VALUE){
            throw new InvalidInputException("Invalid Shots count!");
        }

        if (pointsPerShot != null && (pointsPerShot < 1 || pointsPerShot > P_POINTS_PER_SHOT_MAX_VALUE)){
            throw new InvalidInputException("Invalid Points per shot value!");
        }

        if (pointsPerLastShot != null && (pointsPerLastShot < 1 || pointsPerLastShot > P_POINTS_PER_LAST_SHOT_MAX_VALUE)){
            throw new InvalidInputException("Invalid Points per last shot value!");
        }

        if (notes != null && (notes.length() < 1 || notes.length() > P_NOTES_MAX_LENGTH)){
            throw new InvalidInputException("Invalid notes!");
        }

        this.xPos = xPos;
        this.yPos = yPos;
        this.imgWidth = imgWidth;
        this.imgHeight = imgHeight;
        this.shotsCount = shotsCount;
        this.pointsPerShot = pointsPerShot == null ? 1 : pointsPerShot; // DEFAULT 1
        this.pointsPerLastShot = pointsPerLastShot == null ? 1 : pointsPerLastShot; // DEFAULT 1
        this.notes = notes;

    }

    // methods

    @NonNull
    @Override
    public String toString() {
        return "Position{" +
                "xPos=" + xPos +
                ", yPos=" + yPos +
                ", imgWidth=" + imgWidth +
                ", imgHeight=" + imgHeight +
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

    public int getImgWidth() {return imgWidth;}

    public int getImgHeight() {
        return imgHeight;
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
