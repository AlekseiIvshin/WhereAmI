package com.eficksan.whereami.presentation.maps;

import android.content.Context;
import android.location.Location;
import android.util.Log;
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
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.algo.NonHierarchicalDistanceBasedAlgorithm;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Provides messages map view.
 */
public class MapMessagesView implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private static final String TAG = MapMessagesView.class.getSimpleName();
    @Inject
    Context context;

    @Bind(R.id.messages_map)
    public MapView messagesMap;
    private GoogleMap mGoogleMap;
    private Marker mUserPositionMarker;
    private MapMessageClickListener mMessageClickListener;
    private ClusterManager<MessageClusterItem> mClusterManager;

    public void takeView(View view) {
        ButterKnife.bind(this, view);
        messagesMap.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setOnMarkerClickListener(this);
        setUpClusterer(mGoogleMap);
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
        MessageClusterItem clusterItem;
        for (PlacingMessage message : messages) {
            clusterItem = new MessageClusterItem(new LatLng(message.latitude, message.longitude));
            marker = mGoogleMap.addMarker(new MarkerOptions()
                    .position(clusterItem.getPosition())
                    .title(message.message));
            marker.setTag(message.messageId);
            if (mClusterManager!=null) {
                mClusterManager.addItem(clusterItem);
            }
        }
    }

    public void showError(int errorResId) {
        Toast.makeText(context, errorResId, Toast.LENGTH_SHORT).show();
    }

    public void onDestroy() {
        mClusterManager.clearItems();
        mClusterManager = null;
        mGoogleMap = null;
        messagesMap.onDestroy();
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

        return mClusterManager != null && mClusterManager.onMarkerClick(marker);
    }

    public void setMessageClickListener(MapMessageClickListener messageClickListener) {
        mMessageClickListener = messageClickListener;
    }

    public interface MapMessageClickListener {
        void onMessageClick(String messageId);
    }

    public static class MessageClusterItem implements ClusterItem {
        public final LatLng position;

        public MessageClusterItem(LatLng position) {
            this.position = position;
        }

        @Override
        public LatLng getPosition() {
            return position;
        }
    }

    private void setUpClusterer(GoogleMap map) {
        mClusterManager = new ClusterManager<>(context, map);
        map.setOnCameraIdleListener(mClusterManager);
    }
}
