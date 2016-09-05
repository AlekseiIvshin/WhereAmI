package com.eficksan.whereami.presentation.maps;

import android.location.Location;

import com.eficksan.whereami.data.location.LocationRequestDelegate;
import com.eficksan.whereami.domain.location.LocationListeningInteractor;
import com.eficksan.whereami.presentation.routing.Router;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by Aleksei_Ivshin on 9/5/16.
 */
public class MapMessagesPresenter {

    @Inject
    Router router;

    @Inject
    LocationListeningInteractor locationListeningInteractor;

    private MapMessagesView mMapMessagesView;

    private Subscriber<Location> locationSubscriber = new Subscriber<Location>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(Location location) {
            mMapMessagesView.moveMapTo(location);
        }
    };

    public void setView(MapMessagesView mapMessagesView) {
        this.mMapMessagesView = mapMessagesView;
    }

    public void onCreate() {
        locationListeningInteractor.execute(LocationRequestDelegate.createDefaultLocationRequest(), locationSubscriber);
    }

    public void onDestroy() {
        locationListeningInteractor.unsubscribe();
    }
}
