package com.eficksan.whereami;

import android.app.Application;

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
    protected AppComponent appComponent;
    protected LocationComponent locationComponent;
    protected MessagingComponent messagingComponent;
    protected MapsComponent mapsComponent;
    protected AuthComponent authComponent;
    protected MessageComponent messageComponent;

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

    public LocationComponent plusLocationComponent() {
        if (locationComponent == null) {
            locationComponent = DaggerLocationComponent.builder()
                    .appComponent(appComponent)
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
                    .appComponent(appComponent)
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
                    .appComponent(appComponent)
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
                    .appComponent(appComponent)
                    .authModule(new AuthModule())
                    .build();
        }
        return authComponent;
    }

    public void removeAuthComponent() {
        authComponent = null;
    }


    public MessageComponent plusMessageDetailsComponent() {
        if (messageComponent == null) {
            messageComponent = DaggerMessageComponent.builder()
                    .appComponent(appComponent)
                    .messageModule(new MessageModule())
                    .build();
        }
        return messageComponent;
    }

    public void removeMessageDetailsComponent() {
        messageComponent = null;
    }
}
