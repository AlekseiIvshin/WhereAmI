package com.eficksan.whereami.ioc.app;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Aleksei Ivshin
 * on 28.08.2016.
 */
@Module
public class AppModule {
    public final Context applicationContext;

    public AppModule(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Provides
    @Singleton
    public Context provideContext() {
        return applicationContext;
    }

}
