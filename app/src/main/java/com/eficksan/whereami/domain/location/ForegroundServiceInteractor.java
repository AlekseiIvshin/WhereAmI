package com.eficksan.whereami.domain.location;

import android.app.Activity;

import com.eficksan.whereami.data.location.WhereAmILocationService;

import java.lang.ref.WeakReference;

/**
 * Interactor for starting and stopping location service in foreground.
 *
 * Created by Aleksei Ivshin
 * on 23.08.2016.
 */
public class ForegroundServiceInteractor {
    private final WeakReference<Activity> refActivityContext;

    public ForegroundServiceInteractor(Activity activity) {
        this.refActivityContext = new WeakReference<>(activity);
    }

    /**
     * Starts location requesting service in foreground.
     */
    public void startForeground(){
        Activity activity = refActivityContext.get();
        if(activity != null) {
            activity.startService(WhereAmILocationService.startForeground(activity));
        }
    }

    /**
     * Stops foreground service.
     */
    public void stopForeground() {
        Activity activity = refActivityContext.get();
        if(activity != null) {
            activity.startService(WhereAmILocationService.stopForeground(activity));
        }
    }
}
