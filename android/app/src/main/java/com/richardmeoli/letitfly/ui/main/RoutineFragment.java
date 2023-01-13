package com.richardmeoli.letitfly.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.richardmeoli.letitfly.R;
import com.richardmeoli.letitfly.logic.Position;
import com.richardmeoli.letitfly.logic.Routine;
import com.richardmeoli.letitfly.logic.database.Database;
import com.richardmeoli.letitfly.logic.database.DatabaseAttributes;
import com.richardmeoli.letitfly.logic.database.InvalidInputException;
import com.richardmeoli.letitfly.logic.database.RoutinesTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;


public class RoutineFragment extends Fragment implements DatabaseAttributes {

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
            requireActivity().deleteDatabase(DATABASE_NAME);

        });

        add.setOnClickListener(v -> {

        try {

            ArrayList<Position> positions = new ArrayList<>();
            positions.add(new Position(50, 68, 5, null, 3, "sasasa"));
            positions.add(new Position(40, 70, 150, 3, 6, "ss nç°*°ç\"'''''"));
            positions.add(new Position(90, 60, 5, 10, null, null));
            positions.add(new Position(40, 80, 160, 9, 5, "kizzi"));
            Routine routine = new Routine("Richard", "opttibile", "#778899", UUID.randomUUID(), 10, false, null, positions);


//            Toast.makeText(requireContext(), String.valueOf(routine.save(requireContext())), Toast.LENGTH_SHORT).show();
//            ArrayList<Object> a = new ArrayList<>(Collections.singletonList("mirco cicardoi"));
//            Database.getInstance(requireContext()).updateRecords(RoutinesTable.ROUTINES_TABLE, RoutinesTable.R_COLUMN_NAME, "Richard", new String[]{RoutinesTable.R_COLUMN_NAME}, a);



        } catch (InvalidInputException e) {
            Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }


    });


}
}