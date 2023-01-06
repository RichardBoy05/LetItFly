package com.richardmeoli.letitfly.logic;

import android.util.Base64;
import android.content.Context;

import java.util.Arrays;
import java.util.ArrayList;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

import com.richardmeoli.letitfly.logic.database.Database;
import com.richardmeoli.letitfly.logic.database.RoutinesTable;
import com.richardmeoli.letitfly.logic.database.InvalidInputException;

public class Routine implements RoutinesTable { // abstraction of the concept of Routine

    // fields
    
    private final String name;
    private final String author;
    private final String color;
    private final String uuid;
    private final int time;
    private final boolean isPublic;
    private final String notes;
    private final ArrayList<Position> positions;
    
    // constructor

    public Routine(String name, String author, String color, String uuid, int time, boolean isPublic, String notes, ArrayList<Position> positions) throws InvalidInputException {

        if (name == null || name.length() < R_NAME_MIN_LENGTH || name.length() > R_NAME_MAX_LENGTH || !name.matches(R_NAME_VALID_CHARACTERS)){
            throw new InvalidInputException("Invalid routine name!");
        }

        if (author == null || author.length() < R_AUTHOR_MIN_LENGTH || author.length() > R_AUTHOR_MAX_LENGTH || !author.matches(R_AUTHOR_VALID_CHARACTERS)){
            throw new InvalidInputException("Invalid author name!");
        }

        if (color == null || color.length() != 7){
            throw new InvalidInputException("Invalid color!");
        }

        if (uuid == null || uuid.length() != 36){
            throw new InvalidInputException("Invalid UUID string!");
        }

        if (time < 0 || time > R_TIME_MAX_VALUE){
            throw new InvalidInputException("Invalid time!");
        }

        if (notes == null || notes.length() > R_NOTES_MAX_LENGTH){
            throw new InvalidInputException("Invalid notes!");
        }

        if (positions == null || positions.size() < 1){
            throw new InvalidInputException("Invalid positions!");
        }

        this.name = name;
        this.author = author;
        this.color = color;
        this.uuid = uuid;
        this.time = time;
        this.isPublic = isPublic;
        this.notes = notes;
        this.positions = positions;
    }
    
    // methods

    public boolean save(Context context){

        Database db = Database.getInstance(context);
        ArrayList<Object> values = new ArrayList<>(Arrays.asList(name, author, color, uuid, time, isPublic, notes));

        if (!db.insertRecord(ROUTINES_TABLE, values)){return false;}

        String table = encodeTableName(name);
        db.addPositionsTable(table);

        for (Position pos: positions){

            boolean result = db.insertRecord(table, new ArrayList<>(Arrays.asList(pos.getXPos(), pos.getYPos(),
                    pos.getShotsCount(), pos.getPointsPerShot(), pos.getPointsPerLastShot(), pos.getNotes())));

            if (!result) {return false;}
        }

        return true;
    }

    private String encodeTableName(String name) {

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String hashString = android.util.Base64.encodeToString(digest.digest(name.getBytes(StandardCharsets.UTF_8)), Base64.DEFAULT);
            return hashString.substring(0, hashString.length() - 2).replaceAll("[^a-zA-Z0-9]", "_");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return name;
        }
    }
    
    // getters

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
