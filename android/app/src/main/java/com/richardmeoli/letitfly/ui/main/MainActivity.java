package com.richardmeoli.letitfly.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.richardmeoli.letitfly.R;
import com.richardmeoli.letitfly.logic.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomBar;
    private static SQLiteDatabase dbHelper;


    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showFragment(new PlayFragment());

        // database initialization

        dbHelper = DatabaseHelper.getInstance(MainActivity.this).getWritableDatabase();

        // views initialization

        bottomBar = findViewById(R.id.bottom_bar);

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
        dbHelper.close();
        super.onDestroy();
    }

    private void showFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, fragment).commit();
    }

    public static SQLiteDatabase getDbHelper() {
        return dbHelper;
    }

}