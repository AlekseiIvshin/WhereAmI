package com.eficksan.whereami.ioc.app;

import android.content.Context;

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
}
