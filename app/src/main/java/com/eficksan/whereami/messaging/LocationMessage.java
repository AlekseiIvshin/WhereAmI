package com.eficksan.whereami.messaging;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Locale;

/**
 * POJO contains just lat and long of location and message text.
 *
 * Created by Aleksei Ivshin
 * on 30.06.2016.
 */
public class LocationMessage implements Parcelable {

    public final double latitude;
    public final double longitude;
    public final String message;

    public LocationMessage(Location location, String message) {
        this.longitude = location.getLongitude();
        this.latitude = location.getLatitude();
        this.message = message;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "Location: %f x %f; Message: '%s'", latitude, longitude, message);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        dest.writeString(this.message);
    }

    protected LocationMessage(Parcel in) {
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.message = in.readString();
    }

    public static final Parcelable.Creator<LocationMessage> CREATOR = new Parcelable.Creator<LocationMessage>() {
        @Override
        public LocationMessage createFromParcel(Parcel source) {
            return new LocationMessage(source);
        }

        @Override
        public LocationMessage[] newArray(int size) {
            return new LocationMessage[size];
        }
    };
}
