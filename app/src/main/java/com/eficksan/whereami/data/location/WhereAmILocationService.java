package com.eficksan.whereami.data.location;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.eficksan.whereami.R;
import com.eficksan.whereami.presentation.MainActivity;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WhereAmILocationService extends Service implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<LocationSettingsResult>, LocationRepository {
    private static final String TAG = WhereAmILocationService.class.getSimpleName();

    public static final String KEY_LOCATION = "KEY_LOCATION";
    public static final String KEY_ADDRESSES = "KEY_ADDRESSES";


    private HandlerThread mComputingThread = new HandlerThread("LocationComputingThread");
    private Handler mUiHandler;
    private Handler mComputingHandler;

    private Handler.Callback callback = new Handler.Callback() {

        @Override
        public boolean handleMessage(Message message) {
            if (message.what == 0) {
                Location location = message.getData().getParcelable(KEY_LOCATION);
                if (location== null) {
                    return true;
                }
                List<Address> addressList = null;
                try {
                    addressList = mGeocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                } catch (IOException e) {
                    //TODO: notify about error
//            errorMessage = getString(R.string.service_not_available);
                    Log.e(TAG, e.getMessage(), e);
                } catch (IllegalArgumentException e) {
                    //TODO: notify about error
//            errorMessage = getString(R.string.wrong_lat_lon_used);
                    Log.e(TAG, e.getMessage() + ". Location: " + location, e);
                }
                if (addressList != null && !addressList.isEmpty()) {
                    ArrayList<String> addressFragments = new ArrayList<>();
                    Address address = addressList.get(0);
                    for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                        addressFragments.add(address.getAddressLine(i));
                    }
                    mAddresses = addressFragments;
                    Message nextMessage = new Message();
                    message.what = 1;
                    Bundle data = new Bundle();
                    data.putStringArrayList(KEY_ADDRESSES, addressFragments);
                    message.setData(data);
                    mUiHandler.sendMessage(nextMessage);
                }
            } else {
                mAddresses = message.getData().getStringArrayList(KEY_ADDRESSES);
            }
            return true;
        }
    };

    private static final int NOTIFICATION_ID = 42;

    public static final int LOCATION_REQUEST_INTERVAL = 10000;
    public static final int LOCATION_REQUEST_FASTEST_INTERVAL = 5000;

    private static final String ACTION_START_FOREGROUND = "ACTION_START_FOREGROUND";
    private static final String ACTION_STOP_FOREGROUND = "ACTION_STOP_FOREGROUND";
    private static final String ACTION_START_TRACK_LOCATION = "ACTION_START_TRACK_LOCATION";
    private static final String ACTION_STOP_TRACK_LOCATION = "ACTION_STOP_TRACK_LOCATION";
    private static final String ACTION_LAST_LOCATION = "ACTION_LAST_LOCATION";

    private GoogleApiClient mGoogleApiClient;
    private Geocoder mGeocoder;
    private boolean mIsApiClientConnected = false;
    private Location mLastLocation;
    private ArrayList<String> mAddresses;
    private LocationRequest mLocationRequest;
    private final LocalBinder binder = new LocalBinder();

    /**
     * Creates intent for commanding service to work in foreground.
     *
     * @param context some kind of context
     * @return intent
     */
    public static Intent startForeground(Context context) {
        Intent intent = new Intent(context, WhereAmILocationService.class);
        intent.setAction(ACTION_START_FOREGROUND);
        return intent;
    }

    /**
     * Creates intent for commanding service to stop work in foreground.
     *
     * @param context some kind of context
     * @return intent
     */
    public static Intent stopForeground(Context context) {
        Intent intent = new Intent(context, WhereAmILocationService.class);
        intent.setAction(ACTION_STOP_FOREGROUND);
        return intent;
    }

    /**
     * Creates intent for commanding service to start request location.
     *
     * @param context some kind of context
     * @return intent
     */
    public static Intent startService(Context context) {
        return new Intent(context, WhereAmILocationService.class);
    }

    /**
     * Creates intent for commanding service to start request location.
     *
     * @param context some kind of context
     * @return intent
     */
    public static Intent startTrackLocation(Context context) {
        Intent intent = new Intent(context, WhereAmILocationService.class);
        intent.setAction(ACTION_START_TRACK_LOCATION);
        return intent;
    }

    /**
     * Creates intent for commanding service to start request location.
     *
     * @param context some kind of context
     * @return intent
     */
    public static Intent getLastLocation(Context context) {
        Intent intent = new Intent(context, WhereAmILocationService.class);
        intent.setAction(ACTION_LAST_LOCATION);
        return intent;
    }

    /**
     * Creates intent for commanding service to stop request location.
     *
     * @param context some kind of context
     * @return intent
     */
    public static Intent stopTrackLocation(Context context) {
        Intent intent = new Intent(context, WhereAmILocationService.class);
        intent.setAction(ACTION_STOP_TRACK_LOCATION);
        return intent;
    }

    public static LocationRequest createLocationRequest() {
        LocationRequest request = new LocationRequest();
        request.setInterval(LOCATION_REQUEST_INTERVAL);
        request.setFastestInterval(LOCATION_REQUEST_FASTEST_INTERVAL);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return request;
    }

    public WhereAmILocationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        handleCommand(intent);
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mGeocoder = new Geocoder(this, Locale.getDefault());
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();

        mUiHandler = new Handler(callback);
        mComputingThread.start();
        mComputingHandler = new Handler(mComputingThread.getLooper(), callback);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handleCommand(intent);

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        mComputingHandler.removeCallbacksAndMessages(null);
        mUiHandler.removeCallbacksAndMessages(null);
        mGoogleApiClient.disconnect();
        mGoogleApiClient = null;
        super.onDestroy();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.v(TAG, "GoogleApiClient connected");
        mIsApiClientConnected = true;

        mLocationRequest = createLocationRequest();
        getLastLocationRequest();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.v(TAG, "GoogleApiClient suspended");
        mIsApiClientConnected = false;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.v(TAG, "GoogleApiClient connection failed");
        mIsApiClientConnected = false;
        //TODO: Notify user about problem
    }

    @Override
    public void onResult(@NonNull LocationSettingsResult result) {
        final Status status = result.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                Log.v(TAG, "Location settings are satisfied");
                Log.v(TAG, "Location requested: " + mLocationRequest.toString());
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(startTrackLocation(this));
                    return;
                }
                LocationServices.FusedLocationApi
                        .requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                Log.v(TAG, "Location settings are required");
                Toast.makeText(this, R.string.settings_not_satisfied, Toast.LENGTH_SHORT).show();
                try {
                    PendingIntent pendingIntent = PendingIntent.getService(this, 0, startService(this), 0);
                    MainActivity.requestSettings(this, status, pendingIntent).send();
                } catch (PendingIntent.CanceledException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                Log.v(TAG, "Location settings are required, but cannot be changed");
                // Location settings are not satisfied. However, we have no way
                // to fix the settings so we won't show the dialog.
                Toast.makeText(this, R.string.settings_change_unavailable, Toast.LENGTH_SHORT).show();
                break;
        }
    }
    @Override
    public void onLocationChanged(Location location) {
        Log.v(TAG, String.format("Location changed: %1$.4f x %2$.4f", location.getLatitude(), location.getLongitude()));
        mLastLocation = location;
        updateAddresses();


        //TODO: Use best practices for best location
    }

    private void handleCommand(Intent intent) {
        final String action = intent.getAction();
        if (action != null) {
            switch (action) {
                case ACTION_START_FOREGROUND:
                    startForegroundWAI();
                    break;
                case ACTION_STOP_FOREGROUND:
                    stopForegroundWAI();
                    break;
                case ACTION_STOP_TRACK_LOCATION:
                    stopLocationRequest();
                    break;
                case ACTION_START_TRACK_LOCATION:
                    startLocationRequest();
                    break;
                case ACTION_LAST_LOCATION:
                    getLastLocationRequest();
                    break;
            }
        }
    }

    /**
     * Requests permissions.
     */
    private void requestPermissions(Intent intent) {
        try {
            PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, 0);
            MainActivity.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, pendingIntent).send();
        } catch (PendingIntent.CanceledException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    /**
     * Start service foreground.
     */
    private void startForegroundWAI() {
        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.eye)
                .setContentTitle(getString(R.string.notification_title))
                .setContentText(getString(R.string.notification_text))
                .setContentIntent(MainActivity.showLocationScreen(this))
                .build();
        startForeground(NOTIFICATION_ID, notification);
    }

    /**
     * Stop service foreground.
     */
    private void stopForegroundWAI() {
        stopForeground(true);
    }

    /**
     * Stops requesting location.
     */
    private void stopLocationRequest() {
        Log.v(TAG, "Stop location request");
        if (mIsApiClientConnected) {
            Log.v(TAG, "Location request stopped");
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    private void startLocationRequest() {
        checkSettings();
    }

    private void getLastLocationRequest() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(getLastLocation(this));
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        updateAddresses();

        checkSettings();
    }

    @Override
    public Location getLocation() {
        return mLastLocation;
    }

    @Override
    public List<String> getAddresses() {
        return mAddresses;
    }

    public class LocalBinder extends Binder {
        public LocationRepository getDataSource() {
            return WhereAmILocationService.this;
        }
    }

    public void updateAddresses() {
        if (mLastLocation != null) {
            Message message = new Message();
            message.what = 0;
            Bundle data = new Bundle();
            data.putParcelable(KEY_LOCATION, mLastLocation);
            message.setData(data);
            mComputingHandler.sendMessage(message);
        }
    }

    private void checkSettings() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        PendingResult<LocationSettingsResult> locationSettingsResultPendingResult =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        locationSettingsResultPendingResult.setResultCallback(this);
    }
}
