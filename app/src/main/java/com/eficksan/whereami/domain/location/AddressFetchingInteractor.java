package com.eficksan.whereami.domain.location;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import com.eficksan.whereami.domain.BaseInteractor;

import java.io.IOException;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Interactor for listening current location.
 * <p/>
 * Created by Aleksei Ivshin
 * on 22.08.2016.
 */
public class AddressFetchingInteractor extends BaseInteractor<Location, Address> {

    private static final String TAG = AddressFetchingInteractor.class.getSimpleName();
    private final Geocoder geocoder;

    public AddressFetchingInteractor(Geocoder geocoder) {
        super(Schedulers.computation(), AndroidSchedulers.mainThread());
        this.geocoder = geocoder;

    }

    @Override
    protected Observable<Address> buildObservable(Location location) {
        return Observable.just(location)
                .subscribeOn(jobScheduler)
                .map(new Func1<Location, Address>() {
                    @Override
                    public Address call(Location location) {
                        if (location == null) {
                            return null;
                        }
                        List<Address> addressList = null;
                        try {
                            addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        } catch (IOException e) {
                            //TODO: notify about error
                            Log.e(TAG, e.getMessage(), e);
                        } catch (IllegalArgumentException e) {
                            //TODO: notify about error
                            Log.e(TAG, e.getMessage() + ". Location: " + location, e);
                        }
                        if (addressList != null && addressList.size() > 0) {
                            return addressList.get(0);
                        }
                        return null;
                    }
                });
    }
}
