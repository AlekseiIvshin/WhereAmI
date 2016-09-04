package com.eficksan.whereami.data.location;

import android.location.Address;
import android.location.Location;

/**
 * Created by Aleksei Ivshin
 * on 21.08.2016.
 */
public class LocationAddress {

    public final Location location;
    public final Address address;

    public LocationAddress(Location location, Address address) {
        this.location = location;
        this.address = address;
    }
}
