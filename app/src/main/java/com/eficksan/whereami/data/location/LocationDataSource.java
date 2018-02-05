package com.eficksan.whereami.data.location;

import android.location.Location;

import rx.Subscriber;

/**
 * Location data source provides method for subscribing on location changes.
 *
 * Created by Aleksei_Ivshin on 9/5/16.
 */
public interface LocationDataSource {

    /**
     * Subscribe on location changes.
     * @param subscriber subscriber
     */
    void subscribe(Subscriber<Location> subscriber);

    /**
     * Unsubscribe from location changes.
     */
    void unsubscribe(Subscriber<Location> subscriber);

}
