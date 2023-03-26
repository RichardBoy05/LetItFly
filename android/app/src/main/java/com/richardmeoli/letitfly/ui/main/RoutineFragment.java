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

import com.google.firebase.firestore.FirebaseFirestore;
import com.richardmeoli.letitfly.R;
import com.richardmeoli.letitfly.logic.database.local.sqlite.DatabaseAttributes;
import com.richardmeoli.letitfly.logic.database.local.sqlite.InvalidInputException;
import com.richardmeoli.letitfly.logic.database.online.callbacks.FirestoreOnTransactionCallback;
import com.richardmeoli.letitfly.logic.database.online.firestore.Firestore;
import com.richardmeoli.letitfly.logic.database.online.firestore.FirestoreAttributes;
import com.richardmeoli.letitfly.logic.database.online.firestore.FirestoreError;
import com.richardmeoli.letitfly.logic.entities.Position;
import com.richardmeoli.letitfly.logic.entities.Routine;
import com.richardmeoli.letitfly.logic.users.authentication.Authenticator;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;


public class RoutineFragment extends Fragment implements DatabaseAttributes, FirestoreAttributes {

    private static final String TAG = "RoutineFragment";
    private Button add;
    private Button delete;
    private Button upload;
    private Button download;

    @Override
    public void onResume() {
        super.onResume();

        Authenticator auth = Authenticator.getInstance();
        if (auth.getCurrentUser() == null){
            auth.redirectToLoginActivity(requireContext());
        }

    }

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
        upload = view.findViewById(R.id.uploadDbButton);
        download = view.findViewById(R.id.donwloadDbButton);


        delete.setOnClickListener(v -> {
            requireContext().deleteDatabase(DATABASE_NAME);
            Toast.makeText(getActivity(), "deleted db", Toast.LENGTH_SHORT).show();

        });

        add.setOnClickListener(v -> {

            try {

                ArrayList<Position> positions = new ArrayList<>();
                positions.add(new Position(500, 680, 50, 50, 50, null, 3, "sasasa"));
                positions.add(new Position(400, 700, 500, 50, 150, 3, 6, "ss nç°*°ç\"'''''"));
                positions.add(new Position(900, 600, 500, 50, 50, 10, null, null));
                positions.add(new Position(400, 800, 1600, 50, 50, 9, 5, "kizzi"));
                positions.add(new Position(400, 800, 160, 50, 50, 9, 5, "kizzi"));
                positions.add(new Position(400, 800, 160, 50, 50, 9, 5, "kizzi"));
                positions.add(new Position(400, 800, 160, 50, 50, 9, 5, "kizzi"));
                positions.add(new Position(400, 800, 160, 50, 50, 9, 5, "kizzi"));

                String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

                Random random = new Random();
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < 8; i++) {
                    int randomIndex = random.nextInt(CHARACTERS.length());
                    char randomChar = CHARACTERS.charAt(randomIndex);
                    sb.append(randomChar);
                }


                Routine routine = new Routine(sb.toString(), "opttibile", "#778899", UUID.randomUUID(), 10, true, null, positions);


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


            } catch (InvalidInputException e) {
                Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }


        });

        upload.setOnClickListener(v -> {

            Authenticator auth = Authenticator.getInstance();
            auth.signOutUser();

        });

        download.setOnClickListener(v -> {

            Firestore f = Firestore.getInstance();

            FirebaseFirestore d = FirebaseFirestore.getInstance();

            f.addDocument("routines", null, new String[]{"Boh", "sasa","Boh", "sasa","Boh"}, new FirestoreOnTransactionCallback() {
                @Override
                public void onSuccess() {
                    Toast.makeText(requireContext(), "Suces", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onFailure(FirestoreError error) {
                    Toast.makeText(requireContext(), error.toString(), Toast.LENGTH_SHORT).show();

                }
            });
//
        });


    }
}