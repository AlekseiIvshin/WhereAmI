package com.eficksan.whereami.presentation.maps;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.eficksan.whereami.R;
import com.eficksan.whereami.data.messages.PlacingMessage;
import com.eficksan.whereami.presentation.IView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Provides messages map view.
 */
public class MapMessagesView implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, IView {

    private static final String TAG = MapMessagesView.class.getSimpleName();
    @Inject
    Context context;

    @Bind(R.id.messages_map)
    public MapView messagesMap;
    private GoogleMap mGoogleMap;
    private Marker mUserPositionMarker;
    private MapMessageClickListener mMessageClickListener;

    public void takeView(View view) {
        ButterKnife.bind(this, view);
        messagesMap.getMapAsync(this);
    }

    @Override
    public void releaseView() {
        mGoogleMap.clear();
        mGoogleMap = null;
        messagesMap.onDestroy();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setOnMarkerClickListener(this);
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
        Marker marker;
        for (PlacingMessage message : messages) {
            marker = mGoogleMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_message))
                    .position(new LatLng(message.latitude, message.longitude))
                    .title(message.message));
            marker.setTag(message.messageId);
        }
    }

    public void showError(int errorResId) {
        Toast.makeText(context, errorResId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.v(TAG, "On marker click");
        Object markerTag = marker.getTag();
        if (markerTag != null && markerTag instanceof String) {
            String messageId = String.valueOf(marker.getTag());
            Log.v(TAG, "Marker has message id = " + messageId);
            if (messageId != null && mMessageClickListener != null) {
                mMessageClickListener.onMessageClick(messageId);
                return true;
            }
        }

        return false;
    }

    public void setMessageClickListener(MapMessageClickListener messageClickListener) {
        mMessageClickListener = messageClickListener;
    }

    public interface MapMessageClickListener {
        void onMessageClick(String messageId);
    }
}
