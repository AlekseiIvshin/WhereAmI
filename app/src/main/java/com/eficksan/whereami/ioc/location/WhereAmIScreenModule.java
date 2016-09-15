package com.eficksan.whereami.ioc.location;

import android.content.Context;
import android.location.Geocoder;

import com.eficksan.whereami.domain.location.AddressFetchingInteractor;
import com.eficksan.whereami.domain.location.ForegroundServiceInteractor;
import com.eficksan.whereami.domain.location.LocationListeningInteractor;
import com.eficksan.whereami.ioc.fragments.FragmentScope;
import com.eficksan.whereami.presentation.location.WhereAmIPresenter;
import com.eficksan.whereami.presentation.location.WhereAmIView;

import java.util.Locale;

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
    public WhereAmIPresenter provideWhereAmIPresenter(ForegroundServiceInteractor foregroundServiceInteractor, LocationListeningInteractor locationListeningInteractor, AddressFetchingInteractor addressFetchingInteractor) {
        return new WhereAmIPresenter(foregroundServiceInteractor, locationListeningInteractor, addressFetchingInteractor);
    }

    @Provides
    @FragmentScope
    public WhereAmIView provideWhereAmIView(Context context) {
        return new WhereAmIView(context);
    }
}
