package com.eficksan.whereami.ioc.messaging;

import com.eficksan.whereami.ioc.fragments.FragmentScope;
import com.eficksan.whereami.presentation.messaging.PlacingMessagePresenter;
import com.eficksan.whereami.presentation.messaging.PlacingMessageView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Aleksei Ivshin
 * on 28.08.2016.
 */
@Module
public class MessagingModule {

    @Provides
    @FragmentScope
    public PlacingMessagePresenter providePlacingMessagePresenter() {
        return new PlacingMessagePresenter(placingMessageInteractor, placeMessageValidator, context);
    }

    @Provides
    public PlacingMessageView providePlacingMessageView() {
        return new PlacingMessageView();
    }
}
