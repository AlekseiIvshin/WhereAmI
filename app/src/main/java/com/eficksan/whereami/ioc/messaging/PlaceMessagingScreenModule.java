package com.eficksan.whereami.ioc.messaging;

import android.content.Context;

import com.eficksan.whereami.domain.messages.PlaceMessageValidator;
import com.eficksan.whereami.domain.messages.PlacingMessageInteractor;
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
public class PlaceMessagingScreenModule {

    @Provides
    @FragmentScope
    public PlacingMessagePresenter providePlacingMessagePresenter(
            PlacingMessageInteractor placingMessageInteractor,
            PlaceMessageValidator placeMessageValidator,
            Context context) {
        return new PlacingMessagePresenter(placingMessageInteractor, placeMessageValidator, context);
    }

    @Provides
    public PlacingMessageView providePlacingMessageView() {
        return new PlacingMessageView();
    }
}
