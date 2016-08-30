package com.eficksan.whereami.presentation.location;

import android.content.Context;
import android.location.Location;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.eficksan.placingmessages.PlaceMessage;
import com.eficksan.whereami.R;

import java.util.List;

import javax.inject.Inject;

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

    @Inject
    Context context;

    public WaiView(WaiViewHolder viewHolder) {
        this.viewHolder = viewHolder;
    }

    /**
     * Changes view on geo data requesting turn on.
     */
    public void onGeoDataTurnOn() {
        viewHolder.switchRequestLocation.setChecked(true);
        viewHolder.locationCoordinates.setText(R.string.location_request_in_progress);
        viewHolder.locationAddresses.setText("");
    }

    /**
     * Changes view on geo data requesting turn off.
     */
    public void onGeoDataTurnOff() {
        viewHolder.switchRequestLocation.setChecked(false);
        viewHolder.locationCoordinates.setText("");
        viewHolder.locationAddresses.setText("");
        disableMessageCreating();
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
        viewHolder.createMessage.setVisibility(View.GONE);
    }

    public void enableMessageCreating() {
        viewHolder.createMessage.setVisibility(View.VISIBLE);
    }

    public void onLocationHistoryLoaded(Location location) {
        if (location != null) {
            viewHolder.locationHistoryLast.setText(context.getString(R.string.label_coordinates, location.getLatitude(), location.getLongitude()));
        } else {
            viewHolder.locationHistoryLast.setText(R.string.location_not_available);
        }
    }

    public void updateMessages(List<PlaceMessage> messages) {
        viewHolder.messagesAdapter.setMessages(messages);
    }

    public void notifySyncResult(int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }
}
