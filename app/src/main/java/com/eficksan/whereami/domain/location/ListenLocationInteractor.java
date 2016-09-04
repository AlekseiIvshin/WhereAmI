package com.eficksan.whereami.domain.location;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.eficksan.whereami.data.location.LocationAddress;
import com.eficksan.whereami.data.location.LocationRepository;
import com.eficksan.whereami.data.location.WhereAmILocationService;
import com.eficksan.whereami.domain.Interactor;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

/**
 * Interactor for listening current location.
 * <p/>
 * Created by Aleksei Ivshin
 * on 22.08.2016.
 */
public class ListenLocationInteractor extends Interactor<Long, LocationAddress> {

    private static final String TAG = ListenLocationInteractor.class.getSimpleName();

    private static final long DEFAULT_SECONDS_DELAY = 30;

    /**
     * Interval for request location from service.
     */
    private long mLocationRequestSecondsInterval = DEFAULT_SECONDS_DELAY;

    private Context mContext;

    /**
     * Location channel will dispatch events every N seconds to subscriber.
     */
    private PublishSubject<LocationAddress> mLocationChannel;
    /**
     * Subscription for receiving current location every N seconds from service.
     */
    private Subscription mDataSourceSubscription;
    private boolean mIsServiceConnected = false;

    private ServiceConnection mLocationServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, final IBinder iBinder) {
            mIsServiceConnected = true;
            final LocationRepository locationRepository = ((WhereAmILocationService.LocalBinder) iBinder).getDataSource();
            Log.v(TAG, "Location service was bound");
            mDataSourceSubscription = subscribeOnLocationChanges(locationRepository);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.v(TAG, "Location service was disconnected");
            mIsServiceConnected = false;
            unsubscribeLocationChanges();
        }
    };

    public ListenLocationInteractor(Context context) {
        super(Schedulers.computation(), AndroidSchedulers.mainThread());
        mContext = context;
        mLocationChannel = PublishSubject.create();
    }

    @Override
    protected Observable<LocationAddress> buildObservable(Long parameter) {
        mLocationRequestSecondsInterval = (parameter > 0? parameter : DEFAULT_SECONDS_DELAY);
        startLocationRequest();
        return mLocationChannel;
    }

    @Override
    public void unsubscribe() {
        super.unsubscribe();
        stopLocationRequest();
        mContext = null;
    }

    /**
     * Starts listen location. Bind service and add listener on service connected.
     */
    private void startLocationRequest() {
        Log.v(TAG, "startLocationRequest");
        mContext.bindService(WhereAmILocationService.startTrackLocation(mContext), mLocationServiceConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * Stops listen location changes. Remove all listeners.
     */
    private void stopLocationRequest() {
        Log.v(TAG, "stopLocationRequest");
        if (mDataSourceSubscription != null) {
            mDataSourceSubscription.unsubscribe();
        }
        if (mIsServiceConnected) {
            mContext.unbindService(mLocationServiceConnection);
            mIsServiceConnected = false;
        }
    }

    /**
     * Subscribes on location changes.
     *
     * @param locationRepository location repository
     * @return subscription
     */
    private Subscription subscribeOnLocationChanges(final LocationRepository locationRepository) {
        return Observable.interval(0L, mLocationRequestSecondsInterval, TimeUnit.SECONDS)
                .map(new Func1<Long, LocationAddress>() {
                    @Override
                    public LocationAddress call(Long aLong) {
                        return new LocationAddress(locationRepository.getLocation(), locationRepository.getAddress());
                    }
                })
                .subscribe(new Action1<LocationAddress>() {
                    @Override
                    public void call(LocationAddress location) {
                        if (location != null) {
                            Log.v(TAG, String.format("Location obtain: %s", location.toString()));
                            mLocationChannel.onNext(location);
                        }
                    }
                });
    }

    /**
     * Unsubscribes location changes observer.
     */
    private void unsubscribeLocationChanges() {
        mLocationChannel.onCompleted();
        if (mDataSourceSubscription != null) {
            mDataSourceSubscription.unsubscribe();
            mDataSourceSubscription = null;
        }
    }
}
