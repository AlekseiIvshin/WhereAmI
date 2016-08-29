package com.eficksan.whereami;

import android.app.Activity;
import android.app.Application;

import com.eficksan.whereami.ioc.activity.ActivityComponent;
import com.eficksan.whereami.ioc.activity.ActivityModule;
import com.eficksan.whereami.ioc.activity.DaggerActivityComponent;
import com.eficksan.whereami.ioc.app.AppComponent;
import com.eficksan.whereami.ioc.app.AppModule;
import com.eficksan.whereami.ioc.app.DaggerAppComponent;
import com.eficksan.whereami.ioc.location.DaggerLocationComponent;
import com.eficksan.whereami.ioc.location.LocationComponent;
import com.eficksan.whereami.ioc.location.LocationModule;
import com.eficksan.whereami.ioc.messaging.DaggerMessagingComponent;
import com.eficksan.whereami.ioc.messaging.MessagingComponent;
import com.eficksan.whereami.ioc.messaging.MessagingModule;

/**
 * Created by Aleksei Ivshin
 * on 28.08.2016.
 */
public class App extends Application {
    AppComponent appComponent;
    ActivityComponent activityComponent;
    LocationComponent locationComponent;
    MessagingComponent messagingComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public ActivityComponent plusActivityComponent(Activity activity) {
        if (activityComponent == null) {
            activityComponent = DaggerActivityComponent.builder()
                    .appComponent(appComponent)
                    .activityModule(new ActivityModule(activity))
                    .build();
        }
        return activityComponent;
    }

    public void removeActivityComponent() {
        activityComponent = null;
    }

    public LocationComponent plusLocationComponent() {
        if (locationComponent == null) {
            locationComponent = DaggerLocationComponent.builder()
                    .activityComponent(activityComponent)
                    .locationModule(new LocationModule())
                    .build();
        }
        return locationComponent;
    }

    public void removeLocationComponent() {
        locationComponent = null;
    }

    public MessagingComponent plusMessagingComponent() {
        if (messagingComponent == null) {
            messagingComponent = DaggerMessagingComponent.builder()
                    .activityComponent(activityComponent)
                    .messagingModule(new MessagingModule())
                    .build();
        }
        return messagingComponent;
    }

    public void removeMessagingComponent() {
        messagingComponent = null;
    }
}
