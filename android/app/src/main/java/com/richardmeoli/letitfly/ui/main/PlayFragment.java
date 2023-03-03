package com.richardmeoli.letitfly.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.richardmeoli.letitfly.R;
import com.richardmeoli.letitfly.logic.users.authentication.AuthenticationError;
import com.richardmeoli.letitfly.logic.users.authentication.Authenticator;
import com.richardmeoli.letitfly.logic.users.authentication.callbacks.AuthOnEventCallback;

public class PlayFragment extends Fragment {

    private static final String TAG = "PlayFragment";

    @Override
    public void onResume(){
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
        return inflater.inflate(R.layout.fragment_play, container, false);
    }
}