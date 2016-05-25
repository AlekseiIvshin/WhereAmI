package com.eficksan.whereami.fragments;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eficksan.whereami.MainActivity;
import com.eficksan.whereami.R;
import com.eficksan.whereami.geo.Constants;
import com.eficksan.whereami.geofence.GeofenceTransitionsIntentService;
import com.eficksan.whereami.googleapi.ApiConnectionObservable;
import com.eficksan.whereami.googleapi.ApiConnectionObserver;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Aleksei Ivshin
 * on 24.04.2016.
 */
@SuppressWarnings("ResourceType")
public class GeofenceFragment extends Fragment
        implements ApiConnectionObserver, ResultCallback<Status> {

    public static final String TAG = GeofenceFragment.class.getSimpleName();

    private static final String EXTRA_LOCATION = TAG + "/EXTRA_LOCATION";
    private static final float GEOFENCE_DEFAULT_RADIUS = 100f;
    private static final long GEOFENCE_DEFAULT_EXPIRATION = 60;
    private static final String GEOFENCE_CURRENT_LOCATION_ID = TAG + "/GEOFENCE_CURRENT_LOCATION_ID";

    private WeakReference<GoogleApiClient> mGoogleApiClient;
    private WeakReference<ApiConnectionObservable> mApiConnectionObservable;

    private boolean mGeofencing = false;
    private List<Geofence> mGeofencesList;
    private PendingIntent mGeofencePendingIntent;

    @Override
    public void onResult(Status status) {
        Log.i(TAG, status.toString());
    }

    @SuppressLint("ParcelCreator")
    private class GeofenceResultReceiver extends ResultReceiver {

        public GeofenceResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            String toastMessage = null;
            switch (resultCode) {
                case Constants.SUCCESS_RESULT:
                    //TODO: add mapping for transitions from code to names
                    int geofenceTransistion = resultData.getInt(Constants.EXTRA_GEOFENCSE_TRANSITION);
                    String[] geofencesRequestId = resultData.getStringArray(Constants.EXTRA_GEOFENCES_REQUEST_ID);
                    toastMessage = String.format("Transition: %d, request ids: %s", geofenceTransistion, TextUtils.join(", ", geofencesRequestId));
                    break;
                case Constants.FAILURE_RESULT:
                    toastMessage = resultData.getString(Constants.RESULT_DATA_KEY);
                    break;
            }
            if (toastMessage != null) {
                Toast.makeText(getActivity(), toastMessage, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public int describeContents() {
            return 0;
        }
    }

    private GeofenceResultReceiver mResultReceiver = new GeofenceResultReceiver(new Handler(Looper.getMainLooper()));

    public static GeofenceFragment newInstance(Location location) {
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_LOCATION, location);

        GeofenceFragment fragment = new GeofenceFragment();
        fragment.setArguments(args);
        return fragment;
    }

    //TODO: move to base fragment
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mApiConnectionObservable = new WeakReference<ApiConnectionObservable>((MainActivity) context);
        mApiConnectionObservable.get().registerConnectionObserver(this);
        GoogleApiClient googleApiClient = ((MainActivity) context).getGoogleApiClient();
        if (googleApiClient != null) {
            mGoogleApiClient = new WeakReference<>(googleApiClient);
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
        if (mGeofencing && mGoogleApiClient.get() != null) {
            addGeofences(mGoogleApiClient.get());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGeofencing) {
            removeGeofences(mGoogleApiClient.get());
        }
    }

    //TODO: move to base fragment
    @Override
    public void onDetach() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.clear();
        }
        if (mApiConnectionObservable != null) {
            mApiConnectionObservable.get().unregisterConnectionObserver(this);
            mApiConnectionObservable.clear();
        }
        super.onDetach();
    }


    private void updateUI() {
        //TODO: update UI
    }

    private void addGeofences(GoogleApiClient googleApiClient) {
        LocationServices.GeofencingApi.addGeofences(
                googleApiClient,
                getGeofencingRequest(),
                getGeofencePendingIntent()
        ).setResultCallback(this);
    }

    private void removeGeofences(GoogleApiClient googleApiClient) {
        LocationServices.GeofencingApi.removeGeofences(
                googleApiClient,
                getGeofencePendingIntent()
        ).setResultCallback(this);
    }

    private PendingIntent getGeofencePendingIntent() {
        if (mGeofencePendingIntent == null) {
            Intent intent = GeofenceTransitionsIntentService.getGeofenceTransitionIntent(getActivity(), mResultReceiver);
            mGeofencePendingIntent = PendingIntent.getService(getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        }
        return mGeofencePendingIntent;
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(getGeofencesList());
        return builder.build();
    }

    private List<Geofence> getGeofencesList() {
        if (mGeofencesList == null) {
            mGeofencesList = new LinkedList<>();

            Location location = getArguments().getParcelable(EXTRA_LOCATION);
            mGeofencesList.add(new Geofence.Builder()
                    .setRequestId(GEOFENCE_CURRENT_LOCATION_ID)
                    .setCircularRegion(location.getLatitude(), location.getLongitude(), GEOFENCE_DEFAULT_RADIUS)
                    .setExpirationDuration(GEOFENCE_DEFAULT_EXPIRATION)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build());
        }

        return mGeofencesList;
    }

    @Override
    public void onConnected(GoogleApiClient googleApiClient, Bundle bundle) {
        mGoogleApiClient = new WeakReference<>(googleApiClient);
        mGeofencing = true;
        addGeofences(googleApiClient);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        mGeofencing = false;
        mGoogleApiClient.clear();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGeofencing = false;
    }
}
