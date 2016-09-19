package com.eficksan.whereami.ioc.maps;

import com.eficksan.whereami.data.messages.MessagesDataSource;
import com.eficksan.whereami.domain.location.LocationListeningInteractor;
import com.eficksan.whereami.domain.messages.MessagesFetchingInteractor;
import com.eficksan.whereami.ioc.fragments.FragmentScope;
import com.eficksan.whereami.presentation.maps.MapMessagesPresenter;
import com.eficksan.whereami.presentation.maps.MapMessagesView;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import rx.Scheduler;

/**
 * Created by Aleksei Ivshin
 * on 28.08.2016.
 */
@Module
public class MapsScreenModule {

    @Provides
    public MessagesFetchingInteractor provideMessagesFetchingInteractor(
            MessagesDataSource messagesDataSource,
            @Named("job") Scheduler jobScheduler, @Named("ui") Scheduler uiScheduler) {
        return new MessagesFetchingInteractor(messagesDataSource, jobScheduler, uiScheduler);
    }

    @Provides
    @FragmentScope
    public MapMessagesPresenter provideMapMessagesPresenter(
            LocationListeningInteractor locationListeningInteractor,
            MessagesFetchingInteractor messagesFetchingInteractor) {
        return new MapMessagesPresenter(locationListeningInteractor, messagesFetchingInteractor);
    }

    @Provides
    public MapMessagesView provideMapMessagesView() {
        return new MapMessagesView();
    }
}
