package com.eficksan.whereami.presentation.maps;

import android.location.Location;
import android.os.Bundle;

import com.eficksan.whereami.R;
import com.eficksan.whereami.data.location.LocationRequestDelegate;
import com.eficksan.whereami.data.messages.PlacingMessage;
import com.eficksan.whereami.domain.Constants;
import com.eficksan.whereami.domain.location.LocationListeningInteractor;
import com.eficksan.whereami.domain.messages.MessagesFetchingInteractor;
import com.eficksan.whereami.presentation.BasePresenter;
import com.eficksan.whereami.presentation.routing.Screens;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Presenter for maps.
 */
public class MapMessagesPresenter extends BasePresenter implements MapMessagesView.MapMessageClickListener {

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
            messagesFetchingInteractor.execute(new LatLng(location.getLatitude(), location.getLongitude()), messagesSubscriber);
        }
    };

    private Subscriber<List<PlacingMessage>> messagesSubscriber = new Subscriber<List<PlacingMessage>>() {
        @Override
        public void onCompleted() {
            messagesFetchingInteractor.unsubscribe();
        }

        @Override
        public void onError(Throwable e) {
            mMapMessagesView.showError(R.string.error_maps_fetching_messages);
            messagesFetchingInteractor.unsubscribe();
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
        locationListeningInteractor.execute(LocationRequestDelegate.createDisplacementRequest(LocationRequestDelegate.LOCATION_REQUEST_SMALLEST_DISPLACEMENT), locationSubscriber);
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
