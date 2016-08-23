package com.eficksan.whereami.data.location;

import android.location.Location;

import java.util.List;

/**
 * Created by Aleksei Ivshin
 * on 21.08.2016.
 */
public class WaiEvent {

    public final Location location;
    public final List<String> addresses;

    public WaiEvent(Location location, List<String> addresses) {
        this.location = location;
        this.addresses = addresses;
    }
}
