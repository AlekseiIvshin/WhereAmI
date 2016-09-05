package com.eficksan.whereami.presentation.maps;

import android.content.Context;
import android.location.Location;
import android.view.View;

import com.eficksan.whereami.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Aleksei_Ivshin on 9/5/16.
 */
public class MapMessagesView implements OnMapReadyCallback {

    @Inject
    Context context;

    @Bind(R.id.messages_map)
    public MapView messagesMap;
    private GoogleMap mGoogleMap;
    private Marker mUserPositionMarker;

    public void takeView(View view) {
        ButterKnife.bind(this, view);
        messagesMap.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
    }

    /**
     * Moves map to location.
     * @param location target location
     */
    public void moveMapTo(Location location) {
        if (mGoogleMap!=null) {
            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
            if (mUserPositionMarker == null) {
                mUserPositionMarker = mGoogleMap.addMarker(new MarkerOptions()
                        .position(userLocation)
                        .title(context.getString(R.string.current_location)));
            } else {
                mUserPositionMarker.setPosition(userLocation);
            }
        }
    }
}
