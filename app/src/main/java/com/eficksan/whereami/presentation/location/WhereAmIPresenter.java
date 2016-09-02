package com.eficksan.whereami.presentation.location;

import android.location.Location;
import android.os.Bundle;
import android.view.View;

import com.eficksan.whereami.data.location.WaiEvent;
import com.eficksan.whereami.domain.Constants;
import com.eficksan.whereami.domain.location.ForegroundServiceInteractor;
import com.eficksan.whereami.domain.location.ListenLocationInteractor;
import com.eficksan.whereami.domain.location.LocationAndAddressRequestInteractor;
import com.eficksan.whereami.domain.messaging.MessagesContainer;
import com.eficksan.whereami.presentation.routing.Router;
import com.eficksan.whereami.presentation.routing.Screens;
import com.jakewharton.rxbinding.view.RxView;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by Aleksei Ivshin
 * on 20.08.2016.
 */
public class WhereAmIPresenter {

    private WhereAmIView mView;

    @Inject
    Router mRouter;
    @Inject
    ListenLocationInteractor listenLocationInteractor;

    @Inject
    LocationAndAddressRequestInteractor locationAndAddressRequestInteractor;

    @Inject
    ForegroundServiceInteractor foregroundServiceInteractor;

    @Inject
    MessagesContainer mMessagesContainer;

    private Location lastLocation = null;
    private Subscription mCreateMessageListener;
    private Subscription mLocationHistoryListener;

    public void onStart() {
        foregroundServiceInteractor.stopForeground();

        handleSwitchLocationListening(mView.switchRequestLocation.isChecked());

        setListeners();
    }

    public void onStop() {
        removeListeners();
        foregroundServiceInteractor.startForeground();
        listenLocationInteractor.unsubscribe();
    }

    /**
     * Sets listeners on view events and other events.
     */
    private void setListeners() {
        mView.switchRequestLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleSwitchLocationListening(mView.switchRequestLocation.isChecked());
            }
        });

        mCreateMessageListener = RxView.clicks(mView.createMessage)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (lastLocation != null) {
                            Bundle args = new Bundle();
                            args.putParcelable(Constants.EXTRA_LOCATION_DATA, lastLocation);
                            mRouter.showScreen(Screens.MESSAGING_SCREEN, args);
                        } else {
                            //TODO: show error
                        }
                    }
                });
    }

    /**
     * Remove listeners.
     */
    private void removeListeners() {
        mView.switchRequestLocation.setOnClickListener(null);
        mCreateMessageListener.unsubscribe();
        mCreateMessageListener = null;
        mLocationHistoryListener.unsubscribe();
        mLocationHistoryListener = null;
    }

    /**
     * Handles switching location requesting.
     *
     * @param isNeedToListenLocation flag for starting location listening
     */
    private void handleSwitchLocationListening(boolean isNeedToListenLocation) {
        mView.disableMessageCreating();
        if (isNeedToListenLocation) {
            mView.onGeoDataTurnOn();
            listenLocationInteractor.execute(30000L, new Subscriber<WaiEvent>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(WaiEvent waiEvent) {
                    lastLocation = waiEvent.location;
                    mView.onLocationChanged(waiEvent.location);
                    mView.onAddressChanged(waiEvent.addresses);
                    if (waiEvent.location == null) {
                        mView.disableMessageCreating();
                    } else {
                        mView.enableMessageCreating();
                    }
                }
            });
        } else {
            mView.onGeoDataTurnOff();
            lastLocation = null;
            listenLocationInteractor.unsubscribe();
        }
    }

    public void setView(WhereAmIView view) {
        this.mView = view;
    }
}
