package com.eficksan.whereami.data.location;

import android.location.Address;
import android.location.Location;

import java.util.List;

/**
 * Location and address repository
 *
 * Created by Aleksei Ivshin
 * on 21.08.2016.
 */
public interface LocationRepository {

    /**
     * Get location from repository.
     * @return location
     */
    Location getLocation();

    /**
     * Get address from repository.
     * @return address
     */
    Address getAddress();
}
