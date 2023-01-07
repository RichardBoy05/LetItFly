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

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

            ArrayList<Position> positions = new ArrayList<>();
            positions.add(new Position(50, 68, 140, 10, 5, "Dle \" raom'i"));
            positions.add(new Position(40, 70, 150, 7, 19, "sss"));
            positions.add(new Position(90, 60, 120, 5, 18, "lle \" ram'ice"));
            positions.add(new Position(40, 80, 160, 3, 1, "Dle \" rom'ie"));
            Routine routine = new Routine("cicarsdofcofmfbo", "richard", "#778899", UUID.randomUUID(), 10, true, null, positions);
            Toast.makeText(requireContext(), String.valueOf(routine.save(requireContext())), Toast.LENGTH_SHORT).show();
            System.out.println(routine);

//            Routine routine = new Routine("dsds", requireContext());
//            System.out.println(routine.toString());

        } catch (InvalidInputException e) {
            Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }


    });


}
}