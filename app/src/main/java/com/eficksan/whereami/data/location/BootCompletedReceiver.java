package com.eficksan.whereami.data.location;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.eficksan.whereami.domain.Constants;

public class BootCompletedReceiver extends BroadcastReceiver {
    private static final String TAG = BootCompletedReceiver.class.getSimpleName();

    public BootCompletedReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v(TAG, "Device boot completed.");
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        if (sharedPreferences.getBoolean(Constants.PREF_KEY_START_TRACK_LOCATION_ON_BOOT, false)) {
            context.startService(WhereAmILocationService.startForeground(context));
        }
    }
}
