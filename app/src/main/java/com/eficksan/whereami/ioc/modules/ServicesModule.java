package com.eficksan.whereami.ioc.modules;

import android.content.Context;
import android.location.LocationManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Aleksei Ivshin
 * on 25.04.2016.
 */
@Module
public class ServicesModule {
    private final Context context;

    public ServicesModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    public LocationManager provideLocationManager() {
        return (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }
}
