package com.eficksan.whereami.presentation;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.eficksan.whereami.App;
import com.eficksan.whereami.R;
import com.eficksan.whereami.domain.Constants;
import com.eficksan.whereami.ioc.activity.ActivityComponent;
import com.eficksan.whereami.presentation.auth.signin.SignInFragment;
import com.eficksan.whereami.presentation.auth.signup.SignUpFragment;
import com.eficksan.whereami.presentation.location.WhereAmIFragment;
import com.eficksan.whereami.presentation.maps.MapMessagesFragment;
import com.eficksan.whereami.presentation.message.MessageDetailsFragment;
import com.eficksan.whereami.presentation.messaging.PlacingMessageFragment;
import com.eficksan.whereami.presentation.routing.Router;
import com.eficksan.whereami.presentation.routing.Screens;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

/**
 * Main application activity.
 * Manages screen visibility.
 */
public class MainActivity extends AppCompatActivity implements Router {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String ACTION_SHOW_SCREEN = MainActivity.class.getPackage().getName() + "/ACTION_SHOW_SCREEN";
    private static final String ACTION_REQUEST_PERMISSIONS = MainActivity.class.getPackage().getName() + "/ACTION_PERMISSIONS_REQUEST_RESULT";

    private static final String ACTION_REQUEST_SETTINGS = MainActivity.class.getPackage().getName() + "/ACTION_SETTINGS_REQUEST_RESULT";

    private static final String EXTRA_REQUESTED_PERMISSIONS = "EXTRA_REQUESTED_PERMISSIONS";
    private static final String EXTRA_SCREEN_KEY = "EXTRA_SCREEN_KEY";
    private static final String EXTRA_SETTINGS_STATUS = "EXTRA_SETTINGS_STATUS";

    private static final int ACTION_SHOW_LOCATION_SCREEN_CODE = 1;
    private static final int ACTION_REQUEST_PERMISSION_CODE = 2;
    private static final int ACTION_REQUEST_SETTINGS_CODE = 3;

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private static final int REQUEST_CHECK_SETTINGS = 2;
    private int mCurrentScreenKey = Screens.NONE;
    private DrawerLayout mDrawerLayout;

    @Inject
    FirebaseAuth firebaseAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;
    private boolean mIsDestroyBySystem;

    /**
     * Creates pending intent for show location screen.
     *
     * @param context some kind of context
     * @return pending intent
     */
    public static PendingIntent showLocationScreen(Context context) {
        Log.v(TAG, "Request show location screen");
        Intent intent = new Intent(context, MainActivity.class);
        intent.setAction(ACTION_SHOW_SCREEN);
        intent.putExtra(EXTRA_SCREEN_KEY, Screens.LOCATION_SCREEN);
        return PendingIntent.getActivities(context, ACTION_SHOW_LOCATION_SCREEN_CODE, new Intent[]{intent}, 0);
    }

    /**
     * Creates pending intent for requesting permissions.
     *
     * @param context     some kind of context
     * @param permissions required permissions
     * @return pending intent
     */
    public static PendingIntent requestPermissions(Context context, String[] permissions) {
        Log.v(TAG, "Request permissions: " + TextUtils.join(", ", permissions));
        Intent intent = new Intent(context, MainActivity.class);
        intent.setAction(ACTION_REQUEST_PERMISSIONS);
        intent.putExtra(EXTRA_REQUESTED_PERMISSIONS, permissions);
        return PendingIntent.getActivities(context, ACTION_REQUEST_PERMISSION_CODE, new Intent[]{intent}, 0);
    }

