package com.eficksan.whereami.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.eficksan.whereami.MainActivity;
import com.eficksan.whereami.googleapi.ApiConnectionObservable;
import com.eficksan.whereami.googleapi.ApiConnectionObserver;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.lang.ref.WeakReference;

/**
 * Created by Aleksei Ivshin
 * on 24.04.2016.
 */
@SuppressWarnings("ResourceType")
public abstract class BaseApiConnectedFragment extends Fragment {

    private GoogleApiClient mGoogleApiClient;
    private WeakReference<ApiConnectionObservable> mApiConnectionObservable;
    private ApiConnectionObserver mApiConnectionObserver;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mApiConnectionObserver = new ApiConnectionObserver() {
            @Override
            public void onConnected(GoogleApiClient googleApiClient, Bundle bundle) {
                mGoogleApiClient = googleApiClient;
                BaseApiConnectedFragment.this.onConnected(bundle);
            }

            @Override
            public void onConnectionFailed(ConnectionResult connectionResult) {
                mGoogleApiClient = null;
                BaseApiConnectedFragment.this.onConnectionFailed(connectionResult);
            }

            @Override
            public void onConnectionSuspended(int i) {
            }
        };
        mApiConnectionObservable = new WeakReference<ApiConnectionObservable>((MainActivity) context);
        mApiConnectionObservable.get().registerConnectionObserver(mApiConnectionObserver);
        GoogleApiClient googleApiClient = ((MainActivity) context).getGoogleApiClient();
        if (googleApiClient != null) {
            mGoogleApiClient = googleApiClient;
        }
    }

    @Override
    public void onDetach() {
        mGoogleApiClient = null;
        if (mApiConnectionObservable != null) {
            mApiConnectionObservable.get().unregisterConnectionObserver(mApiConnectionObserver);
            mApiConnectionObservable.clear();
        }
        super.onDetach();
    }

    public GoogleApiClient getGoogleApiClient() {
        return mGoogleApiClient;
    }

    public boolean isGoogleApiConnected() {
        return (mGoogleApiClient != null && mGoogleApiClient != null && mGoogleApiClient.isConnected());
    }

    public abstract void onConnected(Bundle bundle);

    public abstract void onConnectionFailed(ConnectionResult connectionResult);

    public abstract void onConnectionSuspended(int i);
}
