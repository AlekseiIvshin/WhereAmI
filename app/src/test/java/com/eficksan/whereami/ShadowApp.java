package com.eficksan.whereami;

import android.app.Activity;

import com.eficksan.whereami.ioc.activity.ActivityComponent;
import com.eficksan.whereami.ioc.activity.ActivityModule;
import com.eficksan.whereami.ioc.activity.DaggerActivityComponent;
import com.eficksan.whereami.ioc.activity.TestActivityModule;
import com.eficksan.whereami.ioc.app.DaggerAppComponent;
import com.eficksan.whereami.ioc.app.TestAppModule;
import com.eficksan.whereami.ioc.auth.AuthComponent;
import com.eficksan.whereami.ioc.auth.AuthModule;
import com.eficksan.whereami.ioc.auth.DaggerAuthComponent;
import com.eficksan.whereami.ioc.auth.TestAuthModule;

/**
 * Created by Aleksei_Ivshin on 9/12/16.
 */
public class ShadowApp extends App {

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                .appModule(new TestAppModule(this))
                .build();
    }


    public ActivityComponent plusActivityComponent(Activity activity) {
        if (activityComponent == null) {
            activityComponent = DaggerActivityComponent.builder()
                    .appComponent(appComponent)
                    .activityModule(new TestActivityModule(activity))
                    .build();
        }
        return activityComponent;
    }

    public void removeActivityComponent() {
        activityComponent = null;
    }

    public AuthComponent plusAuthComponent() {
        if (authComponent == null) {
            authComponent = DaggerAuthComponent.builder()
                    .activityComponent(activityComponent)
                    .authModule(new TestAuthModule())
                    .build();
        }
        return authComponent;
    }

    public void removeAuthComponent() {
        activityComponent = null;
    }
}
