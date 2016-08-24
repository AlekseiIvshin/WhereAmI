package com.eficksan.whereami.presentation.location;

import android.view.View;

import com.eficksan.whereami.data.location.WaiEvent;
import com.eficksan.whereami.domain.location.ListenLocationInteractor;
import com.eficksan.whereami.domain.location.ForegroundServiceInteractor;
import com.eficksan.whereami.routing.Router;

import rx.Subscriber;

/**
 * Created by Aleksei Ivshin
 * on 20.08.2016.
 */
public class WaiPresenter {

    private Router mRouter;
    private WaiView mView;
    private ListenLocationInteractor listenLocationInteractor;
    private ForegroundServiceInteractor foregroundServiceInteractor;

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
    }

    /**
     * Handles switching location requesting.
     *
     * @param isNeedToListenLocation
     */
    private void handleSwitchLocationListening(boolean isNeedToListenLocation) {
        if (isNeedToListenLocation) {
            listenLocationInteractor.execute(30000l, new Subscriber<WaiEvent>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(WaiEvent waiEvent) {
                    mView.onLocationChanged(waiEvent.location);
                    mView.onAddressChanged(waiEvent.addresses);
                }
            });
        } else {
            listenLocationInteractor.unsubscribe();
        }
    }
}
