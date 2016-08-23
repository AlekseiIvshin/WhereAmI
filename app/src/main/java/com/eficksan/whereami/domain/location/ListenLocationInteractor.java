package com.eficksan.whereami.domain.location;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.IBinder;

import com.eficksan.whereami.domain.Interactor;
import com.eficksan.whereami.data.location.LocationDataSource;
import com.eficksan.whereami.data.location.WaiEvent;
import com.eficksan.whereami.data.location.WhereAmILocationService;

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
 *
 * Created by Aleksei Ivshin
 * on 22.08.2016.
 */
public class ListenLocationInteractor extends Interactor<WaiEvent, Long> {

    private static final long DEFAULT_SECONDS_DELAY = 30;
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

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        public long secondsDelay = DEFAULT_SECONDS_DELAY;

        @Override
        public void onServiceConnected(ComponentName componentName, final IBinder iBinder) {
            mIsServiceConnected = true;
            final LocationDataSource locationDataSource = ((WhereAmILocationService.LocalBinder) iBinder).getDataSource();
            mDataSourceSubscription = Observable.interval(secondsDelay, TimeUnit.SECONDS)
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object o) {
                            Location location = locationDataSource.getLocation();
                            List<String> addresses = locationDataSource.getAddresses();
                            mLocationChannel.onNext(new WaiEvent(location, addresses));
                        }
                    });
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
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
     * @param secondsDelay delay between location requesting.
     */
    private void startLocationRequest(long secondsDelay) {
        Activity activity = mRefActivityContext.get();
        if (activity != null) {
            activity.bindService(WhereAmILocationService.startService(activity), mServiceConnection, Context.BIND_AUTO_CREATE);
        }
    }

    /**
     * Stops listen location changes. Remove all listeners.
     */
    private void stopLocationRequest() {
        if (mDataSourceSubscription != null) {
            mDataSourceSubscription.unsubscribe();
        }
        Activity activity = mRefActivityContext.get();
        if (activity != null && mIsServiceConnected) {
            activity.unbindService(mServiceConnection);
            mIsServiceConnected = false;
        }
    }
}
