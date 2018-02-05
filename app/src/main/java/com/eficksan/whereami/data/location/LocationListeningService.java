package com.eficksan.whereami.data.location;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.eficksan.whereami.R;
import com.eficksan.whereami.domain.Constants;
import com.eficksan.whereami.presentation.MainActivity;
import com.google.android.gms.location.LocationSettingsResult;

import rx.Subscriber;
import rx.observables.ConnectableObservable;
import rx.subjects.PublishSubject;

public class LocationListeningService
        extends Service
        implements LocationDataSource, LocationRequestingDelegate.DelegateCallback {
    private static final String TAG = LocationListeningService.class.getSimpleName();

    private BroadcastReceiver mRequirementsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case Constants.ACTION_PERMISSIONS_REQUEST_RESULT:
                case Constants.ACTION_SETTINGS_REQUEST_RESULT:
                    mLocationRequestDelegate.startLocationRequest();
                    break;
                default: Log.v(TAG, "There is an unhandled broadcast action: " + action);
            }
        }
    };

    private static final int NOTIFICATION_ID = 42;

    private static final String ACTION_START_FOREGROUND = "ACTION_START_FOREGROUND";
    private static final String ACTION_STOP_FOREGROUND = "ACTION_STOP_FOREGROUND";
    private static final String ACTION_START_TRACK_LOCATION = "ACTION_START_TRACK_LOCATION";

    private final LocalBinder binder = new LocalBinder();

    private LocationRequestingDelegate mLocationRequestDelegate;
    private PublishSubject<Location> locationPublishSubject;
    private ConnectableObservable<Location> locationChannel;
    private int mSubscriptionsCount;

    /**
     * Creates intent for commanding service to work in foreground.
     *
     * @param context some kind of context
     * @return intent
     */
    public static Intent startForeground(Context context) {
        Intent intent = new Intent(context, LocationListeningService.class);
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
        Intent intent = new Intent(context, LocationListeningService.class);
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
        Intent intent = new Intent(context, LocationListeningService.class);
        intent.setAction(ACTION_START_TRACK_LOCATION);
        return intent;
    }

    public LocationListeningService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mLocationRequestDelegate = new LocationRequestingDelegate(
                getApplicationContext(),
                LocationRequestingDelegate.createIntervalLocationRequest(),
                this);

        locationPublishSubject = PublishSubject.create();
        locationChannel = locationPublishSubject.publish();
        locationChannel.connect();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.ACTION_PERMISSIONS_REQUEST_RESULT);
        intentFilter.addAction(Constants.ACTION_SETTINGS_REQUEST_RESULT);
        LocalBroadcastManager.getInstance(this).registerReceiver(mRequirementsReceiver, intentFilter);

        mLocationRequestDelegate.connect();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handleCommand(intent);

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        locationChannel.refCount();
        mLocationRequestDelegate.disconnect();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRequirementsReceiver);
        super.onDestroy();
    }

    @Override
    public void onPermissionsRequired(String[] permissions) {
        try {
            MainActivity.requestPermissions(
                    this,
                    permissions,
                    Constants.ACTION_PERMISSIONS_REQUEST_RESULT).send();
        } catch (PendingIntent.CanceledException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    @Override
    public void onSettingsResolutionRequired(LocationSettingsResult result) {
        Toast.makeText(this, R.string.settings_not_satisfied, Toast.LENGTH_SHORT).show();
        try {
            MainActivity.requestSettings(
                    this,
                    result.getStatus(),
                    Constants.ACTION_SETTINGS_REQUEST_RESULT).send();
        } catch (PendingIntent.CanceledException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    @Override
    public void onSettingsChangeUnavailable(LocationSettingsResult result) {
        Toast.makeText(this, R.string.settings_change_unavailable, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void subscribe(Subscriber<Location> subscriber) {
        locationChannel.subscribe(subscriber);
        mSubscriptionsCount++;
        Log.v(TAG, "New location subscriber. Current count = " + mSubscriptionsCount);
        mLocationRequestDelegate.startLocationRequest();
    }

    @Override
    public void unsubscribe(Subscriber<Location> subscriber) {
        subscriber.unsubscribe();
        mSubscriptionsCount--;
        Log.v(TAG, "Unsubscribed. Current count = " + mSubscriptionsCount);
        if (mSubscriptionsCount == 0) {
            Log.v(TAG, "Subscribers count is 0. Stop location requesting");
            mLocationRequestDelegate.stopLocationRequest();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        locationPublishSubject.onNext(location);
    }

    public class LocalBinder extends Binder {
        public LocationDataSource getDataSource() {
            return LocationListeningService.this;
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
                default: Log.v(TAG, "There is an unhandled command action: " + action);
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
