package com.eficksan.whereami.data.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

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

import rx.Subscriber;
import rx.Subscription;
import rx.subjects.PublishSubject;

/**
 * Created by Aleksei Ivshin
 * on 04.09.2016.
 */
public class LocationRequestingDelegate implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<LocationSettingsResult> {

    private static final String TAG = LocationRequestingDelegate.class.getSimpleName();

    public static final int LOCATION_REQUEST_INTERVAL = 10000;
    public static final int LOCATION_REQUEST_FASTEST_INTERVAL = 5000;
    public static final float LOCATION_REQUEST_SMALLEST_DISPLACEMENT = 10f;

    private final Context context;
    private GoogleApiClient mGoogleApiClient;
    private final LocationRequest mLocationRequest;
    private final DelegateCallback delegateCallback;
    private boolean mIsRequestLocationOnStart = false;

    private final LocationListener mLocationListener;
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

    public LocationRequestingDelegate(Context context, LocationRequest mLocationRequest, DelegateCallback delegateCallback, LocationListener locationListener) {
        this.context = context;
        this.mLocationRequest = mLocationRequest;
        this.delegateCallback = delegateCallback;
        this.mLocationListener = locationListener;
    }

    public void connect() {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
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
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, mLocationListener);
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
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    delegateCallback.onPermissionsRequired(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION});
                    return;
                }
                LocationServices.FusedLocationApi
                        .requestLocationUpdates(mGoogleApiClient, mLocationRequest, mLocationListener);
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                Log.v(TAG, "Location settings require resolution");
                delegateCallback.onSettingsResolutionRequired(result);
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                Log.v(TAG, "Location settings change unavailable");
                delegateCallback.onSettingsChangeUnavailable(result);
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
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            delegateCallback.onPermissionsRequired(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION});
            return;
        }
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        Log.v(TAG, "Last location is " + lastLocation);
        mLocationListener.onLocationChanged(lastLocation);
    }

    public interface DelegateCallback {

        void onPermissionsRequired(String[] permissions);

        void onSettingsResolutionRequired(LocationSettingsResult result);

        void onSettingsChangeUnavailable(LocationSettingsResult result);
    }
}
