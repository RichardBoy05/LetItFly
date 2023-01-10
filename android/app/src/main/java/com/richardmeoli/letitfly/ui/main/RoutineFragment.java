package com.richardmeoli.letitfly.ui.main;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
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

        try {

//            ArrayList<Position> positions = new ArrayList<>();
//            positions.add(new Position(50, 68, 14, null, 3, null));
//            positions.add(new Position(40, 70, 150, 3, 6, null));
//            positions.add(new Position(90, 60, 120, 10, null, null));
//            positions.add(new Position(40, 80, 160, 9, 5, null));
//            Routine routine = new Routine("fffff", "LMAO", "#778899", UUID.randomUUID(), null, true, null, positions);
//            Toast.makeText(requireContext(), String.valueOf(routine.save(requireContext())), Toast.LENGTH_SHORT).show();
//            System.out.println(routine);

            Routine a = new Routine(UUID.fromString("2d66b4db-4a2e-47f2-8738-1be803d4d5b4"), requireContext());
            System.out.println(a);


        } catch (InvalidInputException e) {
            Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }


    });


}
}