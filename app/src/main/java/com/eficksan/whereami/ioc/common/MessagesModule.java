package com.eficksan.whereami.ioc.common;

import com.eficksan.whereami.data.messages.MessagesDataSource;
import com.eficksan.whereami.domain.messages.FindMessageInteractor;
import com.eficksan.whereami.domain.messages.MessagesFetchingInteractor;
import com.eficksan.whereami.domain.messages.PlaceMessageValidator;
import com.eficksan.whereami.domain.messages.PlacingMessageInteractor;
import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import rx.Scheduler;

/**
 * Created by Aleksei Ivshin
 * on 28.08.2016.
 */
@Module
public class MessagesModule {


    @Provides
    public MessagesFetchingInteractor provideMessagesFetchingInteractor(
            MessagesDataSource messagesDataSource,
            @Named("job") Scheduler jobScheduler, @Named("ui") Scheduler uiScheduler) {
        return new MessagesFetchingInteractor(messagesDataSource, jobScheduler, uiScheduler);
    }

    @Provides
    public FindMessageInteractor provideFindMessageInteractor(
            MessagesDataSource messagesDataSource,
            @Named("job") Scheduler jobScheduler, @Named("ui") Scheduler uiScheduler) {
        return new FindMessageInteractor(messagesDataSource, jobScheduler, uiScheduler);
    }

    @Provides
    public PlaceMessageValidator providePlaceMessageValidator() {
        return new PlaceMessageValidator();
    }

    @Provides
    public PlacingMessageInteractor providePlacingMessageInteractor(
            MessagesDataSource messagesDataSource,
            FirebaseAuth firebaseAuth,
            @Named("job") Scheduler jobScheduler, @Named("ui") Scheduler uiScheduler) {
        return new PlacingMessageInteractor(messagesDataSource, firebaseAuth, uiScheduler, jobScheduler);
    }
}
