package com.eficksan.whereami.googleapi;

/**
 * Created by Aleksei Ivshin
 * on 03.05.2016.
 */
public interface ApiConnectionObservable {

    void registerConnectionObserver(ApiConnectionObserver apiConnectionObservable);

    void unregisterConnectionObserver(ApiConnectionObserver apiConnectionObservable);

    void unregisterAllConnectionObservers();

}
