package com.eficksan.whereami.ioc.common;

import android.content.Context;
import android.location.Geocoder;

import com.eficksan.whereami.domain.location.AddressFetchingInteractor;
import com.eficksan.whereami.domain.location.ForegroundServiceInteractor;
import com.eficksan.whereami.domain.location.LocationListeningInteractor;

import java.util.Locale;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Aleksei Ivshin
 * on 28.08.2016.
 */
@Module
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

}
