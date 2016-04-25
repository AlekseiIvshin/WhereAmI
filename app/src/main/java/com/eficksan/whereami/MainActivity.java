package com.eficksan.whereami;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.eficksan.whereami.fragments.LocationRequestingFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, LocationRequestingFragment.newInstance(), LocationRequestingFragment.TAG)
                    .addToBackStack(LocationRequestingFragment.TAG)
                    .commit();
        }
    }
}
