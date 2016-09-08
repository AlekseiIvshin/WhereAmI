package com.eficksan.whereami.ioc.maps;

import android.content.Context;

import com.eficksan.whereami.data.messages.MessagesRepository;
import com.eficksan.whereami.domain.location.LocationListeningInteractor;
import com.eficksan.whereami.domain.messages.MessagesFetchingInteractor;
import com.eficksan.whereami.ioc.fragments.FragmentScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Aleksei Ivshin
 * on 28.08.2016.
 */
@Module
@FragmentScope
public class MapsModule {

    @Provides
    public LocationListeningInteractor provideLocationListeningInteractor(Context context) {
        return new LocationListeningInteractor(context);
    }

    @Provides
    public MessagesFetchingInteractor provideMessagesFetchingInteractor(MessagesRepository repository){
        return new MessagesFetchingInteractor(repository);
    }
}
