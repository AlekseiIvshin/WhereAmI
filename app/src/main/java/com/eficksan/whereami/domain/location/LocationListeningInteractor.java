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
import com.eficksan.whereami.domain.Interactor;
import com.google.android.gms.location.LocationRequest;

import rx.Subscriber;

/**
 * Interactor for listening current location.
 * <p/>
 * Created by Aleksei Ivshin
 * on 22.08.2016.
 */
public class LocationListeningInteractor implements Interactor<LocationRequest, Location> {

    private static final String TAG = LocationListeningInteractor.class.getSimpleName();

    private Context mContext;

    private boolean mIsServiceConnected = false;

    private LocationDataSource locationDataSource;
    private LocationRequest mLocationRequest;
    private Subscriber<Location> mLocationSubscriber;
    private ServiceConnection mLocationServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, final IBinder iBinder) {
            mIsServiceConnected = true;

            locationDataSource = ((LocationListeningService.LocalBinder) iBinder).getDataSource();
            locationDataSource.subscribe(mLocationRequest, mLocationSubscriber);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.v(TAG, "Location service was disconnected");
            mIsServiceConnected = false;
            locationDataSource.unsubscribe();
            locationDataSource = null;
        }
    };

    @Override
    public void execute(@NonNull LocationRequest parameter,@NonNull Subscriber<Location> subscriber) {
        mLocationRequest = parameter;
        mLocationSubscriber = subscriber;
        startLocationRequest();
    }

    public LocationListeningInteractor(Context context) {
        mContext = context;
    }

    @Override
    public void unsubscribe() {
        if (locationDataSource!=null) {
            locationDataSource.unsubscribe();
        }
        stopLocationRequest();
        mContext = null;
    }

    /**
     * Starts listen location. Bind service and add listener on service connected.
     */
    private void startLocationRequest() {
        Log.v(TAG, "startLocationRequest");
        mContext.bindService(LocationListeningService.startTrackLocation(mContext), mLocationServiceConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * Stops listen location changes. Remove all listeners.
     */
    private void stopLocationRequest() {
        Log.v(TAG, "stopLocationRequest");
        if (mIsServiceConnected) {
            mContext.unbindService(mLocationServiceConnection);
            mIsServiceConnected = false;
        }
    }
}
