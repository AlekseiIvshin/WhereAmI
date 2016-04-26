package com.eficksan.whereami.geo;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import com.eficksan.whereami.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * This class helps to get location address.
 */
public class FetchAddressIntentService extends IntentService {

    public static final String TAG = FetchAddressIntentService.class.getSimpleName();

    protected ResultReceiver mReceiver;

    public FetchAddressIntentService() {
        super(TAG);
    }

    public static void requestLocationAddresses(Context context, ResultReceiver resultReceiver, Location location) {
        Intent intent = new Intent(context, FetchAddressIntentService.class);
        intent.putExtra(Constants.EXTRA_LOCATION_DATA, location);
        intent.putExtra(Constants.RECEIVER, resultReceiver);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        Location location = intent.getParcelableExtra(Constants.EXTRA_LOCATION_DATA);

        mReceiver = intent.getParcelableExtra(Constants.RECEIVER);
        String errorMessage = "";
        List<Address> addressList = null;
        try {
            addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch (IOException e) {
            errorMessage = getString(R.string.service_not_available);
            Log.e(TAG, e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            errorMessage = getString(R.string.wrong_lat_lon_used);
            Log.e(TAG, e.getMessage() + ". Location: " + location, e);
        }

        if (addressList == null || addressList.isEmpty()) {
            if (errorMessage.isEmpty()) {
                errorMessage = getString(R.string.address_not_found);
                Log.e(TAG, errorMessage);
            }
            deliverResultToReceiver(Constants.FAILURE_RESULT, errorMessage);
        } else {
            Address address = addressList.get(0);
            ArrayList<String> addressFragments = new ArrayList<String>();

            // Fetch the address lines using getAddressLine,
            // join them, and send them to the thread.
            for(int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                addressFragments.add(address.getAddressLine(i));
            }
            Log.i(TAG, getString(R.string.address_found));
            deliverResultToReceiver(Constants.SUCCESS_RESULT,
                    TextUtils.join(System.getProperty("line.separator"),
                            addressFragments));
        }
    }

    private void deliverResultToReceiver(int resultCode, String message) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.RESULT_DATA_KEY, message);
        mReceiver.send(resultCode, bundle);
    }

}
