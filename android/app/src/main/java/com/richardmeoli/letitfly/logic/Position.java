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
        this.routineName = routineName;
        this.xPos = xPos;
        this.yPos = yPos;
        this.shotsCount = shotsCount;
        this.pointsPerShot = pointsPerShot;
        this.pointsPerLastShot = pointsPerLastShot;
        this.notes = notes;

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
