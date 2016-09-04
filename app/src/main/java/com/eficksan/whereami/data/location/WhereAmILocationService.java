package com.eficksan.whereami.data.location;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.eficksan.whereami.R;
import com.eficksan.whereami.domain.Constants;
import com.eficksan.whereami.presentation.MainActivity;
import com.google.android.gms.location.LocationSettingsResult;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class WhereAmILocationService extends Service implements LocationRepository, LocationRequestDelegate.DelegateCallback {
    private static final String TAG = WhereAmILocationService.class.getSimpleName();

    public static final String KEY_LOCATION = "KEY_LOCATION";
    public static final String KEY_ADDRESS = "KEY_ADDRESS";


    private HandlerThread mComputingThread = new HandlerThread("LocationComputingThread");
    private Handler mUiHandler;
    private Handler mComputingHandler;

    private Handler.Callback callback = new Handler.Callback() {

        @Override
        public boolean handleMessage(Message message) {
            if (message.what == 0) {
                Location location = message.getData().getParcelable(KEY_LOCATION);
                if (location == null) {
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
                    Message nextMessage = new Message();
                    message.what = 1;
                    Bundle data = new Bundle();
                    data.putParcelable(KEY_ADDRESS, addressList.get(0));
                    message.setData(data);
                    mUiHandler.sendMessage(nextMessage);
                }
            } else {
                mLastAddress = message.getData().getParcelable(KEY_ADDRESS);
            }
            return true;
        }
    };


    private BroadcastReceiver mRequirementsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case Constants.ACTION_PERMISSIONS_REQUEST_RESULT:
                case Constants.ACTION_SETTINGS_REQUEST_RESULT:
                    mLocationRequestDelegate.startLocationRequest();
                    break;
            }
        }
    };

    private static final int NOTIFICATION_ID = 42;

    private static final String ACTION_START_FOREGROUND = "ACTION_START_FOREGROUND";
    private static final String ACTION_STOP_FOREGROUND = "ACTION_STOP_FOREGROUND";
    private static final String ACTION_START_TRACK_LOCATION = "ACTION_START_TRACK_LOCATION";

    private Geocoder mGeocoder;
    private Location mLastLocation;
    private Address mLastAddress;

    private final LocalBinder binder = new LocalBinder();
    private LocationRequestDelegate mLocationRequestDelegate;

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
    public static Intent startTrackLocation(Context context) {
        Intent intent = new Intent(context, WhereAmILocationService.class);
        intent.setAction(ACTION_START_TRACK_LOCATION);
        return intent;
    }

    public WhereAmILocationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mLocationRequestDelegate = new LocationRequestDelegate(getApplicationContext(), this);
        mLocationRequestDelegate.connect();
        mGeocoder = new Geocoder(this, Locale.getDefault());

        mUiHandler = new Handler(callback);
        mComputingThread.start();
        mComputingHandler = new Handler(mComputingThread.getLooper(), callback);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.ACTION_PERMISSIONS_REQUEST_RESULT);
        intentFilter.addAction(Constants.ACTION_SETTINGS_REQUEST_RESULT);
        LocalBroadcastManager.getInstance(this).registerReceiver(mRequirementsReceiver, intentFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handleCommand(intent);

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        mLocationRequestDelegate.stopLocationRequest();
        mLocationRequestDelegate.disconnect();
        mComputingHandler.removeCallbacksAndMessages(null);
        mUiHandler.removeCallbacksAndMessages(null);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRequirementsReceiver);
        super.onDestroy();
    }

    @Override
    public void onPermissionsRequired(String[] permissions) {
        MainActivity.requestPermissions(this, permissions);
    }

    @Override
    public void onSettingsResolutionRequired(LocationSettingsResult result) {
        Toast.makeText(this, R.string.settings_not_satisfied, Toast.LENGTH_SHORT).show();
        MainActivity.requestSettings(this, result.getStatus());
    }

    @Override
    public void onSettingsChangeUnavailable(LocationSettingsResult result) {
        Toast.makeText(this, R.string.settings_change_unavailable, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.v(TAG, String.format("Location changed: %1$.4f x %2$.4f", location.getLatitude(), location.getLongitude()));
        mLastLocation = location;
        updateAddresses();
    }

    @Override
    public Location getLocation() {
        return mLastLocation;
    }

    @Override
    public Address getAddress() {
        return mLastAddress;
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
            }
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
        stopSelf();
    }
}
