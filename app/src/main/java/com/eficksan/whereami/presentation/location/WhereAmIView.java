package com.eficksan.whereami.presentation.location;

import android.content.Context;
import android.location.Location;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.eficksan.whereami.R;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * View part of location request flow.
 *
 * Manages location controls and location requests results.
 *
 * Created by Aleksei Ivshin
 * on 16.08.2016.
 */
public class WhereAmIView {

    @Bind(R.id.label_location)
    public TextView locationAddresses;
    @Bind(R.id.label_coordinates)
    public TextView locationCoordinates;
    @Bind(R.id.switch_request_location)
    public Switch switchRequestLocation;
    @Bind(R.id.create_message)
    public FloatingActionButton createMessage;

    @Inject
    Context context;

    /**
     * Take view of component.
     * @param view view
     */
    public void takeView(View  view) {
        ButterKnife.bind(this, view);
    }

    /**
     * Changes view on geo data requesting turn on.
     */
    public void onGeoDataTurnOn() {
        switchRequestLocation.setChecked(true);
        locationCoordinates.setText(R.string.location_request_in_progress);
        locationAddresses.setText("");
    }

    /**
     * Changes view on geo data requesting turn off.
     */
    public void onGeoDataTurnOff() {
        switchRequestLocation.setChecked(false);
        locationCoordinates.setText("");
        locationAddresses.setText("");
        disableMessageCreating();
    }

    /**
     * Shows location updates.
     * @param location new location
     */
    public void onLocationChanged(Location location) {
        if (location != null) {
            locationCoordinates.setText(context.getString(R.string.label_coordinates, location.getLatitude(), location.getLongitude()));
        } else {
            locationCoordinates.setText(R.string.location_not_available);
        }
    }

    /**
     * Shows address updates.
     * @param address new address
     */
    public void onAddressChanged(List<String> address) {
        locationAddresses.setText(TextUtils.join(System.getProperty("line.separator"),address));
    }

    public void disableMessageCreating() {
        createMessage.setVisibility(View.GONE);
    }

    public void enableMessageCreating() {
        createMessage.setVisibility(View.VISIBLE);
    }

}
