package com.eficksan.whereami.domain.location;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.util.Log;

import com.eficksan.whereami.data.location.LocationDataSource;
import com.eficksan.whereami.data.location.LocationListeningService;

import rx.Subscriber;

/**
 * Interactor for listening current location.
 * <p/>
 * Created by Aleksei Ivshin
 * on 22.08.2016.
 */
public class LocationListeningInteractor {

    private static final String TAG = LocationListeningInteractor.class.getSimpleName();

    private final Context mContext;

    private boolean mIsServiceConnected = false;
    private boolean mIsNeedRequestLocation = true;

    private LocationDataSource locationDataSource;
    private Subscriber<Location> mLocationSubscriber;
    private ServiceConnection mLocationServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, final IBinder iBinder) {
            mIsServiceConnected = true;

            if (mIsNeedRequestLocation) {
                locationDataSource = ((LocationListeningService.LocalBinder) iBinder).getDataSource();
                locationDataSource.subscribe(mLocationSubscriber);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.v(TAG, "Location service was disconnected");
            mIsServiceConnected = false;
            locationDataSource.unsubscribe(mLocationSubscriber);
            locationDataSource = null;
        }
    };

    public LocationListeningInteractor(Context context) {
        mContext = context;
    }

    public void execute(@NonNull Subscriber<Location> subscriber) {
        mLocationSubscriber = subscriber;
        bindLocationRequestingService();
    }

    public void unsubscribe() {
        if (locationDataSource != null && !mLocationSubscriber.isUnsubscribed()) {
            locationDataSource.unsubscribe(mLocationSubscriber);
        }
        unbindLocationRequestingService();
    }

    /**
     * Starts listen location. Bind service and add listener on service connected.
     */
    private void bindLocationRequestingService() {
        Log.v(TAG, "bindLocationRequestingService");
        mIsNeedRequestLocation = true;
        mContext.bindService(
                LocationListeningService.startTrackLocation(mContext),
                mLocationServiceConnection,
                Context.BIND_AUTO_CREATE);
    }

    /**
     * Stops listen location changes. Remove all listeners.
     */
    private void unbindLocationRequestingService() {
        Log.v(TAG, "unbindLocationRequestingService");
        mIsNeedRequestLocation = false;
        if (mIsServiceConnected) {
            mContext.unbindService(mLocationServiceConnection);
            mIsServiceConnected = false;
        }
    }
}
