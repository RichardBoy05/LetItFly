package com.richardmeoli.letitfly.logic.entities;

import android.content.Context;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.UUID;
import java.util.ArrayList;

import com.richardmeoli.letitfly.logic.database.local.sqlite.Database;
import com.richardmeoli.letitfly.logic.database.local.tables.RoutinesTable;
import com.richardmeoli.letitfly.logic.database.local.tables.PositionsTable;
import com.richardmeoli.letitfly.logic.database.local.sqlite.InvalidInputException;
import com.richardmeoli.letitfly.logic.database.online.firestore.Firestore;
import com.richardmeoli.letitfly.logic.database.online.firestore.FirestoreAttributes;
import com.richardmeoli.letitfly.logic.database.online.firestore.FirestoreError;
import com.richardmeoli.letitfly.logic.database.online.callbacks.FirestoreOnTransactionCallback;

public class Routine implements RoutinesTable, PositionsTable, FirestoreAttributes { // abstraction of the concept of Routine

    private static final String TAG = "Routine";

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
        // builts a Routine object with the parameters passed in the constructor

        if (name == null || name.length() < R_NAME_MIN_LENGTH || name.length() > R_NAME_MAX_LENGTH || !name.matches(R_NAME_VALID_CHARACTERS)) {
            throw new InvalidInputException("Invalid routine name!");
        }

        if (author == null || author.length() < R_AUTHOR_MIN_LENGTH || author.length() > R_AUTHOR_MAX_LENGTH || !author.matches(R_AUTHOR_VALID_CHARACTERS)) {
            throw new InvalidInputException("Invalid author name!");
        }

        if (color == null || color.length() != 7 || color.charAt(0) != '#') {
            throw new InvalidInputException("Invalid color!");
        }

        if (uuid == null || uuid.toString().length() != 36) {
            throw new InvalidInputException("Invalid UUID string!");
        }

        if (time != null && (time < 0 || time > R_TIME_MAX_VALUE)) {
            throw new InvalidInputException("Invalid time!");
        }

        if (notes != null && (notes.length() < 1 || notes.length() > R_NOTES_MAX_LENGTH)) {
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

    public Routine(String name, Context context) throws InvalidInputException {
        // builts a Routine object by selecting its values from the local database (select by name)

        String[] r_columns = {R_COLUMN_AUTHOR, R_COLUMN_COLOR, R_COLUMN_UUID,
                R_COLUMN_TIME, R_COLUMN_IS_PUBLIC, R_COLUMN_NOTES};

        List<List<Object>> routines = Database.getInstance(context).selectRecords(
                ROUTINES_TABLE, r_columns, R_COLUMN_NAME, name, null, null);

        if (routines.size() == 0) {
            throw new InvalidInputException("A routine named \"" + name + "\" doesn't exist!");
        }

        List<Object> routine = routines.get(0);

        this.name = name;
        this.author = routine.get(0).toString();
        this.color = routine.get(1).toString();
        this.uuid = UUID.fromString(routine.get(2).toString());
        this.time = (Integer) routine.get(3);
        this.isPublic = (Integer) routine.get(4) != 0;
        this.notes = routine.get(5) == null ? null : routine.get(5).toString();

        ArrayList<Position> positions = new ArrayList<>();
        String[] p_columns = {P_COLUMN_X_POS, P_COLUMN_Y_POS, P_COLUMN_IMG_WIDTH, P_COLUMN_IMG_HEIGHT,
                P_COLUMN_SHOTS, P_COLUMN_PTS_PER_SHOT, P_COLUMN_PTS_PER_LAST_SHOT, P_COLUMN_NOTES};

        List<List<Object>> positionsResult = Database.getInstance(context).selectRecords(
                POSITIONS_TABLE, p_columns, P_COLUMN_ROUTINE, name, P_COLUMN_ID, true);

        for (List<Object> i : positionsResult) {

            Position pos = new Position((int) i.get(0), (int) i.get(1), (int) i.get(2), (int) i.get(3),
                    (int) i.get(4), (Integer) i.get(5), (Integer) i.get(6), (String) i.get(7));

            positions.add(pos);

        }

        this.positions = positions;
    }


    // methods

    public void save(Context context, final FirestoreOnTransactionCallback callback) {

        Database db = Database.getInstance(context);
        Object[] values = {name, author, color, uuid.toString(), time, isPublic, notes};

        if (!db.insertRecord(ROUTINES_TABLE, values, context)) {
            callback.onFailure(FirestoreError.LOCAL_DB_ERROR);
            return;
        }

        for (Position pos : positions) {

            boolean result = db.insertRecord(POSITIONS_TABLE, new Object[]{name, pos.getXPos(), pos.getYPos(),
                    pos.getImgWidth(), pos.getImgHeight(), pos.getShotsCount(), pos.getPointsPerShot(), pos.getPointsPerLastShot(), pos.getNotes()}, context);

            if (!result) {
                db.deleteRecords(ROUTINES_TABLE, R_COLUMN_NAME, name, context);
                callback.onFailure(FirestoreError.LOCAL_DB_ERROR);
                return;
            }
        }

        if (!isPublic) {
            callback.onSuccess();
            return;
        }

        uploadToFirestore(new FirestoreOnTransactionCallback() {
            @Override
            public void onSuccess() {
                callback.onSuccess();
            }

            @Override
            public void onFailure(FirestoreError error) {
                callback.onFailure(error);

            }

        });


    }

    private void uploadToFirestore(final FirestoreOnTransactionCallback callback) {

        Firestore fs = Firestore.getInstance();

        fs.addDocument(ROUTINES_COLLECTION, uuid.toString(), new Object[]{name, author, color, time, notes}, new FirestoreOnTransactionCallback() {
            @Override
            public void onSuccess() {

                for (int i = 0; i < positions.size(); i++) {

                    fs.addDocument(POSITIONS_COLLECTION, uuid + " (" + (int) (i + 1) + ")", new Object[]{positions.get(i).getXPos(), positions.get(i).getYPos(),
                            positions.get(i).getImgWidth(), positions.get(i).getImgHeight(), positions.get(i).getShotsCount(),
                            positions.get(i).getPointsPerShot(), positions.get(i).getPointsPerLastShot(), positions.get(i).getNotes()}, new FirestoreOnTransactionCallback() {

                        @Override
                        public void onSuccess() {
                            callback.onSuccess();
                        }

                        @Override
                        public void onFailure(FirestoreError error) {

                            fs.deleteDocument(ROUTINES_COLLECTION, uuid.toString(), new FirestoreOnTransactionCallback() {
                                @Override
                                public void onSuccess() {
                                    callback.onFailure(error);
                                }

                                @Override
                                public void onFailure(FirestoreError error) {
                                    callback.onFailure(error);

                                }
                            });

                        }
                    });

                }

            }

            @Override
            public void onFailure(FirestoreError error) {
                callback.onFailure(error);
            }
        });
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
