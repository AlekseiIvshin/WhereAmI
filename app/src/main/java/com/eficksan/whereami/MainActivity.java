package com.eficksan.whereami;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.eficksan.whereami.fragments.LocationRequestingFragment;
import com.eficksan.whereami.fragments.SplashFragment;
import com.eficksan.whereami.googleapi.GoogleApiConnectActivity;
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

    public GoogleApiClient getGoogleApiClient() {
        return mGoogleApiClient;
    }

    @Override
    public void onConnected(Bundle bundle) {
        super.onConnected(bundle);
        if (!isRestored) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, LocationRequestingFragment.newInstance(), LocationRequestingFragment.TAG)
                    .commit();
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

    private void showSplash() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, SplashFragment.newInstance(), SplashFragment.TAG)
                .commit();
    }

}
