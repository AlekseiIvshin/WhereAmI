package com.eficksan.whereami.presentation.maps;

import android.content.Context;
import android.location.Location;
import android.view.View;
import android.widget.Toast;

import com.eficksan.whereami.R;
import com.eficksan.whereami.data.messages.PlacingMessage;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Provides messages map view.
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
     *
     * @param location target location
     */
    public void moveMapTo(Location location) {
        if (mGoogleMap != null) {
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

    /**
     * Shows messages on map.
     *
     * @param messages messages
     */
    public void showMessages(List<PlacingMessage> messages) {
        for (PlacingMessage message :
                messages) {
            mGoogleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(message.latitude, message.longitude))
                    .title(message.message));
        }
    }

    public void showError(int errorResId) {
        Toast.makeText(context, errorResId, Toast.LENGTH_SHORT).show();
    }

    public void onDestroy() {
        mGoogleMap = null;
        messagesMap.onDestroy();
    }
}
