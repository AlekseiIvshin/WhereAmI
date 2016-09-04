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

    String ACTION_PERMISSIONS_REQUEST_RESULT = "ACTION_PERMISSIONS_REQUEST_RESULT";
    String ACTION_SETTINGS_REQUEST_RESULT = "ACTION_SETTINGS_REQUEST_RESULT";

}