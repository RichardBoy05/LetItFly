package com.richardmeoli.letitfly.logic;

import java.util.ArrayList;

public class Routine {

    // abstraction of the concept of Routine

    private final String name;
    private final String author;
    private final String color;
    private final String uuid;
    private final int time;
    private final boolean isPublic;
    private final String notes;
    private final ArrayList<Position> positions;

    public Routine(String name, String author, String color, String uuid, int time, boolean isPublic, String notes, ArrayList<Position> positions) {

        if (name == null || name.length() < DatabaseHelper.ROUTINE_NAME_MIN_LENGTH || name.length() > DatabaseHelper.ROUTINE_NAME_MAX_LENGTH){
            throw new IllegalArgumentException("Invalid name");
        }

        if (author == null || author.length() < DatabaseHelper.USERNAME_MIN_LENGTH || author.length() > DatabaseHelper.USERNAME_MAX_LENGTH){
            throw new IllegalArgumentException("Invalid username");
        }

        if (color == null || color.length() != 7){
            throw new IllegalArgumentException("Invalid color");
        }

        if (uuid == null || uuid.length() != 36){
            throw new IllegalArgumentException("Invalid UUID string");
        }

        if (time < 0 || time > DatabaseHelper.SMALLINT_MAX_VALUE){
            throw new IllegalArgumentException("Invalid time");
        }

        if (notes == null || notes.length() > DatabaseHelper.R_NOTES_MAX_LENGTH || notes.indexOf('§') != -1){
            throw new IllegalArgumentException("Invalid notes");
        }

        if (positions == null || positions.size() < 1){
            throw new IllegalArgumentException("Invalid positions");
        }

        this.name = name.replaceAll("[^a-zA-Z0-9]", "_");
        this.author = author.replaceAll("[^a-zA-Z0-9]", "_");
        this.color = color;
        this.uuid = uuid;
        this.time = time;
        this.isPublic = isPublic;
        this.notes = notes.replaceAll("'", "§");;
        this.positions = positions;
    }

    public String getName() {
        return name;
    }


    public String getAuthor() {
        return author;
    }

    public String getColor() {
        return color;
    }


    public String getUuid() {
        return uuid;
    }


    public int getTime() {
        return time;
    }


    public boolean isPublic() {
        return isPublic;
    }


    public String getNotes() {
        return notes;
    }

    public ArrayList<Position> getPositions() {
        return positions;
    }

}
