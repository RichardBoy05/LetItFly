package com.richardmeoli.letitfly.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.annotation.SuppressLint;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.richardmeoli.letitfly.R;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("NonConstantResourceId")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showFragment(new com.richardmeoli.letitfly.main.PlayFragment());

        // views initialization

        BottomNavigationView bottomBar = findViewById(R.id.bottom_bar);

        // basic configuration

        bottomBar.setSelectedItemId(R.id.play_tab);

        // listeners

        bottomBar.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){
                case R.id.routine_tab:
                    showFragment(new com.richardmeoli.letitfly.main.RoutineFragment());
                    break;

                case R.id.play_tab:
                    showFragment(new com.richardmeoli.letitfly.main.PlayFragment());
                    break;

                case R.id.stats_tab:
                    showFragment(new com.richardmeoli.letitfly.main.StatsFragment());
                    break;

                default:
                    return false;
            }

            return true;
        });

    }

    private void showFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, fragment).commit();
    }
}