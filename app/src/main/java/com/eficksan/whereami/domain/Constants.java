package com.eficksan.whereami.domain;

/**
 * Created by Aleksei Ivshin
 * on 26.04.2016.
 */
public interface Constants {
    int SUCCESS_RESULT = 0;
    int FAILURE_RESULT = 1;
    String PACKAGE_NAME =
            "com.eficksan.whereami";
    String EXTRA_LOCATION_DATA = PACKAGE_NAME +
            ".EXTRA_LOCATION_DATA";

    int MAX_MESSAGE_SIZE = 120;

    String PREF_NAME = "prefs_whereami";

    String PREF_KEY_START_TRACK_LOCATION_ON_BOOT = "start_track_location_on_boot";
}