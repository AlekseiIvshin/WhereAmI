package com.eficksan.whereami.googleapi;

import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by Aleksei Ivshin
 * on 03.05.2016.
 */
public interface ApiConnectionObserver {

    void onConnected(GoogleApiClient googleApiClient, Bundle bundle);

    void onConnectionFailed(ConnectionResult result);

    void onConnectionSuspended(int i);
}
