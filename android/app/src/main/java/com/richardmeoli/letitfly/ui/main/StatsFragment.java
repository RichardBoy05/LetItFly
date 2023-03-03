package com.richardmeoli.letitfly.ui.main;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import androidx.fragment.app.Fragment;

import com.richardmeoli.letitfly.R;
import com.richardmeoli.letitfly.logic.users.authentication.Authenticator;

public class StatsFragment extends Fragment {

    @Override
    public void onResume(){
        super.onResume();

        Authenticator auth = Authenticator.getInstance();
        if (auth.getCurrentUser() == null){
            auth.redirectToLoginActivity(requireContext());
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stats, container, false);
    }
}