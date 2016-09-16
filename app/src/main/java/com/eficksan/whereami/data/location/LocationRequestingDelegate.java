package com.eficksan.whereami.data.location;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.eficksan.whereami.R;
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

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.support.v4.content.ContextCompat.checkSelfPermission;

/**
 * Created by Aleksei Ivshin
 * on 04.09.2016.
 */
public class LocationRequestingDelegate
        implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        ResultCallback<LocationSettingsResult> {

    private static final String TAG = LocationRequestingDelegate.class.getSimpleName();

    public static final int LOCATION_REQUEST_INTERVAL = 10000;
    public static final int LOCATION_REQUEST_FASTEST_INTERVAL = 5000;
    public static final float LOCATION_REQUEST_SMALLEST_DISPLACEMENT = 10f;

    private final Context mContext;
    private GoogleApiClient mGoogleApiClient;
    private final LocationRequest mLocationRequest;
    private final DelegateCallback mDelegateCallback;
    private boolean mIsRequestLocationOnStart = false;
    private boolean mIsRequesting = false;

    public static LocationRequest createIntervalLocationRequest() {
        LocationRequest request = new LocationRequest();
        request.setInterval(LOCATION_REQUEST_INTERVAL);
        request.setSmallestDisplacement(LOCATION_REQUEST_SMALLEST_DISPLACEMENT);
        request.setFastestInterval(LOCATION_REQUEST_FASTEST_INTERVAL);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        Log.v(TAG, "Creates request oriented on interval: " + request.toString());
        return request;
    }

    public LocationRequestingDelegate(Context context, LocationRequest locationRequest, DelegateCallback callback) {
        this.mContext = context;
        this.mLocationRequest = locationRequest;
        this.mDelegateCallback = callback;
    }

    public void connect() {
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();
    }

    public void disconnect() {
        mGoogleApiClient.disconnect();
    }

    public void startLocationRequest() {
        if (mIsRequesting) {
            return;
        }
        getLastLocationRequest();
        mIsRequesting = true;
        Log.v(TAG, "Start location request");
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            Log.v(TAG, "Google client is connected");
            checkSettingsAndRequestLocation();
            mIsRequestLocationOnStart = false;
        } else {
            Log.v(TAG, "Google client is not connected. Postpone requesting to client connection");
            mIsRequestLocationOnStart = true;
        }
    }

    /**
     * Stops requesting location.
     */
    public void stopLocationRequest() {
        mIsRequesting = false;
        Log.v(TAG, "Stop location request");
        if (mGoogleApiClient.isConnected()) {
            Log.v(TAG, "Location request stopped");
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, mDelegateCallback);
        }
    }

    private void checkSettingsAndRequestLocation() {
        Log.v(TAG, "Check settings for location request: " + mLocationRequest.toString());
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        PendingResult<LocationSettingsResult> locationSettingsResultPendingResult =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        locationSettingsResultPendingResult.setResultCallback(this);

    }

    @Override
    public void onResult(@NonNull LocationSettingsResult result) {
        final Status status = result.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                Log.v(TAG, "Location settings are satisfied");
                Log.v(TAG, "Location requested: " + mLocationRequest.toString());
                if (checkSelfPermission(mContext, ACCESS_FINE_LOCATION) != PERMISSION_GRANTED) {
                    mDelegateCallback.onPermissionsRequired(new String[]{ACCESS_FINE_LOCATION});
                    return;
                }
                if (mGoogleApiClient !=null && mGoogleApiClient.isConnected()) {
                    LocationServices.FusedLocationApi
                            .requestLocationUpdates(mGoogleApiClient, mLocationRequest, mDelegateCallback);
                } else {
                    // TODO: notify user about problem
                }
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                Log.v(TAG, "Location settings require resolution");
                mDelegateCallback.onSettingsResolutionRequired(result);
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                Log.v(TAG, "Location settings change unavailable");
                mDelegateCallback.onSettingsChangeUnavailable(result);
                break;
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.v(TAG, "GoogleApiClient connected");
        getLastLocationRequest();
        if (mIsRequestLocationOnStart) {
            checkSettingsAndRequestLocation();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.v(TAG, "GoogleApiClient suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.v(TAG, "GoogleApiClient connection failed");
        //TODO: Notify user about problem
    }

    private void getLastLocationRequest() {
        Log.v(TAG, "Get last location");
        if (checkSelfPermission(mContext, ACCESS_FINE_LOCATION) != PERMISSION_GRANTED) {
            mDelegateCallback.onPermissionsRequired(
                    new String[]{ACCESS_FINE_LOCATION});
            return;
        }
        Location lastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);

        Log.v(TAG, "Last location is " + lastLocation);
        mDelegateCallback.onLocationChanged(lastLocation);
    }

    public interface DelegateCallback extends LocationListener {

        void onPermissionsRequired(String[] permissions);

        void onSettingsResolutionRequired(LocationSettingsResult result);

        void onSettingsChangeUnavailable(LocationSettingsResult result);
    }
}
