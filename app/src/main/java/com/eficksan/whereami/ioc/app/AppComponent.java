package com.eficksan.whereami.ioc.app;

import android.content.Context;
import android.support.annotation.Nullable;

import com.eficksan.whereami.data.sync.SyncAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

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
    FirebaseDatabase firebaseDatabase();

    @Nullable
    FirebaseUser currentUser();

    void inject(SyncAdapter syncAdapter);
}
