package com.eficksan.whereami.presentation.location;

import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.eficksan.whereami.R;
import com.eficksan.whereami.domain.Constants;
import com.eficksan.whereami.domain.location.AddressFetchingInteractor;
import com.eficksan.whereami.domain.location.ForegroundServiceInteractor;
import com.eficksan.whereami.domain.location.LocationListeningInteractor;
import com.eficksan.whereami.presentation.common.BasePresenter;
import com.eficksan.whereami.presentation.routing.Screens;
import com.jakewharton.rxbinding.view.RxView;

import java.util.ArrayList;

import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by Aleksei Ivshin
 * on 20.08.2016.
 */
public class WhereAmIPresenter extends BasePresenter<WhereAmIView> {
    private static final ArrayList<Location> locationHistory = new ArrayList<>();
    private static String addressesHeap = "";

    final ForegroundServiceInteractor mForegroundServiceInteractor;

    final LocationListeningInteractor mLocationListeningInteractor;

    final AddressFetchingInteractor mAddressFetchingInteractor;

    private Location mLastLocation = null;
    private Subscription mCreateMessageListener;

    public WhereAmIPresenter(
            ForegroundServiceInteractor foregroundServiceInteractor,
            LocationListeningInteractor locationListeningInteractor,
            AddressFetchingInteractor addressFetchingInteractor) {
        this.mForegroundServiceInteractor = foregroundServiceInteractor;
        this.mLocationListeningInteractor = locationListeningInteractor;
        this.mAddressFetchingInteractor = addressFetchingInteractor;
    }

    public void onStart() {
        super.onStart();
        mForegroundServiceInteractor.onStart();

        handleSwitchLocationListening(mView.switchRequestLocation.isChecked());

        setListeners();
    }

    public void onStop() {
        removeListeners();
        mForegroundServiceInteractor.onStop();
        mLocationListeningInteractor.unsubscribe();
        super.onStop();
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
                        if (mLastLocation != null) {
                            Bundle args = new Bundle();
                            args.putParcelable(Constants.EXTRA_LOCATION_DATA, mLastLocation);
                            mRouter.showScreen(Screens.MESSAGING_SCREEN, args);
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
        mForegroundServiceInteractor.turnLocationRequesting(isNeedToListenLocation);
        if (isNeedToListenLocation) {
            mView.onGeoDataTurnOn();
            mLocationListeningInteractor.execute(new LocationSubscriber());
        } else {
            mView.onGeoDataTurnOff();
            mLastLocation = null;
            mLocationListeningInteractor.unsubscribe();
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

    /**
     * Listens location changes.
     */
    private class LocationSubscriber extends Subscriber<Location> {
        @Override
        public void onCompleted() {
            mLocationListeningInteractor.unsubscribe();
        }

        @Override
        public void onError(Throwable e) {
            mLocationListeningInteractor.unsubscribe();
            Log.e(WhereAmIPresenter.class.getSimpleName(), e.getMessage(), e);
        }

        @Override
        public void onNext(Location location) {
            if (location !=null) {
                locationHistory.add(new Location(location));
            }
            mLastLocation = location;
            mView.onLocationChanged(location);
            updateAvailabilityToCreateMessage(location);
            mAddressFetchingInteractor.execute(location, new AddressSubscriber());
        }
    }

    /**
     * Listens address changes.
     */
    private class AddressSubscriber extends Subscriber<Address> {
        @Override
        public void onCompleted() {
            mAddressFetchingInteractor.unsubscribe();
        }

        @Override
        public void onError(Throwable e) {
            mAddressFetchingInteractor.unsubscribe();
            Log.e(WhereAmIPresenter.class.getSimpleName(), e.getMessage(), e);
        }

        @Override
        public void onNext(Address address) {
            if (address!=null) {
                addressesHeap += (addressesHeap + address.toString());
            }
            mView.onAddressChanged(address);
            mAddressFetchingInteractor.unsubscribe();
        }
    }
}
