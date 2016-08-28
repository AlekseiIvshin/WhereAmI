package com.eficksan.whereami.ioc.fragments;

import android.app.Activity;

import com.eficksan.whereami.presentation.routing.Router;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Aleksei Ivshin
 * on 28.08.2016.
 */
@Module
public class FragmentModule {

    public final Activity activity;

    public FragmentModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    @FragmentScope
    public Activity provideActivity() {
        return activity;
    }

    @Provides
    @FragmentScope
    public Router provideRouter() {
        return (Router) activity;
    }
}
