package com.eficksan.whereami.domain.location;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import java.lang.ref.WeakReference;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by Aleksei Ivshin
 * on 28.08.2016.
 */
public class LocationHistoryInteractor {

    public static final int MSG_RESULT_ERROR = 0;
    public static final int MSG_RESULT_OK = 1;
    public static final String EXTRA_DATA = "EXTRA_DATA";

    public static final int MSG_SAVE_LOCATION = 1;
    public static final int MSG_GET_LAST_LOCATION = 2;
    private static final String TAG = LocationHistoryInteractor.class.getSimpleName();

    private PublishSubject<Location> mLastLocationObservable = PublishSubject.create();

    private final WeakReference<Activity> mRefActivityContext;
    private boolean mIsLocationServiceConnected = false;
    private Messenger mServiceMessenger;

    class ServiceResponseHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_GET_LAST_LOCATION: {
                    if(MSG_RESULT_OK == msg.arg1) {
                        Location location = msg.getData().getParcelable(EXTRA_DATA);
                        mLastLocationObservable.onNext(location);
                    } else {
                        Log.v(TAG, "There are not any locations saved before.");
                    }
                }
                case MSG_SAVE_LOCATION: {
                    if(MSG_RESULT_OK == msg.arg1) {
                        Log.v(TAG, "Location was saved!");
                    } else {
                        Log.v(TAG, "Location was not saved!");
                    }
                }
                default: super.handleMessage(msg);
            }
        }
    }

    final Messenger mResponseHandler = new Messenger(new ServiceResponseHandler());

    private ServiceConnection mLocationServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mServiceMessenger = new Messenger(iBinder);
            mIsLocationServiceConnected = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mServiceMessenger = null;
            mIsLocationServiceConnected = false;
        }
    };

    public LocationHistoryInteractor(Activity activity) {
        this.mRefActivityContext = new WeakReference<Activity>(activity);
    }

    public void onStart() {
        Activity activity= mRefActivityContext.get();
        if (activity!=null) {
            Intent locationServiceIntent = new Intent("com.eficksan.messaging.BIND_LOCATIONS");
            locationServiceIntent.setPackage("com.eficksan.messaging");
            activity.bindService(locationServiceIntent, mLocationServiceConnection, Context.BIND_AUTO_CREATE);
        }
    }

    public void addLocation(Location location) {
        if (mIsLocationServiceConnected) {
            Message message = new Message();
            message.what = MSG_SAVE_LOCATION;
            Bundle data = new Bundle();
            data.putParcelable(EXTRA_DATA, location);
            message.setData(data);
            message.replyTo = mResponseHandler;
            try {
                mServiceMessenger.send(message);
            } catch (RemoteException e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
    }

    public Observable<Location> getLastLocation() {
        if (mIsLocationServiceConnected) {
            Message message = new Message();
            message.what = MSG_GET_LAST_LOCATION;
            message.replyTo = mResponseHandler;
            try {
                mServiceMessenger.send(message);
            } catch (RemoteException e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
        return mLastLocationObservable;
    }

    public void onStop() {
        Activity activity= mRefActivityContext.get();
        if (activity!= null && mIsLocationServiceConnected) {
            activity.unbindService(mLocationServiceConnection);
        }
    }
}
