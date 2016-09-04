package com.eficksan.whereami.presentation.location;

import android.location.Location;
import android.os.Bundle;
import android.view.View;

import com.eficksan.whereami.data.location.LocationAddress;
import com.eficksan.whereami.domain.Constants;
import com.eficksan.whereami.domain.location.ForegroundServiceInteractor;
import com.eficksan.whereami.domain.location.ListenLocationInteractor;
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
    ForegroundServiceInteractor foregroundServiceInteractor;

    private Location lastLocation = null;
    private Subscription mCreateMessageListener;
    private Subscription mLocationHistoryListener;

    public void onStart() {
        foregroundServiceInteractor.onStart();

        handleSwitchLocationListening(mView.switchRequestLocation.isChecked());

        setListeners();
    }

    public void onStop() {
        removeListeners();
        foregroundServiceInteractor.onStop();
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
        foregroundServiceInteractor.turnLocationRequesting(isNeedToListenLocation);
        if (isNeedToListenLocation) {
            mView.onGeoDataTurnOn();
            listenLocationInteractor.execute(30000L, new Subscriber<LocationAddress>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(LocationAddress locationAddress) {
                    lastLocation = locationAddress.location;
                    mView.onLocationChanged(locationAddress.location);
                    mView.onAddressChanged(locationAddress.address);
                    if (locationAddress.location == null) {
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
