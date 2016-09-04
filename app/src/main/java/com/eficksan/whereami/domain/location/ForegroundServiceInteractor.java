package com.eficksan.whereami.domain.location;

import android.app.ActivityManager;
import android.content.Context;

import com.eficksan.whereami.data.location.WhereAmILocationService;

/**
 * Interactor for starting and stopping location service in foreground.
 * <p/>
 * Created by Aleksei Ivshin
 * on 23.08.2016.
 */
public class ForegroundServiceInteractor {
    private boolean mIsNeedRequestLocation;

    private final Context context;

    public ForegroundServiceInteractor(Context context) {
        this.context = context;
    }

    /**
     * Starts location requesting service in foreground.
     */
    public void onStop() {
        if (mIsNeedRequestLocation) {
            context.startService(WhereAmILocationService.startForeground(context));
        }
    }

    /**
     * Stops foreground service.
     */
    public void onStart() {
        if (isMyServiceRunning(WhereAmILocationService.class)) {
            context.startService(WhereAmILocationService.stopForeground(context));
        }
    }

    /**
     * Saves requesting location state.
     *
     * @param isNeedRequestLocation is need request location
     */
    public void turnLocationRequesting(boolean isNeedRequestLocation) {
        mIsNeedRequestLocation = isNeedRequestLocation;
    }

    /**
     * Checks on is service running.
     *
     * @param serviceClass service class
     * @return true is service is running, false - otherwise
     */
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
