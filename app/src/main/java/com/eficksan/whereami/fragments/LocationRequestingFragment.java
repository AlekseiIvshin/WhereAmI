package com.eficksan.whereami.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.eficksan.whereami.App;
import com.eficksan.whereami.MainActivity;
import com.eficksan.whereami.R;
import com.eficksan.whereami.geo.Constants;
import com.eficksan.whereami.geo.FetchAddressIntentService;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Aleksei Ivshin
 * on 24.04.2016.
 */
@SuppressWarnings("ResourceType")
public class LocationRequestingFragment extends Fragment implements LocationListener {

   WeakReference<GoogleApiClient> mGoogleApiClient;

    public static final int LOCATION_REQUEST_INTERVAL = 10000;
    public static final int LOCATION_REQUEST_FASTEST_INTERVAL = 5000;

    public static final String TAG = LocationRequestingFragment.class.getSimpleName();

    private static final String KEY_REQUESTING_LOCATION_UPDATES = "REQUESTING_LOCATION_UPDATES";
    private static final String KEY_LAST_LOCATION = "KEY_LAST_LOCATION";
    private static final String KEY_LAST_LOCATION_ADDRESSES = "KEY_LAST_LOCATION_ADDRESSES";

    @Bind(R.id.label_location)
    TextView mLocationAddresses;
    @Bind(R.id.label_coordinates)
    TextView mLocationCoordinates;

    @Inject
    LocationManager locationManager;

    private Location mLastLocation;
    private String mLastLocationAddresses;
    private LocationRequest mLocationRequest;
    private boolean mRequestingLocationUpdates = false;

    @SuppressLint("ParcelCreator")
    private class AddressResultReceiver extends ResultReceiver {

        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            switch (resultCode) {
                case Constants.SUCCESS_RESULT:
                    mLastLocationAddresses = resultData.getString(Constants.RESULT_DATA_KEY);
                    updateUI();
                    break;
                case Constants.FAILURE_RESULT:
                    Toast.makeText(getActivity(), resultData.getString(Constants.RESULT_DATA_KEY), Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        @Override
        public int describeContents() {
            return 0;
        }
    }

    private AddressResultReceiver mResultReceiver = new AddressResultReceiver(new Handler(Looper.getMainLooper()));

    public static LocationRequestingFragment newInstance() {
        return new LocationRequestingFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mGoogleApiClient = new WeakReference<>(((MainActivity) context).getGoogleApiClient());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getActivity().getApplication()).getObjectGraph().inject(this);
        restoreLocationFromBundle(savedInstanceState);
        if (mGoogleApiClient.get() != null && mGoogleApiClient.get().isConnected()) {
            createLocationRequest();
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient.get());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_location_requesting, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateUI();
    }

    /* Request updates at startup */
    @Override
    public void onResume() {
        super.onResume();
        if (mRequestingLocationUpdates) {
           startLocationRequest();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mRequestingLocationUpdates) {
            stopLocationRequest();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_REQUESTING_LOCATION_UPDATES, mRequestingLocationUpdates);
        outState.putParcelable(KEY_LAST_LOCATION, mLastLocation);
        outState.putString(KEY_LAST_LOCATION_ADDRESSES, mLastLocationAddresses);
    }

    @Override
    public void onDetach() {
        mGoogleApiClient.clear();
        super.onDetach();
    }

    @OnClick(R.id.request_location)
    public void handleRequestLocation() {
        Log.v(TAG, "Handle location request");
        if (!mRequestingLocationUpdates) {
            mRequestingLocationUpdates = true;
            startLocationRequest();
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        Log.v(TAG, String.format("Location changed: %1$.4f x %2$.4f", location.getLatitude(), location.getLongitude()));
        mLastLocation = location;
        FetchAddressIntentService.requestLocationAddresses(getActivity(), mResultReceiver, mLastLocation);
        updateUI();
    }

    /**
     * Starts requesting API for obtain current location.
     */
    private void startLocationRequest() {
        Log.v(TAG, String.format(
                "Start location request: is API client not null %b, is API client connected %b",
                mGoogleApiClient.get() !=null,
                mGoogleApiClient.get().isConnected()));
        if (mGoogleApiClient.get() != null && mGoogleApiClient.get().isConnected()) {
            Log.v(TAG, "Location requested: " + mLocationRequest.toString());
            LocationServices.FusedLocationApi
                    .requestLocationUpdates(mGoogleApiClient.get(), mLocationRequest, this);
        }
    }

    /**
     * Stops requesting location.
     */
    private void stopLocationRequest() {
        Log.v(TAG, "Stop location request");
        if (mGoogleApiClient.get() != null && mRequestingLocationUpdates) {
            Log.v(TAG, "Location request stopped");
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient.get(), this);
        }
    }

    private void updateUI(){
        if (mLastLocation != null) {
            mLocationCoordinates.setText(getString(R.string.label_coordinates, mLastLocation.getLatitude(), mLastLocation.getLongitude()));
        } else {
            mLocationCoordinates.setText(R.string.location_not_available);
        }

        mLocationAddresses.setText(mLastLocationAddresses);
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(LOCATION_REQUEST_INTERVAL);
        mLocationRequest.setFastestInterval(LOCATION_REQUEST_FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void restoreLocationFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mRequestingLocationUpdates = savedInstanceState.getBoolean(KEY_REQUESTING_LOCATION_UPDATES, false);
            mLastLocation = savedInstanceState.getParcelable(KEY_LAST_LOCATION);
            mLastLocationAddresses = savedInstanceState.getString(KEY_LAST_LOCATION_ADDRESSES, "");
        } else {
            mRequestingLocationUpdates = false;
        }
    }

}
