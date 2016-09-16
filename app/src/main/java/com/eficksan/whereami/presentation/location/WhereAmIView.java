package com.eficksan.whereami.presentation.location;

import android.content.Context;
import android.location.Address;
import android.location.Location;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.eficksan.whereami.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * View part of location request flow.
 * <p/>
 * Manages location controls and location requests results.
 * <p/>
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

    final Context context;

    public WhereAmIView(Context context) {
        this.context = context;
    }

    /**
     * Take view of component.
     *
     * @param view view
     */
    public void takeView(View view) {
        ButterKnife.bind(this, view);
    }


    public void releaseView() {
        locationAddresses = null;
        locationCoordinates = null;
        switchRequestLocation = null;
        createMessage = null;
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
     *
     * @param location new location
     */
    public void onLocationChanged(Location location) {
        if (location != null) {
            locationCoordinates.setText(
                    context.getString(R.string.label_coordinates, location.getLatitude(), location.getLongitude()));
        } else {
            locationCoordinates.setText(R.string.location_not_available);
        }
    }

    /**
     * Shows address updates.
     *
     * @param address new address
     */
    public void onAddressChanged(Address address) {
        ArrayList<String> addressFragments = new ArrayList<>();
        for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
            addressFragments.add(address.getAddressLine(i));
        }
        locationAddresses.setText(TextUtils.join(System.getProperty("line.separator"), addressFragments));
    }

    public void disableMessageCreating() {
        createMessage.setVisibility(View.GONE);
    }

    public void enableMessageCreating() {
        createMessage.setVisibility(View.VISIBLE);
    }

    public void showError(int errorResId) {
        Toast.makeText(context, errorResId, Toast.LENGTH_SHORT).show();
    }
}
