package com.pmapps.bustime2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.pmapps.bustime2.NavigationFragments.FavouritesFragment;
import com.pmapps.bustime2.NavigationFragments.NearbyFragment;
import com.pmapps.bustime2.NavigationFragments.SettingsFragment;

public class MainActivity extends AppCompatActivity {

    ChipNavigationBar bottomNavBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavBar = findViewById(R.id.bottomNavBar);
        bottomNavBar.setItemSelected(R.id.item0,true);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FavouritesFragment()).commit();
        bottomNavBar.setOnItemSelectedListener(i -> {
            Fragment fragment = null;
            if (i == R.id.item0) fragment = new FavouritesFragment();
            else if (i == R.id.item1) fragment = new NearbyFragment();
            else if (i == R.id.item2) fragment = new SettingsFragment();
            if (fragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
            }
        });

    }
    
}