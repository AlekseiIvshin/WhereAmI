package com.eficksan.whereami.ioc.app;

import android.content.Context;

import com.eficksan.whereami.data.sync.SyncAdapter;
import com.eficksan.whereami.domain.messaging.MessagesContainer;

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
    MessagesContainer messagesContainer();

    void inject(SyncAdapter syncAdapter);
}