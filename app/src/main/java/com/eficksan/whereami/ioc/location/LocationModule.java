package com.eficksan.whereami.ioc.location;

import android.app.Activity;
import android.content.Context;

import com.eficksan.whereami.domain.location.ForegroundServiceInteractor;
import com.eficksan.whereami.domain.location.ListenLocationInteractor;
import com.eficksan.whereami.domain.sync.SyncInteractor;
import com.eficksan.whereami.ioc.fragments.FragmentScope;

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
    public ListenLocationInteractor provideListenLocationInteractor(Context context) {
        return new ListenLocationInteractor(context);
    }

    @Provides
    public ForegroundServiceInteractor provideForegroundServiceInteractor(Activity activity) {
        return new ForegroundServiceInteractor(activity);
    }

    @Provides
    public SyncInteractor provideInteractor(Context context) {
        return new SyncInteractor(context);
    }
}
