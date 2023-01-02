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
import com.richardmeoli.letitfly.logic.DatabaseHelper;
import com.richardmeoli.letitfly.logic.Routine;

import java.util.UUID;

public class RoutineFragment extends Fragment {

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
            requireContext().deleteDatabase(DatabaseHelper.DATABASE_NAME);

        });

        add.setOnClickListener(v -> {

            try {
                Routine r = new Routine("dsd", null, null, new UUID(1L,1L), 0, false, null, null);
                Toast.makeText(getActivity(), "adding routine", Toast.LENGTH_SHORT).show();
            } catch (IllegalArgumentException e){
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }


        });



    }
}