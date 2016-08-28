package com.eficksan.whereami.domain.location;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;

import com.eficksan.whereami.data.location.LocationRepository;
import com.eficksan.whereami.data.location.WaiEvent;
import com.eficksan.whereami.data.location.WhereAmILocationService;
import com.eficksan.whereami.domain.Interactor;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

/**
 * Interactor for listening current location.
 * <p/>
 * Created by Aleksei Ivshin
 * on 22.08.2016.
 */
public class ListenLocationInteractor extends Interactor<Long, WaiEvent> {

    private static final long DEFAULT_SECONDS_DELAY = 30;
    private static final String TAG = ListenLocationInteractor.class.getSimpleName();
    private final WeakReference<Activity> mRefActivityContext;
    /**
     * Location channel will dispatch events every N seconds to subscriber.
     */
    private PublishSubject<WaiEvent> mLocationChannel;
    /**
     * Subscription for receiving current location every N seconds from service.
     */
    private Subscription mDataSourceSubscription;
    private boolean mIsServiceConnected = false;

    private ServiceConnection mLocationServiceConnection = new ServiceConnection() {
        public long secondsDelay = DEFAULT_SECONDS_DELAY;

        @Override
        public void onServiceConnected(ComponentName componentName, final IBinder iBinder) {
            mIsServiceConnected = true;
            final LocationRepository locationDataSource = ((WhereAmILocationService.LocalBinder) iBinder).getDataSource();
            Log.v(TAG, "Location service was bound");
            mDataSourceSubscription = Observable.interval(secondsDelay, TimeUnit.SECONDS)
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object o) {
                            Location location = locationDataSource.getLocation();
                            if (location != null) {
                                List<String> addresses = locationDataSource.getAddresses();
                                Log.v(TAG, String.format("Location obtain: %s", location.toString()));
                                Log.v(TAG, String.format("Address obtain: %s", addresses.toString()));
                                mLocationChannel.onNext(new WaiEvent(location, addresses));
                            }
                        }
                    });
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.v(TAG, "Location service was disconnected");
            mIsServiceConnected = false;
            mLocationChannel.onCompleted();
            if (mDataSourceSubscription != null) {
                mDataSourceSubscription.unsubscribe();
            }
        }
    };

    public ListenLocationInteractor(Activity activity) {
        super(Schedulers.computation(), AndroidSchedulers.mainThread());
        mLocationChannel = PublishSubject.create();
        mRefActivityContext = new WeakReference<>(activity);
    }

    @Override
    protected Observable<WaiEvent> buildObservable(Long parameter) {
        startLocationRequest(parameter);
        return mLocationChannel;
    }

    @Override
    public void unsubscribe() {
        super.unsubscribe();
        stopLocationRequest();
    }

    /**
     * Starts listen location. Bind service and add listener on service connected.
     *
     * @param secondsDelay delay between location requesting.
     */
    private void startLocationRequest(long secondsDelay) {
        Log.v(TAG, "startLocationRequest");
        Activity activity = mRefActivityContext.get();
        if (activity != null) {
            activity.bindService(WhereAmILocationService.startService(activity), mLocationServiceConnection, Context.BIND_AUTO_CREATE);
        }
    }

    /**
     * Stops listen location changes. Remove all listeners.
     */
    private void stopLocationRequest() {
        Log.v(TAG, "stopLocationRequest");
        if (mDataSourceSubscription != null) {
            mDataSourceSubscription.unsubscribe();
        }
        Activity activity = mRefActivityContext.get();
        if (activity != null && mIsServiceConnected) {
            activity.unbindService(mLocationServiceConnection);
            mIsServiceConnected = false;
        }
    }
}
