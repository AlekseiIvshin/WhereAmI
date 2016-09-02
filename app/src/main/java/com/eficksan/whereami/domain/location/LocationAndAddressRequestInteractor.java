package com.eficksan.whereami.domain.location;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.location.Address;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;

import com.eficksan.whereami.data.location.LocationRepository;
import com.eficksan.whereami.data.location.WhereAmILocationService;
import com.eficksan.whereami.domain.Interactor;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
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
public class LocationAndAddressRequestInteractor extends Interactor<Long, Location> {

    private static final String TAG = LocationAndAddressRequestInteractor.class.getSimpleName();

    private static final long DEFAULT_SECONDS_DELAY = 30;

    @Inject
    Context context;

    /**
     * Interval for request location from service.
     */
    private long mLocationRequestSecondsInterval = DEFAULT_SECONDS_DELAY;

    /**
     * Location channel will dispatch events every N seconds to subscriber.
     */
    private PublishSubject<Location> mLocationChannel;

    /**
     * Address channel will dispatch events every N seconds to subscriber.
     */
    private PublishSubject<Location> mAddressChannel;
    /**
     * Subscription for receiving current location every N seconds from service.
     */
    private Subscription mDataSourceSubscription;

    private boolean mIsServiceConnected = false;

    private ServiceConnection mLocationServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, final IBinder iBinder) {
            Log.v(TAG, "Location service was bound");
            mIsServiceConnected = true;
            final LocationRepository locationDataSource = ((WhereAmILocationService.LocalBinder) iBinder).getDataSource();
            mDataSourceSubscription = subscribeOnLocationChanges(locationDataSource);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.v(TAG, "Location service was disconnected");
            mIsServiceConnected = false;
            unsubscribeLocationChanges();
        }
    };

    public LocationAndAddressRequestInteractor() {
        super(Schedulers.computation(), AndroidSchedulers.mainThread());
    }

    @Override
    public void execute(Long parameter, Subscriber<Location> subscriber) {
        super.execute(parameter, subscriber);
        mLocationRequestSecondsInterval = parameter;
        startLocationRequest();
    }

    @Override
    protected Observable<Location> buildObservable(Long parameter) {
        mLocationChannel = PublishSubject.create();
        return mLocationChannel;
    }

    @Override
    public void unsubscribe() {
        stopLocationRequest();
        unsubscribeLocationChanges();
        super.unsubscribe();
    }

    /**
     * Starts listen location. Bind service and add listener on service connected.
     */
    private void startLocationRequest() {
        Log.v(TAG, "startLocationRequest");
        context.bindService(WhereAmILocationService.startTrackLocation(context), mLocationServiceConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * Stops listen location changes. Remove all listeners.
     */
    private void stopLocationRequest() {
        Log.v(TAG, "stopLocationRequest");
        if (mIsServiceConnected) {
            context.unbindService(mLocationServiceConnection);
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
                .map(new Func1<Long, Location>() {
                    @Override
                    public Location call(Long aLong) {
                        return locationRepository.getLocation();
                    }
                })
                .subscribe(new Action1<Location>() {
                    @Override
                    public void call(Location location) {
                        if (location != null) {
                            Log.v(TAG, String.format("Location obtain: %s", location.toString()));
                            mLocationChannel.onNext(location);
                        }
                    }
                });
    }

    /**
     * Subscribes on location changes.
     *
     * @param locationRepository location repository
     * @return subscription
     */
    private Subscription subscribeOnAddressChanges(final LocationRepository locationRepository) {
        return Observable.interval(0L, mLocationRequestSecondsInterval, TimeUnit.SECONDS)
                .map(new Func1<Long, Location>() {
                    @Override
                    public Location call(Long aLong) {
                        return locationRepository.getLocation();
                    }
                })
                .subscribe(new Action1<Location>() {
                    @Override
                    public void call(Location location) {
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
