package com.richardmeoli.letitfly.logic;

import java.util.ArrayList;
import java.util.UUID;

public class Routine {

    // abstraction of the concept of Routine

    private final String name;
    private final String author;
    private final String color;
    private final UUID uuid;
    private final int time;
    private final boolean isPublic;
    private final String notes;
    private final ArrayList<Position> positions;

    public Routine(String name, String author, String color, UUID uuid, int time, boolean isPublic, String notes, ArrayList<Position> positions) {
        this.name = name;
        this.author = author;
        this.color = color;
        this.uuid = uuid;
        this.time = time;
        this.isPublic = isPublic;
        this.notes = notes;
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


    public UUID getUuid() {
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
