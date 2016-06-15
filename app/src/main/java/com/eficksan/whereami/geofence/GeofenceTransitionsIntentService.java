package com.eficksan.whereami.geofence;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import com.eficksan.whereami.R;
import com.eficksan.whereami.geo.Constants;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

public class GeofenceTransitionsIntentService extends IntentService {

    public static final String TAG = GeofenceTransitionsIntentService.class.getSimpleName();
    private static final String EXTRA_RECEIVER = TAG + "/EXTRA_RECEIVER";
    private ResultReceiver mReceiver;

    public GeofenceTransitionsIntentService() {
        super(TAG);
    }

    public static Intent getGeofenceTransitionIntent(Context context, ResultReceiver resultReceiver) {
        Intent intent = new Intent(context, GeofenceTransitionsIntentService.class);
        intent.putExtra(EXTRA_RECEIVER, resultReceiver);
        return intent;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            Log.e(TAG, "There is an error with code " + geofencingEvent.getErrorCode());
            deliverFailureResultToReceiver(geofencingEvent.getErrorCode());
            return;
        }
        mReceiver = intent.getParcelableExtra(Constants.RECEIVER);
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        if (Geofence.GEOFENCE_TRANSITION_ENTER == geofenceTransition
                || Geofence.GEOFENCE_TRANSITION_EXIT == geofenceTransition) {
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

            String geofenceTransitionDetails = getGeofenceTransitionDetails(
                    geofenceTransition,
                    triggeringGeofences
            );

            deliverSuccessResultToReceiver(geofenceTransition, triggeringGeofences);
            Log.i(TAG, "Transition details: " + geofenceTransitionDetails);
        } else {
            deliverFailureResultToReceiver(Constants.GEOFENCE_TRANSITION_INVALID_TYPE);
            Log.e(TAG, getString(R.string.geofence_transition_invalid_type, geofenceTransition));
        }
    }

    private void deliverSuccessResultToReceiver(int geofenceTransistion,
                                                List<Geofence> triggeringGeofences) {
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.EXTRA_GEOFENCSE_TRANSITION, geofenceTransistion);
        bundle.putStringArray(Constants.EXTRA_GEOFENCSE_TRANSITION, geofencesToStringArray(triggeringGeofences));
        mReceiver.send(Constants.SUCCESS_RESULT, bundle);
    }

    private void deliverFailureResultToReceiver(int errorCode) {
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.RESULT_DATA_KEY, errorCode);
        mReceiver.send(Constants.FAILURE_RESULT, bundle);
    }

    private static String getGeofenceTransitionDetails(int geofenceTransistion, List<Geofence> triggeringGeofences) {
        return String.format(
                "Transition type %d, triggering geofences: %s",
                geofenceTransistion,
                TextUtils.join(", ", geofencesToStringArray(triggeringGeofences)));
    }

    private static String[] geofencesToStringArray(List<Geofence> geofences) {
        int size = geofences.size();
        String[] geofencesArray = new String[size];
        for (int i = 0; i < size; i++) {
            geofencesArray[i] = geofences.get(i).getRequestId();
        }
        return geofencesArray;
    }
}
