package com.eficksan.whereami.ioc.location;

import android.content.Context;

import com.eficksan.whereami.domain.location.AddressFetchingInteractor;
import com.eficksan.whereami.domain.location.ForegroundServiceInteractor;
import com.eficksan.whereami.domain.location.LocationListeningInteractor;
import com.eficksan.whereami.ioc.fragments.FragmentScope;
import com.eficksan.whereami.presentation.location.WhereAmIPresenter;
import com.eficksan.whereami.presentation.location.WhereAmIView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Aleksei Ivshin
 * on 28.08.2016.
 */
@Module
public class WhereAmIScreenModule {

    @Provides
    @FragmentScope
    public WhereAmIPresenter provideWhereAmIPresenter(
            ForegroundServiceInteractor foregroundService,
            LocationListeningInteractor locationListening,
            AddressFetchingInteractor addressFetching) {
        return new WhereAmIPresenter(foregroundService, locationListening, addressFetching);
    }

    @Provides
    @FragmentScope
    public WhereAmIView provideWhereAmIView(Context context) {
        return new WhereAmIView(context);
    }
}
