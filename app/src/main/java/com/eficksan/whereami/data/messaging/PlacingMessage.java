package com.eficksan.whereami.data.messaging;

/**
 * POJO for creating message
 */
public class PlacingMessage {

    public final double latitude;
    public final double longitude;
    public final String message;

    public PlacingMessage(double latitude, double longitude, String message) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.message = message;
    }
}
