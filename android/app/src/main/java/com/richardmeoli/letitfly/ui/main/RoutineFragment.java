package com.richardmeoli.letitfly.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.richardmeoli.letitfly.R;
import com.richardmeoli.letitfly.logic.DatabaseContract;
import com.richardmeoli.letitfly.logic.DatabaseHelper;

import java.util.UUID;

public class RoutineFragment extends Fragment implements DatabaseContract {

    private Button add;
    private Button delete;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_routine, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        add = view.findViewById(R.id.add_routine);
        delete = view.findViewById(R.id.delete_db);


        delete.setOnClickListener(v -> {
            Toast.makeText(getActivity(), "deleted db", Toast.LENGTH_SHORT).show();
            requireContext().deleteDatabase(DATABASE_NAME);

        });

        add.setOnClickListener(v -> {

//            try {
//
//                Position a = new Position("Test", 85, 66, 15, 1, 2, "susaaaaaaa");
//                Position b = new Position("Test", 420, 69, 15, 1, 2, "sds");
//
//                ArrayList<Position> pos = new ArrayList<Position>();
//                pos.add(a);
//                pos.add(b);
//
                String uuid = UUID.randomUUID().toString();
//
//                Routine r = new Routine("Test", "Rich", "#0000ff", uuid, 0, false, "sasa", pos);
//                Toast.makeText(getActivity(), "adding routine", Toast.LENGTH_SHORT).show();
//            } catch (IllegalArgumentException e){
//                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
//            }

//            ArrayList<Object> values = new ArrayList<>();
//            values.add("nuova savage date");
//            values.add("01010101010101010x1010");
//            values.add(69);
//            values.add("ssasas");
//
//            String[] columns = new String[4];
//            columns[0] = DatabaseHelper.S_COLUMN_DATE;
//            columns[1] = DatabaseHelper.S_COLUMN_OUTCOME;
//            columns[2] = DatabaseHelper.S_COLUMN_REPS;
//            columns[3] = DatabaseHelper.S_COLUMN_ROUTINE;


//            boolean result = DatabaseHelper.insertRecord(DatabaseHelper.TABLE_ROUTINES, values);
//            Toast.makeText(getActivity(), "addition " + result, Toast.LENGTH_SHORT).show();


//            boolean result2 = DatabaseHelper.deleteRecord(DatabaseHelper.TABLE_ROUTINES, DatabaseHelper.R_COLUMN_AUTHOR, "richard");
//            Toast.makeText(getActivity(), "deletion " + result2, Toast.LENGTH_SHORT).show();

//            boolean result3 = DatabaseHelper.updateRecords("sasa", DatabaseHelper.R_S_P_COLUMN_ID, 41, columns, values);
//            Toast.makeText(getActivity(), "update " + result3, Toast.LENGTH_SHORT).show();

            System.out.println(DatabaseHelper.selectRecords(STATS_TABLE,
                    null,
                    null, null));


        });



    }
}