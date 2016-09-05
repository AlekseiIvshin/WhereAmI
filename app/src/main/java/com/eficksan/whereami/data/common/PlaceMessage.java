package com.eficksan.whereami.data.common;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Aleksei Ivshin
 * on 28.08.2016.
 */
public class PlaceMessage {
    public final String id;
    public final double latitude;
    public final double longitude;
    public final String message;
    public final User user;
    public final long timeStamp;
    public final long rating;

    public PlaceMessage(String id, double latitude, double longitude, String message, User user, long timeStamp, long rating) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.message = message;
        this.user = user;
        this.timeStamp = timeStamp;
        this.rating = rating;
    }
}
