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
import com.richardmeoli.letitfly.logic.database.local.DatabaseAttributes;
import com.richardmeoli.letitfly.logic.database.local.InvalidInputException;
import com.richardmeoli.letitfly.logic.database.online.FirestoreAttributes;
import com.richardmeoli.letitfly.logic.database.online.FirestoreError;
import com.richardmeoli.letitfly.logic.database.online.callbacks.FirestoreOnTransactionCallback;

import java.util.ArrayList;
import java.util.UUID;


public class RoutineFragment extends Fragment implements DatabaseAttributes, FirestoreAttributes {

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
            positions.add(new Position(50, 68, 5, 50, 50, null, 3, "sasasa"));
            positions.add(new Position(40, 70, 50, 50, 150, 3, 6, "ss nç°*°ç\"'''''"));
            positions.add(new Position(90, 60, 5, 50, 50, 10, null, null));
            positions.add(new Position(40, 80, 160, 50, 50, 9, 5, "kizzi"));
            Routine routine = new Routine("Ricfhasdsdsasasasrd", "opttibile", "#778899", UUID.randomUUID(), 10, true, null, positions);


            routine.save(requireContext(), new FirestoreOnTransactionCallback() {
                @Override
                public void onSuccess() {
                    Toast.makeText(requireContext(), "Ezgo", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(FirestoreError error) {
                    Toast.makeText(requireContext(), error.toString(), Toast.LENGTH_SHORT).show();
                }
            });

//            Firestore fs = Firestore.getInstance();
//
//            ArrayList<String> fields = new ArrayList<>();
//            fields.add("name");
//
//            fs.selectDocumentById(ROUTINES_COLLECTION, null, "2b127eb2-dadf-45ca-b309-89ff7ffc4a15", new FirestoreOnSingleQueryCallback() {
//                @Override
//                public void onSuccess(Map<String, Object> list) {
//                    Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show();
//
//                }
//
//                @Override
//                public void onFailure(FirestoreError error) {
//                    Toast.makeText(requireContext(), error.toString(), Toast.LENGTH_SHORT).show();
//
//                }
//            });


        } catch (InvalidInputException e) {
            Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }


    });


}
}