package com.eficksan.whereami.presentation;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.eficksan.whereami.R;
import com.eficksan.whereami.fragments.SplashFragment;
import com.eficksan.whereami.geo.Constants;
import com.eficksan.whereami.geofence.GeofenceFragment;
import com.eficksan.whereami.presentation.location.WhereAmIFragment;
import com.eficksan.whereami.maps.MapsFragment;
import com.eficksan.whereami.routing.Router;
import com.eficksan.whereami.routing.Screens;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationSettingsResult;

/**
 * Main application activity.
 * Manages screen visibility.
 */
public class MainActivity extends AppCompatActivity implements Router {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private static final String ACTION_REQUEST_PERMISSIONS = MainActivity.class.getPackage().getName() + "/ACTION_REQUEST_PERMISSIONS";
    private static final String ACTION_SHOW_SCREEN = MainActivity.class.getPackage().getName() + "/ACTION_SHOW_SCREEN";


    private static final String ACTION_REQUEST_SETTINGS = MainActivity.class.getPackage().getName() + "/ACTION_REQUEST_SETTINGS";

    private static final String EXTRA_REQUESTED_PERMISSIONS = "EXTRA_REQUESTED_PERMISSIONS";
    private static final String EXTRA_REQUEST_PERMISSION_PENDING_INTENT = "EXTRA_REQUEST_PERMISSION_PENDING_INTENT";
    private static final String EXTRA_SCREEN_KEY = "EXTRA_SCREEN_KEY";
    private static final String EXTRA_PENDING_INTENT = "EXTRA_PENDING_INTENT";
    private static final String EXTRA_SETTINGS_STATUS = "EXTRA_SETTINGS_STATUS";

    private static final int ACTION_SHOW_LOCATION_SCREEN_CODE = 1;
    private static final int ACTION_REQUEST_PERMISSION_CODE = 2;
    private static final int ACTION_REQUEST_SETTINGS_CODE = 3;
    private static final int REQUEST_CHECK_SETTINGS = 3;

    /**
     * Creates pending intent for show location screen.
     * @param context some kind of context
     * @return pending intent
     */
    public static PendingIntent showLocationScreen(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setAction(ACTION_SHOW_SCREEN);
        intent.putExtra(EXTRA_SCREEN_KEY, Screens.SCREEN_WHERE_AM_I);
        return PendingIntent.getActivities(context, ACTION_SHOW_LOCATION_SCREEN_CODE, new Intent[]{intent}, 0);
    }

    /**
     * Creates pending intent for requesting permissions.
     * @param context some kind of context
     * @return pending intent
     */
    public static PendingIntent requestPermissions(Context context, String[] permissions, PendingIntent pendingIntent) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setAction(ACTION_REQUEST_PERMISSIONS);
        intent.putExtra(EXTRA_REQUESTED_PERMISSIONS, permissions);
        intent.putExtra(EXTRA_REQUEST_PERMISSION_PENDING_INTENT, pendingIntent);
        return PendingIntent.getActivities(context, ACTION_REQUEST_PERMISSION_CODE, new Intent[]{intent}, 0);
    }

    public static PendingIntent requestSettings(Context context, Status status, PendingIntent pendingIntent) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setAction(ACTION_REQUEST_SETTINGS);
        intent.putExtra(EXTRA_PENDING_INTENT, pendingIntent);
        intent.putExtra(EXTRA_SETTINGS_STATUS, status);
        return PendingIntent.getActivities(context, ACTION_REQUEST_SETTINGS_CODE, new Intent[]{intent}, 0);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            int screenKey = getIntent().getIntExtra(EXTRA_SCREEN_KEY, Screens.SCREEN_WHERE_AM_I);
            showScreen(screenKey, getIntent().getExtras());
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String action = intent.getAction();
        if (ACTION_SHOW_SCREEN.equals(action)) {
            int screenKey = intent.getIntExtra(EXTRA_SCREEN_KEY, Screens.SCREEN_WHERE_AM_I);
            showScreen(screenKey, intent.getExtras());
        }
        if (ACTION_REQUEST_PERMISSIONS.equals(action)) {
            String[] permissions = intent.getStringArrayExtra(EXTRA_REQUESTED_PERMISSIONS);
            ActivityCompat.requestPermissions(this, permissions, REQUEST_LOCATION_PERMISSION);
        }
        if (ACTION_REQUEST_SETTINGS.equals(action)) {
            Status status = intent.getParcelableExtra(EXTRA_SETTINGS_STATUS);
            // Location settings are not satisfied, but this can be fixed
            // by showing the user a dialog.
            try {
                // Show the dialog by calling startResolutionForResult(),
                // and check the result in onActivityResult().
                status.startResolutionForResult(
                       this,
                        REQUEST_CHECK_SETTINGS);
            } catch (IntentSender.SendIntentException e) {
                // Ignore the error.
            }
        }
    }

    public void replaceFragment(Fragment fragment, String tag, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();

        transaction.replace(R.id.fragment_container, fragment, tag);
        if (addToBackStack) {
            transaction.addToBackStack(tag);
        }
        transaction.commit();
    }

    private void showSplash() {
        replaceFragment(SplashFragment.newInstance(), SplashFragment.TAG, false);
    }

    @Override
    public void showScreen(int key, Bundle args) {
        switch (key) {
            case Screens.MAP_SCREEN: {
                Location location = args.getParcelable(Constants.EXTRA_LOCATION_DATA);
                replaceFragment(MapsFragment.newInstance(location), MapsFragment.TAG, false);
                break;
            }
            case Screens.GEOFENCES_SCREEN: {
                Location location = args.getParcelable(Constants.EXTRA_LOCATION_DATA);
                replaceFragment(GeofenceFragment.newInstance(location), GeofenceFragment.TAG, false);
                break;
            }
            case Screens.SPLASH_SCREEN:
                showSplash();
                break;
            case Screens.SCREEN_WHERE_AM_I:
            default:
                replaceFragment(WhereAmIFragment.newInstance(), WhereAmIFragment.TAG, false);
                break;

        }
    }

    @Override
    public void closeScreen(int key) {
        switch (key) {
            case Screens.SCREEN_WHERE_AM_I:
                getFragmentManager().popBackStack();
                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, R.string.permission_granted_try_again, Toast.LENGTH_SHORT).show();
                    PendingIntent pendingIntent = getIntent().getParcelableExtra(EXTRA_REQUEST_PERMISSION_PENDING_INTENT);
                    if (pendingIntent != null) {
                        try {
                            pendingIntent.send();
                        } catch (PendingIntent.CanceledException e) {
                            Log.e(TAG, e.getMessage(), e);
                        }
                    }
                } else {
                    Toast.makeText(this, R.string.permission_was_not_granted, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_CHECK_SETTINGS == requestCode) {
            Log.v(TAG, "On request check settings: resultCode = " + resultCode);
            switch (resultCode) {
                case RESULT_OK:
                    Toast.makeText(this, R.string.settings_satisfied, Toast.LENGTH_SHORT).show();
                    PendingIntent pendingIntent = getIntent().getParcelableExtra(EXTRA_PENDING_INTENT);
                    if (pendingIntent != null) {
                        try {
                            pendingIntent.send();
                        } catch (PendingIntent.CanceledException e) {
                            Log.e(TAG, e.getMessage(), e);
                        }
                    }
                    break;
                case RESULT_CANCELED:
                    Toast.makeText(this, R.string.settings_not_satisfied, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
