package com.eficksan.whereami.presentation.location;

import android.content.Context;
import android.location.Location;
import android.text.TextUtils;

import com.eficksan.whereami.R;

import java.util.List;

/**
 * View part of location request flow.
 *
 * Manages location controls and location requests results.
 *
 * Created by Aleksei Ivshin
 * on 16.08.2016.
 */
public class WaiView {

    public final WaiViewHolder viewHolder;
    private final Context context;

    public WaiView(WaiViewHolder viewHolder, Context context) {
        this.viewHolder = viewHolder;
        this.context = context;
    }

    /**
     * Changes view on geo data requesting turn on.
     */
    public void onGeoDataTurnOn() {
        viewHolder.switchRequestLocation.setChecked(true);
        viewHolder.createMessage.setEnabled(true);
    }

    /**
     * Changes view on geo data requesting turn off.
     */
    public void onGeoDataTurnOff() {
        viewHolder.switchRequestLocation.setChecked(false);
        viewHolder.createMessage.setEnabled(false);
    }

    /**
     * Shows location updates.
     * @param location new location
     */
    public void onLocationChanged(Location location) {
        if (location != null) {
            viewHolder.locationCoordinates.setText(context.getString(R.string.label_coordinates, location.getLatitude(), location.getLongitude()));
        } else {
            viewHolder.locationCoordinates.setText(R.string.location_not_available);
        }
    }

    /**
     * Shows address updates.
     * @param address new address
     */
    public void onAddressChanged(List<String> address) {
        viewHolder.locationAddresses.setText(TextUtils.join(System.getProperty("line.separator"),address));
    }

    public void disableMessageCreating() {
        viewHolder.createMessage.setEnabled(false);
    }

    public void enableMessageCreating() {
        viewHolder.createMessage.setEnabled(true);
    }
}
