package com.eficksan.whereami.presentation.location;

import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.view.View;

import com.eficksan.whereami.R;
import com.eficksan.whereami.data.location.LocationRequestDelegate;
import com.eficksan.whereami.domain.Constants;
import com.eficksan.whereami.domain.location.AddressFetchingInteractor;
import com.eficksan.whereami.domain.location.ForegroundServiceInteractor;
import com.eficksan.whereami.domain.location.LocationListeningInteractor;
import com.eficksan.whereami.presentation.BasePresenter;
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
public class WhereAmIPresenter extends BasePresenter {

    private WhereAmIView mView;

    final ForegroundServiceInteractor foregroundServiceInteractor;

    final LocationListeningInteractor locationListeningInteractor;

    final AddressFetchingInteractor addressFetchingInteractor;

    /**
     * Listens address changes.
     */
    private Subscriber<Address> addressSubscriber = new Subscriber<Address> (){
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(Address address) {
            mView.onAddressChanged(address);
            addressFetchingInteractor.unsubscribe();
        }
    };

    /**
     * Listens location changes.
     */
    private Subscriber<Location> locationSubscriber = new Subscriber<Location> (){
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(Location location) {
            lastLocation = location;
            mView.onLocationChanged(location);
            updateAvailabilityToCreateMessage(location);
            addressFetchingInteractor.execute(location, addressSubscriber);
        }
    };

    private Location lastLocation = null;
    private Subscription mCreateMessageListener;

    public WhereAmIPresenter(ForegroundServiceInteractor foregroundServiceInteractor, LocationListeningInteractor locationListeningInteractor, AddressFetchingInteractor addressFetchingInteractor) {
        this.foregroundServiceInteractor = foregroundServiceInteractor;
        this.locationListeningInteractor = locationListeningInteractor;
        this.addressFetchingInteractor = addressFetchingInteractor;
    }

    public void onStart() {
        foregroundServiceInteractor.onStart();

        handleSwitchLocationListening(mView.switchRequestLocation.isChecked());

        setListeners();
    }

    public void onStop() {
        removeListeners();
        foregroundServiceInteractor.onStop();
        locationListeningInteractor.unsubscribe();
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
                            router.showScreen(Screens.MESSAGING_SCREEN, args);
                        } else {
                            mView.showError(R.string.location_not_available);
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
            locationListeningInteractor.execute(LocationRequestDelegate.createIntervalLocationRequest(), locationSubscriber);
        } else {
            mView.onGeoDataTurnOff();
            lastLocation = null;
            locationListeningInteractor.unsubscribe();
        }
    }

    public void setView(WhereAmIView view) {
        this.mView = view;
    }

    private void updateAvailabilityToCreateMessage(Location location) {
        if (location == null) {
            mView.disableMessageCreating();
        } else {
            mView.enableMessageCreating();
        }
    }
}
