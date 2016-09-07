package com.eficksan.whereami.data.messages;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * POJO for creating message
 */
public class PlacingMessage {

    public double latitude;
    public double longitude;
    public String message;

    public PlacingMessage(double latitude, double longitude, String message) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.message = message;
    }
}
