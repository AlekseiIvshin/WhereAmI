package com.eficksan.whereami.ioc.messaging;

import com.eficksan.whereami.data.messages.MessagesRepository;
import com.eficksan.whereami.domain.messaging.PlaceMessageValidator;
import com.eficksan.whereami.domain.messaging.PlacingMessageInteractor;
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
    public PlaceMessageValidator providePlaceMessageValidator() {
        return new PlaceMessageValidator();
    }

    @Provides
    public PlacingMessageInteractor providePlacingMessageInteractor(MessagesRepository messagesRepository) {
        return new PlacingMessageInteractor(messagesRepository);
    }

}
