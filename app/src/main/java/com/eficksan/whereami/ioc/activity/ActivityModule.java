package com.eficksan.whereami.ioc.activity;

import android.app.Activity;

import com.eficksan.whereami.presentation.routing.Router;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Aleksei Ivshin
 * on 28.08.2016.
 */
@Module
public class ActivityModule {

    private final Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    @ActivityScope
    public Router provideRouter() {
        return (Router) activity;
    }

}
