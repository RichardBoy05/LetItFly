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

import java.util.ArrayList;
import java.util.UUID;

import com.richardmeoli.letitfly.R;
import com.richardmeoli.letitfly.logic.Position;
import com.richardmeoli.letitfly.logic.Routine;
import com.richardmeoli.letitfly.logic.database.DatabaseAttributes;
import com.richardmeoli.letitfly.logic.database.InvalidInputException;


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

            ArrayList<Position> positions = new ArrayList<>();
            positions.add(new Position(50, 68, 140, 270, 470, "Dle \" raom'i"));
            positions.add(new Position(40, 70, 150, 280, 408, "l5e \" ram'ice"));
            positions.add(new Position(90, 60, 110, 205, 450, "lle \" ram'ice"));
            positions.add(new Position(40, 80, 160, 20, 40, "Dle \" rom'ie"));

            Routine routine = null;

            try {
                routine = new Routine("dfd", "richard", "#778899", UUID.randomUUID().toString(), 10, false, "Interessas'notes\"", positions);
            } catch (InvalidInputException e) {
                e.printStackTrace();
            }

            Toast.makeText(requireContext(), String.valueOf(routine.save(requireContext())), Toast.LENGTH_SHORT).show();

        });



    }
}