package com.eficksan.whereami.ioc.app;

import android.content.Context;

import com.eficksan.whereami.data.sync.SyncAdapter;
import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Aleksei Ivshin
 * on 28.08.2016.
 */
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    Context applicationContext();
    FirebaseAuth firebaseAuth();

    void inject(SyncAdapter syncAdapter);
}