    /**
     * Creates pending intetnt for requesting settings.
     *
     * @param context some kind of context
     * @param status  settings status
     * @return pending intent
     */
    public static PendingIntent requestSettings(Context context, Status status) {
        Log.v(TAG, "Request settings: status = " + status.toString());
        Intent intent = new Intent(context, MainActivity.class);
        intent.setAction(ACTION_REQUEST_SETTINGS);
        intent.putExtra(EXTRA_SETTINGS_STATUS, status);
        return PendingIntent.getActivities(context, ACTION_REQUEST_SETTINGS_CODE, new Intent[]{intent}, 0);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        String action = intent.getAction();
        Log.v(TAG, "On receive new intent. Action = " + action);
        if (ACTION_SHOW_SCREEN.equals(action)) {
            int screenKey = intent.getIntExtra(EXTRA_SCREEN_KEY, Screens.LOCATION_SCREEN);
            showScreen(screenKey, intent.getExtras());
        }
        if (ACTION_REQUEST_PERMISSIONS.equals(action)) {
            String[] permissions = intent.getStringArrayExtra(EXTRA_REQUESTED_PERMISSIONS);
            ActivityCompat.requestPermissions(this, permissions, REQUEST_LOCATION_PERMISSION);
        }
        if (ACTION_REQUEST_SETTINGS.equals(action)) {
            Status status = intent.getParcelableExtra(EXTRA_SETTINGS_STATUS);
            try {
                status.startResolutionForResult(
                        this,
                        REQUEST_CHECK_SETTINGS);
            } catch (IntentSender.SendIntentException e) {
                // Ignore the error.
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityComponent activityComponent = ((App) getApplication()).plusActivityComponent(this);
        activityComponent.inject(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        initNavigationView();

        if (savedInstanceState == null && mCurrentScreenKey == Screens.NONE) {
            if (activityComponent.currentUser() == null) {
                showScreen(Screens.SIGN_IN_SCREEN);
            } else {
                showScreen(Screens.LOCATION_SCREEN);
            }
        }

        mAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Log.v(TAG, "User signed OUT");
                    showScreen(Screens.SIGN_IN_SCREEN);
                } else {
                    Log.v(TAG, "User signed IN");
                    showScreen(Screens.LOCATION_SCREEN);
                }
            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIsDestroyBySystem = false;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mIsDestroyBySystem = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            firebaseAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onDestroy() {
        if (!mIsDestroyBySystem) {
            ((App) getApplication()).removeActivityComponent();
        }
        super.onDestroy();
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

    public void addFragment(Fragment fragment, String tag, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();

        transaction.add(R.id.fragment_container, fragment, tag);
        if (addToBackStack) {
            transaction.addToBackStack(tag);
        }
        transaction.commit();
    }

    @Override
    public void showScreen(int nextScreenKey) {
        showScreen(nextScreenKey, Bundle.EMPTY);
    }

    @Override
    public void showScreen(int nextScreenKey, Bundle args) {
        Log.v(TAG, String.format("Show screen: current screen key = %d, next screen key = %d", mCurrentScreenKey, nextScreenKey));
        if (mCurrentScreenKey == nextScreenKey) {
            return;
        }
        if (Screens.MESSAGE_DETAILS != nextScreenKey) {
            mCurrentScreenKey = nextScreenKey;
        }
        switch (nextScreenKey) {
            case Screens.MESSAGING_SCREEN: {
                Location location = args.getParcelable(Constants.EXTRA_LOCATION_DATA);
                addFragment(PlacingMessageFragment.newInstance(location), PlacingMessageFragment.TAG, true);
                break;
            }
            case Screens.MAPS_SCREEN: {
                replaceFragment(MapMessagesFragment.newInstance(), MapMessagesFragment.TAG, true);
                break;
            }
            case Screens.LOCATION_SCREEN:
                replaceFragment(WhereAmIFragment.newInstance(), WhereAmIFragment.TAG, false);
                break;
            case Screens.SIGN_IN_SCREEN:
                replaceFragment(SignInFragment.newInstance(), SignInFragment.TAG, false);
                break;
            case Screens.SIGN_UP_SCREEN:
                replaceFragment(SignUpFragment.newInstance(), SignUpFragment.TAG, false);
                break;
            case Screens.MESSAGE_DETAILS: {
                String messageId = args.getString(Constants.EXTRA_MESSAGE_ID);
                MessageDetailsFragment fragment = MessageDetailsFragment.newInstance(messageId);
                fragment.show(getSupportFragmentManager(), MessageDetailsFragment.TAG);
                break;
            }

        }
    }

    @Override
    public void closeScreen(int key) {
        Log.v(TAG, String.format("Close screen: current screen key = %d", mCurrentScreenKey));
        switch (key) {
            case Screens.LOCATION_SCREEN:
            case Screens.SIGN_IN_SCREEN:
                finish();
                break;
            case Screens.MAPS_SCREEN:
            case Screens.MESSAGING_SCREEN:
            default:
                getSupportFragmentManager().popBackStack();
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
                    LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(getIntent().getAction()));
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
                    LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(getIntent().getAction()));
                    break;
                case RESULT_CANCELED:
                    Toast.makeText(this, R.string.settings_not_satisfied, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void initNavigationView() {
        //Initializing NavigationView
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        if (navigationView == null) {
            return;
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        if (mDrawerLayout == null) {
            return;
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }

                mDrawerLayout.closeDrawers();

                switch (menuItem.getItemId()) {
                    case R.id.menu_where_am_i:
                        showScreen(Screens.LOCATION_SCREEN);
                        break;
                    case R.id.menu_maps:
                        showScreen(Screens.MAPS_SCREEN);
                        break;
                    case R.id.sign_out:
                        firebaseAuth.signOut();
                        break;
                }
                return true;
            }
        });
    }
}
