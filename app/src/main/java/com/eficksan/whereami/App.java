package com.eficksan.whereami;

import android.app.Activity;
import android.app.Application;

import com.eficksan.whereami.ioc.activity.ActivityComponent;
import com.eficksan.whereami.ioc.activity.ActivityModule;
import com.eficksan.whereami.ioc.activity.DaggerActivityComponent;
import com.eficksan.whereami.ioc.app.AppComponent;
import com.eficksan.whereami.ioc.app.AppModule;
import com.eficksan.whereami.ioc.app.DaggerAppComponent;
import com.eficksan.whereami.ioc.auth.AuthComponent;
import com.eficksan.whereami.ioc.auth.AuthModule;
import com.eficksan.whereami.ioc.auth.DaggerAuthComponent;
import com.eficksan.whereami.ioc.location.DaggerLocationComponent;
import com.eficksan.whereami.ioc.location.LocationComponent;
import com.eficksan.whereami.ioc.location.LocationModule;
import com.eficksan.whereami.ioc.maps.DaggerMapsComponent;
import com.eficksan.whereami.ioc.maps.MapsComponent;
import com.eficksan.whereami.ioc.maps.MapsModule;
import com.eficksan.whereami.ioc.message.DaggerMessageComponent;
import com.eficksan.whereami.ioc.message.MessageComponent;
import com.eficksan.whereami.ioc.message.MessageModule;
import com.eficksan.whereami.ioc.messaging.DaggerMessagingComponent;
import com.eficksan.whereami.ioc.messaging.MessagingComponent;
import com.eficksan.whereami.ioc.messaging.MessagingModule;

/**
 * Created by Aleksei Ivshin
 * on 28.08.2016.
 */
public class App extends Application {
    private AppComponent appComponent;
    private ActivityComponent activityComponent;
    private LocationComponent locationComponent;
    private MessagingComponent messagingComponent;
    private MapsComponent mapsComponent;
    private AuthComponent authComponent;
    private MessageComponent messageComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
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

    public MapsComponent plusMapsComponent() {
        if (mapsComponent == null) {
            mapsComponent = DaggerMapsComponent.builder()
                    .activityComponent(activityComponent)
                    .mapsModule(new MapsModule())
                    .build();
        }
        return mapsComponent;
    }

    public void removeMapsComponent() {
        mapsComponent = null;
    }

    public AuthComponent plusAuthComponent() {
        if (authComponent == null) {
            authComponent = DaggerAuthComponent.builder()
                    .activityComponent(activityComponent)
                    .authModule(new AuthModule())
                    .build();
        }
        return authComponent;
    }

    public void removeAuthComponent() {
        activityComponent = null;
    }


    public MessageComponent plusMessageDetailsComponent() {
        if (messageComponent == null) {
            messageComponent = DaggerMessageComponent.builder()
                    .activityComponent(activityComponent)
                    .messageModule(new MessageModule())
                    .build();
        }
        return messageComponent;
    }

    public void removeMessageDetailsComponent() {
        messageComponent = null;
    }
}
