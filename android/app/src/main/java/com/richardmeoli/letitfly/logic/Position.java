package com.richardmeoli.letitfly.logic;

import android.database.sqlite.SQLiteDatabase;

import com.richardmeoli.letitfly.ui.main.MainActivity;

public class Position {

    // abstraction of the concept of Position

    private final String routineName;
    private final int xPos;
    private final int yPos;
    private final int shotsCount;
    private final int pointsPerShot;
    private final int pointsPerLastShot;
    private final String notes;

    public Position(String routineName, int xPos, int yPos, int shotsCount, int pointsPerShot, int pointsPerLastShot, String notes) {

        if (routineName == null || routineName.length() < DatabaseHelper.ROUTINE_NAME_MIN_LENGTH || routineName.length() > DatabaseHelper.ROUTINE_NAME_MAX_LENGTH){
            throw new IllegalArgumentException("Invalid Routine name");
        }

        if (shotsCount < 1 || shotsCount > DatabaseHelper.SMALLINT_MAX_VALUE){
            throw new IllegalArgumentException("Invalid Shots count");
        }

        if (pointsPerShot < 1 || pointsPerShot > DatabaseHelper.SMALLINT_MAX_VALUE){
            throw new IllegalArgumentException("Invalid Points per shot value");
        }

        if (pointsPerLastShot < 1 || pointsPerLastShot > DatabaseHelper.SMALLINT_MAX_VALUE){
            throw new IllegalArgumentException("Invalid Points per last shot value");
        }

        if (notes == null || notes.length() > DatabaseHelper.P_NOTES_MAX_LENGTH || notes.indexOf('§') != -1){
            throw new IllegalArgumentException("Invalid notes");
        }

        this.routineName = routineName.replaceAll("[^a-zA-Z0-9]", "_");;
        this.xPos = xPos;
        this.yPos = yPos;
        this.shotsCount = shotsCount;
        this.pointsPerShot = pointsPerShot;
        this.pointsPerLastShot = pointsPerLastShot;
        this.notes = notes.replaceAll("'", "§");

    }

    public String getRoutineName() {
        return routineName;
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
