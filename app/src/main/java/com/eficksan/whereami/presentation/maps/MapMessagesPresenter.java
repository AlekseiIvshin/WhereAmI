package com.eficksan.whereami.presentation.maps;

import android.location.Location;
import android.os.Bundle;

import com.eficksan.whereami.R;
import com.eficksan.whereami.data.location.LocationRequestDelegate;
import com.eficksan.whereami.data.messages.PlacingMessage;
import com.eficksan.whereami.domain.Constants;
import com.eficksan.whereami.domain.location.LocationListeningInteractor;
import com.eficksan.whereami.domain.messages.MessagesFetchingInteractor;
import com.eficksan.whereami.presentation.routing.Router;
import com.eficksan.whereami.presentation.routing.Screens;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Presenter for maps.
 */
public class MapMessagesPresenter implements MapMessagesView.MapMessageClickListener {

    @Inject
    Router router;

    @Inject
    LocationListeningInteractor locationListeningInteractor;

    @Inject
    MessagesFetchingInteractor messagesFetchingInteractor;

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
            messagesFetchingInteractor.execute(location);
        }
    };

    private Subscriber<List<PlacingMessage>> messagesSubscriber = new Subscriber<List<PlacingMessage>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            mMapMessagesView.showError(R.string.error_maps_fetching_messages);
        }

        @Override
        public void onNext(List<PlacingMessage> messages) {
            mMapMessagesView.showMessages(messages);
        }
    };

    public void setView(MapMessagesView mapMessagesView) {
        this.mMapMessagesView = mapMessagesView;
        mMapMessagesView.setMessageClickListener(this);
    }

    public void onStart() {
        messagesFetchingInteractor.subscribe(messagesSubscriber);
        locationListeningInteractor.execute(LocationRequestDelegate.createDisplacementRequest(LocationRequestDelegate.LOCATION_REQUEST_FASTEST_INTERVAL), locationSubscriber);
    }

    public void onStop() {
        messagesFetchingInteractor.unsubscribe();
        locationListeningInteractor.unsubscribe();
    }

    @Override
    public void onMessageClick(String messageId) {
        Bundle args = new Bundle();
        args.putString(Constants.EXTRA_MESSAGE_ID, messageId);
        router.showScreen(Screens.MESSAGE_DETAILS, args);
    }
}
