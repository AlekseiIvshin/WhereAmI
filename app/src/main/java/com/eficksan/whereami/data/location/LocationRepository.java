package com.eficksan.whereami.data.location;

import android.location.Location;

import java.util.List;

/**
 * Created by Aleksei Ivshin
 * on 21.08.2016.
 */
public interface LocationRepository {

    Location getLocation();
    List<String> getAddresses();

}
