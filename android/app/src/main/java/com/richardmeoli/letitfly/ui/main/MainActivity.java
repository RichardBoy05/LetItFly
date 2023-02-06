package com.richardmeoli.letitfly.ui.main;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.richardmeoli.letitfly.R;
import com.richardmeoli.letitfly.logic.database.local.sqlite.Database;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showFragment(new PlayFragment());

        // views initialization

        bottomBar = findViewById(R.id.bottom_bar);

        // databases initialization (maybe to remove)

        Database.getInstance(this);
        FirebaseFirestore.getInstance();

        // basic configuration

        bottomBar.setSelectedItemId(R.id.play_tab);

        // listeners

        bottomBar.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){
                case R.id.routine_tab:
                    showFragment(new RoutineFragment());
                    break;

                case R.id.play_tab:
                    showFragment(new PlayFragment());
                    break;

                case R.id.stats_tab:
                    showFragment(new StatsFragment());
                    break;

                default:
                    return false;
            }

            return true;
        });

    }

    @Override
    protected void onDestroy() {
//        Database.getInstance(this).getDbHelper().close();
        super.onDestroy();
    }

    private void showFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, fragment).commit();
    }

}