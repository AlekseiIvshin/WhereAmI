package com.eficksan.whereami.data.location;

import android.location.Location;

import com.google.android.gms.location.LocationRequest;

import java.util.Observable;

import rx.Subscriber;

/**
 * Created by Aleksei_Ivshin on 9/5/16.
 */
public interface LocationDataSource {

    void subscribe(LocationRequest locationRequest, Subscriber<Location> locationSubscriber);

    void unsubscribe();

}
