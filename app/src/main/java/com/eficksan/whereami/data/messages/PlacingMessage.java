package com.eficksan.whereami.data.messages;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * POJO for creating message
 */
@IgnoreExtraProperties
public class PlacingMessage {

    public String messageId;
    public double latitude;
    public double longitude;
    public String message;
    public String userId;

    public PlacingMessage() {}

    public PlacingMessage(double latitude, double longitude, String message) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.message = message;
    }

    @Exclude
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlacingMessage that = (PlacingMessage) o;

        if (Double.compare(that.latitude, latitude) != 0) return false;
        if (Double.compare(that.longitude, longitude) != 0) return false;
        if (!message.equals(that.message)) return false;
        return userId.equals(that.userId);

    }

    @Exclude
    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(latitude);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + message.hashCode();
        result = 31 * result + userId.hashCode();
        return result;
    }
}
