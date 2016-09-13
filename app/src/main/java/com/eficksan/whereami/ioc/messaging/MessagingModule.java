package com.eficksan.whereami.ioc.messaging;

import com.eficksan.whereami.data.messages.MessagesDataSource;
import com.eficksan.whereami.domain.messages.PlaceMessageValidator;
import com.eficksan.whereami.domain.messages.PlacingMessageInteractor;
import com.eficksan.whereami.ioc.fragments.FragmentScope;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import rx.Scheduler;

/**
 * Created by Aleksei Ivshin
 * on 28.08.2016.
 */
@Module
@FragmentScope
public class MessagingModule {

    @Provides
    public PlaceMessageValidator providePlaceMessageValidator() {
        return new PlaceMessageValidator();
    }

    @Provides
    public PlacingMessageInteractor providePlacingMessageInteractor(MessagesDataSource messagesDataSource, @Named("job") Scheduler jobScheduler, @Named("ui") Scheduler uiScheduler) {
        return new PlacingMessageInteractor(messagesDataSource, jobScheduler, uiScheduler);
    }

}
