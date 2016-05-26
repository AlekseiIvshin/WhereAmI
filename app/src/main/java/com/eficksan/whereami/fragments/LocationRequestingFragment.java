package com.eficksan.whereami.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.eficksan.whereami.App;
import com.eficksan.whereami.R;
import com.eficksan.whereami.geo.Constants;
import com.eficksan.whereami.geo.FetchAddressIntentService;
import com.eficksan.whereami.googleapi.ApiConnectionObserver;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Aleksei Ivshin
 * on 24.04.2016.
 */
@SuppressWarnings("ResourceType")
public class LocationRequestingFragment extends BaseApiConnectedFragment
        implements LocationListener, ResultCallback<LocationSettingsResult> {

    public static final String TAG = LocationRequestingFragment.class.getSimpleName();

    private static final int REQUEST_CHECK_SETTINGS = 42;

    public static final int LOCATION_REQUEST_INTERVAL = 10000;
    public static final int LOCATION_REQUEST_FASTEST_INTERVAL = 5000;

    private static final String KEY_REQUESTING_LOCATION_UPDATES = "REQUESTING_LOCATION_UPDATES";
    private static final String KEY_LAST_LOCATION = "KEY_LAST_LOCATION";
    private static final String KEY_LAST_LOCATION_ADDRESSES = "KEY_LAST_LOCATION_ADDRESSES";

    @Bind(R.id.label_location)
    TextView mLocationAddresses;
    @Bind(R.id.label_coordinates)
    TextView mLocationCoordinates;
    @Bind(R.id.request_location)
    FloatingActionButton mRequestLocation;

    @Inject
    LocationManager locationManager;

    private Location mLastLocation;
    private String mLastLocationAddresses;
    private LocationRequest mLocationRequest;
    private boolean mRequestingLocationUpdates = false;


    @SuppressLint("ParcelCreator")
    private class AddressResultReceiver extends ResultReceiver {

        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            switch (resultCode) {
                case Constants.SUCCESS_RESULT:
                    mLastLocationAddresses = resultData.getString(Constants.RESULT_DATA_KEY);
                    updateUI();
                    break;
                case Constants.FAILURE_RESULT:
                    Toast.makeText(getActivity(), resultData.getString(Constants.RESULT_DATA_KEY), Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        @Override
        public int describeContents() {
            return 0;
        }
    }

    private AddressResultReceiver mResultReceiver = new AddressResultReceiver(new Handler(Looper.getMainLooper()));

    public static LocationRequestingFragment newInstance() {
        return new LocationRequestingFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getActivity().getApplication()).getObjectGraph().inject(this);
        restoreLocationFromBundle(savedInstanceState);
        createLocationRequest();
        requestLastLocation();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_location_requesting, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        getPersistedLocation();
        updateUI();
    }

    /* Request updates at startup */
    @Override
    public void onResume() {
        super.onResume();
        if (mRequestingLocationUpdates) {
            startLocationRequest();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mRequestingLocationUpdates) {
            stopLocationRequest();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_REQUESTING_LOCATION_UPDATES, mRequestingLocationUpdates);
        outState.putParcelable(KEY_LAST_LOCATION, mLastLocation);
        outState.putString(KEY_LAST_LOCATION_ADDRESSES, mLastLocationAddresses);
    }

    @Override
    public void onStop() {
        persistLocation();
        super.onStop();
    }

    @OnClick(R.id.request_location)
    public void handleRequestLocation() {
        Log.v(TAG, "Handle location request");
        if (!mRequestingLocationUpdates) {
            mRequestingLocationUpdates = true;
            startLocationRequest();
        } else {
            mRequestingLocationUpdates = false;
            stopLocationRequest();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.v(TAG, String.format("Location changed: %1$.4f x %2$.4f", location.getLatitude(), location.getLongitude()));
        mLastLocation = location;
        FetchAddressIntentService.requestLocationAddresses(getActivity(), mResultReceiver, mLastLocation);
        updateUI();
    }

    @Override
    public void onResult(LocationSettingsResult result) {
        final Status status = result.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                Log.v(TAG, "Location settings are satisfied");
                Log.v(TAG, "Location requested: " + mLocationRequest.toString());
                LocationServices.FusedLocationApi
                        .requestLocationUpdates(getGoogleApiClient(), mLocationRequest, this);
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                Log.v(TAG, "Location settings are required");
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    status.startResolutionForResult(
                            getActivity(),
                            REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    // Ignore the error.
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                Log.v(TAG, "Location settings are required, but cannot be changed");
                // Location settings are not satisfied. However, we have no way
                // to fix the settings so we won't show the dialog.
                Toast.makeText(getActivity(), R.string.settings_change_unavailable, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void getPersistedLocation() {
        if (mLastLocationAddresses == null || mLastLocationAddresses.isEmpty()) {
            SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
            mLastLocationAddresses = preferences.getString(KEY_LAST_LOCATION_ADDRESSES, "");
        }
    }

    private void persistLocation() {
        if (mLastLocationAddresses != null && !mLastLocationAddresses.isEmpty()) {
            getActivity().getPreferences(Context.MODE_PRIVATE)
                    .edit().putString(KEY_LAST_LOCATION_ADDRESSES, mLastLocationAddresses)
                    .apply();
        }
    }

    private void requestLastLocation() {
        if (isGoogleApiConnected()) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(getGoogleApiClient());
        }
    }

    /**
     * Starts requesting API for obtain current location.
     */
    private void startLocationRequest() {
        if (isGoogleApiConnected()) {
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(mLocationRequest);
            PendingResult<LocationSettingsResult> locationSettingsResultPendingResult =
                    LocationServices.SettingsApi.checkLocationSettings(getGoogleApiClient(), builder.build());
            locationSettingsResultPendingResult.setResultCallback(this);
        }
    }

    /**
     * Stops requesting location.
     */
    private void stopLocationRequest() {
        Log.v(TAG, "Stop location request");
        if (isGoogleApiConnected()) {
            Log.v(TAG, "Location request stopped");
            LocationServices.FusedLocationApi.removeLocationUpdates(getGoogleApiClient(), this);
            updateUI();
        }
    }

    private void updateUI() {
        if (mLastLocation != null) {
            mLocationCoordinates.setText(getString(R.string.label_coordinates, mLastLocation.getLatitude(), mLastLocation.getLongitude()));
        } else {
            mLocationCoordinates.setText(R.string.location_not_available);
        }
        mLocationAddresses.setText(mLastLocationAddresses);

        if (mRequestingLocationUpdates) {
            mRequestLocation.setImageResource(R.drawable.map_marker_off);
        } else {
            mRequestLocation.setImageResource(R.drawable.map_marker);
        }
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(LOCATION_REQUEST_INTERVAL);
        mLocationRequest.setFastestInterval(LOCATION_REQUEST_FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void restoreLocationFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mRequestingLocationUpdates = savedInstanceState.getBoolean(KEY_REQUESTING_LOCATION_UPDATES, false);
            mLastLocation = savedInstanceState.getParcelable(KEY_LAST_LOCATION);
            mLastLocationAddresses = savedInstanceState.getString(KEY_LAST_LOCATION_ADDRESSES, "");
        } else {
            mRequestingLocationUpdates = false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_CHECK_SETTINGS == requestCode) {
            Log.v(TAG, "On request check settings: resultCode = " + resultCode);
            switch (resultCode) {
                case Activity.RESULT_OK:
                    Toast.makeText(getActivity(), R.string.settings_satisfied, Toast.LENGTH_SHORT).show();
                    break;
                case Activity.RESULT_CANCELED:
                    Toast.makeText(getActivity(), R.string.settings_not_satisfied, Toast.LENGTH_SHORT).show();
                    getFragmentManager().popBackStack();
                    break;
            }
        }
    }


    @Override
    public void onConnected(Bundle bundle) {
        requestLastLocation();
        if (mRequestingLocationUpdates) {
            startLocationRequest();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public void onConnectionSuspended(int i) {
    }
}
