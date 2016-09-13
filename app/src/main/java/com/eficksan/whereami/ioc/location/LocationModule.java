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
@FragmentScope
public class LocationModule {

    @Provides
    public ForegroundServiceInteractor provideForegroundServiceInteractor(Context context) {
        return new ForegroundServiceInteractor(context);
    }

    @Provides
    public LocationListeningInteractor provideLocationListeningInteractor(Context context) {
        return new LocationListeningInteractor(context);
    }

    @Provides
    public AddressFetchingInteractor provideAddressFetchingInteractor(Context context) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        return new AddressFetchingInteractor(geocoder);
    }

    @Provides
    public WhereAmIPresenter provideWhereAmIPresenter(ForegroundServiceInteractor foregroundServiceInteractor, LocationListeningInteractor locationListeningInteractor, AddressFetchingInteractor addressFetchingInteractor) {
        return new WhereAmIPresenter(foregroundServiceInteractor, locationListeningInteractor, addressFetchingInteractor);
    }

    @Provides
    public WhereAmIView provideWhereAmIView(Context context) {
        return new WhereAmIView(context);
    }
}
