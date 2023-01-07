package com.richardmeoli.letitfly.logic;

import android.content.Context;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.ArrayList;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import com.richardmeoli.letitfly.logic.database.Database;
import com.richardmeoli.letitfly.logic.database.RoutinesTable;
import com.richardmeoli.letitfly.logic.database.InvalidInputException;

public class Routine implements RoutinesTable { // abstraction of the concept of Routine

    // fields

    private final String name;
    private final String author;
    private final String color;
    private final UUID uuid;
    private final Integer time;
    private final boolean isPublic;
    private final String notes;
    private final ArrayList<Position> positions;

    // constructors

    public Routine(String name, String author, String color, UUID uuid, Integer time, boolean isPublic, String notes, ArrayList<Position> positions) throws InvalidInputException {
        // default constructor that builts a Routine object when all its parameters are given

        if (name == null || name.length() < R_NAME_MIN_LENGTH || name.length() > R_NAME_MAX_LENGTH || !name.matches(R_NAME_VALID_CHARACTERS)) {
            throw new InvalidInputException("Invalid routine name!");
        }

        if (author == null || author.length() < R_AUTHOR_MIN_LENGTH || author.length() > R_AUTHOR_MAX_LENGTH || !author.matches(R_AUTHOR_VALID_CHARACTERS)) {
            throw new InvalidInputException("Invalid author name!");
        }

        if (color == null || color.length() != 7) {
            throw new InvalidInputException("Invalid color!");
        }

        if (uuid == null || uuid.toString().length() != 36) {
            throw new InvalidInputException("Invalid UUID string!");
        }

        if (time != null && (time < 0 || time > R_TIME_MAX_VALUE)) {
            throw new InvalidInputException("Invalid time!");
        }

        if (notes != null && notes.length() > R_NOTES_MAX_LENGTH) {
            throw new InvalidInputException("Invalid notes!");
        }

        if (positions == null || positions.size() < 1) {
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

    @NonNull
    @Override
    public String toString() {
        return "Routine{" +
                "name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", color='" + color + '\'' +
                ", uuid=" + uuid +
                ", time=" + time +
                ", isPublic=" + isPublic +
                ", notes='" + notes + '\'' +
                ", positions=" + positions.toString() +
                '}';
    }

    public Routine(String name, Context context) throws InvalidInputException {

        String[] columns = {R_COLUMN_AUTHOR, R_COLUMN_COLOR, R_COLUMN_UUID,
                R_COLUMN_TIME, R_COLUMN_IS_PUBLIC, R_COLUMN_NOTES};

        ArrayList<ArrayList<Object>> routines = Database.getInstance(context).selectRecords(
                ROUTINES_TABLE, columns, R_COLUMN_NAME, name, null, null);

        if (routines.size() == 0){
            throw new InvalidInputException("A routine named \"" + name + "\" doesn't exist!");
        }

        ArrayList<Object> routine = routines.get(0);

        this.name = name;
        this.author = routine.get(0).toString();
        this.color = routine.get(1).toString();
        this.uuid = UUID.fromString(routine.get(2).toString());
        this.time = (Integer) routine.get(3);
        this.isPublic = (Integer) routine.get(4) != 0;
        this.notes = routine.get(5).toString();

        ArrayList<Position> positions = new ArrayList<>();

        ArrayList<ArrayList<Object>> result2 = Database.getInstance(context).selectRecords(
                encodeTableName(name), null, null, null, R_COLUMN_ID, true);

        for (ArrayList<Object> i : result2){

            Position pos = new Position((int) i.get(1), (int)i.get(2), (int) i.get(3),
                    (Integer) i.get(4), (Integer) i.get(5), (String) i.get(6));

            positions.add(pos);

        }

        this.positions = positions;
    }

//    public Routine(UUID uuid){
//
//    }

    // methods

    public boolean save(Context context) {

        Database db = Database.getInstance(context);
        ArrayList<Object> values = new ArrayList<>(Arrays.asList(name, author, color, uuid.toString(), time, isPublic, notes));

        if (!db.insertRecord(ROUTINES_TABLE, values)) {
            return false;
        }

        String table = encodeTableName(name);
        db.addPositionsTable(table);

        for (Position pos : positions) {

            boolean result = db.insertRecord(table, new ArrayList<>(Arrays.asList(pos.getXPos(), pos.getYPos(),
                    pos.getShotsCount(), pos.getPointsPerShot(), pos.getPointsPerLastShot(), pos.getNotes())));

            if (!result) {
                return false;
            }
        }

        return true;
    }

    private String encodeTableName(String name) {

        try {

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(name.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();

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

    public UUID getUuid() {
        return uuid;
    }

    public Integer getTime() {
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
