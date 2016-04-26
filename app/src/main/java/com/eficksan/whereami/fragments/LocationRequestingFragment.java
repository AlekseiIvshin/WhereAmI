package com.eficksan.whereami.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.eficksan.whereami.App;
import com.eficksan.whereami.R;
import com.eficksan.whereami.geo.Constants;
import com.eficksan.whereami.geo.FetchAddressIntentService;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.support.v4.content.ContextCompat.checkSelfPermission;

/**
 * Created by Aleksei Ivshin
 * on 24.04.2016.
 */
@SuppressWarnings("ResourceType")
public class LocationRequestingFragment extends Fragment implements LocationListener {

    public static final int LOCATION_PERMISSION_REQUEST = 42;
    public static final int LOCATION_REQUEST_MIN_TIMEOUT = 60000;
    public static final int LOCATION_REQUEST_MIN_DISTANCE = 10;

    public static final String TAG = LocationRequestingFragment.class.getSimpleName();

    @Bind(R.id.label_location)
    TextView mLocationLabel;
    @Bind(R.id.label_coordinates)
    TextView mCoordinates;

    @Inject
    LocationManager locationManager;

    private String provider;
    private Location lastLocation;

    @SuppressLint("ParcelCreator")
    private class AddressResultReceiver extends ResultReceiver {

        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            switch (resultCode) {
                case Constants.SUCCESS_RESULT:
                    mLocationLabel.setText(resultData.getString(Constants.RESULT_DATA_KEY));
                    break;
                case Constants.FAILURE_RESULT:
                    Toast.makeText(getActivity(), resultData.getString(Constants.RESULT_DATA_KEY), Toast.LENGTH_SHORT);
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getActivity().getApplication()).getObjectGraph().inject(this);

        boolean enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        Log.v(TAG, "GPS is " + (enabled ? "enabled" : "disabled"));
        if (!enabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        Log.v(TAG, String.format("Best provider is '%s'", provider));
        if (!checkPermissions()) {
            Log.v(TAG, String.format("OnCreate: Permissions were not granted: %s, %s ", ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION));
            if (shouldShowRequestPermissionRationale(ACCESS_COARSE_LOCATION)) {
                //TODO: show explanation
                Log.v(TAG, "Should show explanations");
            } else {
                Log.v(TAG, "Request permissions with request key = " + LOCATION_PERMISSION_REQUEST);
                requestPermissions(new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_location_requesting, container, false);
        ButterKnife.bind(this, rootView);
        if (checkPermissions()) {
            Location location = locationManager.getLastKnownLocation(provider);
            Log.v(TAG, "Last known location " + location);

            // Initialize the location fields
            if (location != null) {
                Log.v(TAG, "Provider " + provider + " has been selected.");
                onLocationChanged(location);
            } else {
                mCoordinates.setText(R.string.location_not_available);
            }
        }
        return rootView;
    }

    /* Request updates at startup */
    @Override
    public void onResume() {
        super.onResume();
        if (provider != null && checkPermissions()) {
            Log.v(TAG, String.format("Request location updates provider = %s, time interval = %d, distance = %d",
                    provider, LOCATION_REQUEST_MIN_TIMEOUT, LOCATION_REQUEST_MIN_DISTANCE));
            locationManager.requestLocationUpdates(provider, LOCATION_REQUEST_MIN_TIMEOUT, LOCATION_REQUEST_MIN_DISTANCE, this);
        } else {
            Log.v(TAG, String.format("OnResume: Permissions were not granted: %s, %s ", ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (provider != null && checkPermissions()) {
            Log.v(TAG, "Stops Request location updates");
            locationManager.removeUpdates(this);
        }
    }

    private boolean checkPermissions() {
        return (checkSelfPermission(getActivity(), ACCESS_FINE_LOCATION) == PERMISSION_GRANTED
                || checkSelfPermission(getActivity(), ACCESS_COARSE_LOCATION) == PERMISSION_GRANTED);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (LOCATION_PERMISSION_REQUEST == requestCode) {
            for (int grantResult : grantResults) {
                if (PERMISSION_GRANTED == grantResult) {
                    Toast.makeText(getActivity(), "Permissions granted!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }
    }

    @OnClick(R.id.request_location)
    public void handleRequestLocation() {
        if (lastLocation != null) {
            FetchAddressIntentService.requestLocationAddresses(getActivity(), mResultReceiver, lastLocation);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.v(TAG, String.format("Location changed: %1$.4f x %2$.4f", location.getLatitude(), location.getLongitude()));
        lastLocation = location;
        mCoordinates.setText(getString(R.string.label_coordinates, location.getLatitude(), location.getLongitude()));
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
        Toast.makeText(getActivity(), "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String s) {
        Toast.makeText(getActivity(), "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

}
