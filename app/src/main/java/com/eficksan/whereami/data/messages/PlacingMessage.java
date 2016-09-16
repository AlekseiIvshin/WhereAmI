package com.eficksan.whereami.data.messages;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * POJO for creating message.
 */
@IgnoreExtraProperties
public class PlacingMessage {

    public String messageId;
    public double latitude;
    public double longitude;
    public String message;
    public String userId;

    public PlacingMessage() {
    }

    public PlacingMessage(double latitude, double longitude, String message) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.message = message;
    }

    public PlacingMessage(PlacingMessage message, String userId) {
        this.latitude = message.latitude;
        this.longitude = message.longitude;
        this.message = message.message;
        this.userId = userId;
    }

}
