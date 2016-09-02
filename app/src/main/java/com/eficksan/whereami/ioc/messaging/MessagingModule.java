package com.eficksan.whereami.ioc.messaging;

import android.app.Activity;

import com.eficksan.whereami.domain.messaging.MessagingInteractor;
import com.eficksan.whereami.ioc.fragments.FragmentScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Aleksei Ivshin
 * on 28.08.2016.
 */
@Module
@FragmentScope
public class MessagingModule {

    @Provides
    public MessagingInteractor provideMessagingInteractor(Activity activity) {
        return new MessagingInteractor(activity);
    }

}
