package com.eficksan.whereami;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;

import com.eficksan.whereami.fragments.SplashFragment;
import com.eficksan.whereami.geo.Constants;
import com.eficksan.whereami.geo.LocationRequestingFragment;
import com.eficksan.whereami.geofence.GeofenceFragment;
import com.eficksan.whereami.googleapi.GoogleApiConnectActivity;
import com.eficksan.whereami.maps.MapsFragment;
import com.eficksan.whereami.routing.Router;
import com.eficksan.whereami.routing.Routing;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends GoogleApiConnectActivity implements Router {

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

    @Override
    public void showScreen(int screenId, Bundle args) {
        switch (screenId) {
            case Routing.MAP_SCREEN: {
                Location location = args.getParcelable(Constants.EXTRA_LOCATION_DATA);
                replaceFragment(MapsFragment.newInstance(location), MapsFragment.TAG, false);
                break;
            }
            case Routing.GEOFENCES_SCREEN: {
                Location location = args.getParcelable(Constants.EXTRA_LOCATION_DATA);
                replaceFragment(GeofenceFragment.newInstance(location), GeofenceFragment.TAG, false);
                break;
            }
            case Routing.SPLASH_SCREEN:
                showSplash();
                break;
            case Routing.LOCATION_SCREEN:
            default:
                replaceFragment(LocationRequestingFragment.newInstance(), LocationRequestingFragment.TAG, false);
                break;

        }
    }
}
