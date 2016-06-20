package com.eficksan.whereami;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;

import com.eficksan.whereami.fragments.SplashFragment;
import com.eficksan.whereami.geo.LocationRequestingFragment;
import com.eficksan.whereami.googleapi.GoogleApiConnectActivity;
import com.eficksan.whereami.maps.MapActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends GoogleApiConnectActivity {

    private boolean isRestored = false;

    public MainActivity() {
        super(new Api[]{LocationServices.API});
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        isRestored = savedInstanceState != null;
    }

    @Override
    public void onConnected(Bundle bundle) {
        super.onConnected(bundle);
        if (!isRestored) {
            replaceFragment(LocationRequestingFragment.newInstance(), LocationRequestingFragment.TAG, false);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        super.onConnectionFailed(result);
        showSplash();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    public void replaceFragment(Fragment fragment, String tag, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();

        transaction.replace(R.id.fragment_container, fragment, tag);
        if (addToBackStack) {
            transaction.addToBackStack(tag);
        }
        transaction.commit();
    }

    private void showSplash() {
        replaceFragment(SplashFragment.newInstance(), SplashFragment.TAG, false);
    }

    public void showMap(Bundle args) {
        Intent intent = new Intent(getApplicationContext(), MapActivity.class);
        intent.putExtras(args);
        startActivity(intent);
    }

}
