package com.eficksan.whereami.ioc;

import android.content.Context;

import com.eficksan.whereami.geo.LocationRequestingFragment;
import com.eficksan.whereami.ioc.modules.ServicesModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Aleksei Ivshin
 * on 25.04.2016.
 */

@Singleton
@Component(modules = {ServicesModule.class})
public interface Graph {

    void inject(LocationRequestingFragment locationRequestingFragment);

    final class Initializer {
        public static Graph init(Context context) {
            return DaggerGraph.builder()
                    .servicesModule(new ServicesModule(context))
                    .build();
        }
    }
}

