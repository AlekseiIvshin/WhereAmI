package com.eficksan.whereami.ioc.activity;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;

import com.eficksan.whereami.ioc.app.AppComponent;
import com.eficksan.whereami.presentation.MainActivity;
import com.eficksan.whereami.presentation.routing.Router;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import dagger.Component;

/**
 * Created by Aleksei Ivshin
 * on 28.08.2016.
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    Context applicationContext();

    FirebaseAuth firebaseAuth();

    @Nullable
    FirebaseUser currentUser();

    Router router();

    Activity activity();

    void inject(MainActivity mainActivity);
}
