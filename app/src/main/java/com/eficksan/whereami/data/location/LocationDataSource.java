package com.eficksan.whereami.data.location;

import android.location.Location;

import com.google.android.gms.location.LocationRequest;

import rx.Subscriber;

/**
 * Location data source provides method for subscribing on location changes.
 *
 * Created by Aleksei_Ivshin on 9/5/16.
 */
public interface LocationDataSource {

    /**
     * Subscribe on location changes.
     * @param locationRequest location request
     * @param subscriber subscriber
     */
    void subscribe(LocationRequest locationRequest, Subscriber<Location> subscriber);

    /**
     * Unsubscribe from location changes.
     */
    void unsubscribe();

}
