package com.eficksan.whereami.presentation.location;

import android.location.Location;
import android.os.Bundle;
import android.view.View;

import com.eficksan.whereami.data.location.WaiEvent;
import com.eficksan.whereami.domain.location.ForegroundServiceInteractor;
import com.eficksan.whereami.domain.location.ListenLocationInteractor;
import com.eficksan.whereami.domain.Constants;
import com.eficksan.whereami.presentation.routing.Router;
import com.eficksan.whereami.presentation.routing.Screens;
import com.jakewharton.rxbinding.view.RxView;

import rx.Subscriber;
import rx.functions.Action1;

/**
 * Created by Aleksei Ivshin
 * on 20.08.2016.
 */
public class WaiPresenter {

    private Router mRouter;
    private WaiView mView;
    private ListenLocationInteractor listenLocationInteractor;
    private ForegroundServiceInteractor foregroundServiceInteractor;

    private Location lastLocation = null;

    public void onStart(Router router, WaiView view, ListenLocationInteractor listenLocationInteractor, ForegroundServiceInteractor foregroundServiceInteractor) {
        mRouter = router;
        mView = view;
        this.listenLocationInteractor = listenLocationInteractor;
        this.foregroundServiceInteractor = foregroundServiceInteractor;

        foregroundServiceInteractor.stopForeground();

        handleSwitchLocationListening(mView.viewHolder.switchRequestLocation.isChecked());

        setListeners();
    }

    public void onStop() {
        foregroundServiceInteractor.startForeground();
        listenLocationInteractor.unsubscribe();
    }

    /**
     * Sets listeners on view events.
     */
    private void setListeners() {
        mView.viewHolder.switchRequestLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleSwitchLocationListening(mView.viewHolder.switchRequestLocation.isChecked());
            }
        });

        RxView.clicks(mView.viewHolder.createMessage)
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
     * Handles switching location requesting.
     *
     * @param isNeedToListenLocation
     */
    private void handleSwitchLocationListening(boolean isNeedToListenLocation) {
        mView.disableMessageCreating();
        if (isNeedToListenLocation) {
            mView.onGeoDataTurnOn();
            listenLocationInteractor.execute(30000l, new Subscriber<WaiEvent>() {
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
}
