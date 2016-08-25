package com.eficksan.whereami.domain;

/**
 * Created by Aleksei Ivshin
 * on 26.04.2016.
 */
public final class Constants {
    public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT = 1;
    public static final String PACKAGE_NAME =
            "com.eficksan.whereami";
    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    public static final String RESULT_DATA_KEY = PACKAGE_NAME +
            ".RESULT_DATA_KEY";
    public static final String EXTRA_LOCATION_DATA = PACKAGE_NAME +
            ".EXTRA_LOCATION_DATA";
    public static final String EXTRA_MESSAGE_DATA = PACKAGE_NAME +
            ".EXTRA_MESSAGE_DATA";
    public static final String EXTRA_RESULT_RECEIVER = PACKAGE_NAME +
            ".EXTRA_RESULT_RECEIVER";

    public static final String EXTRA_GEOFENCSE_TRANSITION = "EXTRA_GEOFENCSE_TRANSITION";
    public static final String EXTRA_GEOFENCES_REQUEST_ID = "EXTRA_GEOFENCES_REQUEST_ID";

    public static final int GEOFENCE_TRANSITION_INVALID_TYPE = -42;
    public static final int MAX_MESSAGE_SIZE = 120;

    public static final String PREF_NAME = "prefs_whereami";

    public static final String PREF_KEY_START_TRACK_LOCATION_ON_BOOT = "start_track_location_on_boot";
    public static final String PREF_KEY_START_FOREGROUND = "PREF_KEY_START_FOREGROUND";
}