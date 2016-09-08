package com.eficksan.whereami.ioc.message;

import com.eficksan.whereami.data.auth.UsersRepository;
import com.eficksan.whereami.data.messages.MessagesRepository;
import com.eficksan.whereami.domain.messages.FindMessageInteractor;
import com.eficksan.whereami.domain.messages.PlaceMessageValidator;
import com.eficksan.whereami.domain.messages.PlacingMessageInteractor;
import com.eficksan.whereami.domain.users.FindUserInteractor;
import com.eficksan.whereami.ioc.fragments.FragmentScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Aleksei Ivshin
 * on 28.08.2016.
 */
@Module
public class MessageModule {

    @Provides
    public FindMessageInteractor provideFindMessageInteractor(MessagesRepository messagesRepository) {
        return new FindMessageInteractor(messagesRepository);
    }

    @Provides
    public FindUserInteractor provideFindUserInteractor(UsersRepository usersRepository) {
        return new FindUserInteractor(usersRepository);
    }
}
