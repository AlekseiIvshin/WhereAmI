package com.eficksan.whereami.presentation.maps;

import com.eficksan.whereami.presentation.routing.Router;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import javax.inject.Inject;

/**
 * Created by Aleksei_Ivshin on 9/5/16.
 */
public class MapMessagesPresenter implements OnMapReadyCallback {

    @Inject
    Router router;

    private MapMessagesView mMapMessagesView;

    public void setView(MapMessagesView mapMessagesView) {
        this.mMapMessagesView = mapMessagesView;
    }

    public void onCreate() {
        mMapMessagesView.messagesMap.getMapAsync(this);
    }

    public void onDestroy() {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("Marker"));
    }
}
