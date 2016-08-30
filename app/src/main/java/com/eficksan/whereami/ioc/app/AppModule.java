package com.eficksan.whereami.ioc.app;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

import com.eficksan.whereami.domain.Constants;
import com.eficksan.whereami.domain.messaging.MessagesContainer;
import com.eficksan.whereami.ioc.activity.ActivityScope;

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

    @Provides
    @Singleton
    public MessagesContainer provideMessagesContainer() {
        return new MessagesContainer();
    }
}
