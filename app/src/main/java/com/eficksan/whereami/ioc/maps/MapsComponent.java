package com.eficksan.whereami.ioc.maps;

import com.eficksan.whereami.ioc.activity.ActivityComponent;
import com.eficksan.whereami.ioc.fragments.FragmentScope;
import com.eficksan.whereami.ioc.location.LocationModule;
import com.eficksan.whereami.presentation.location.WhereAmIPresenter;
import com.eficksan.whereami.presentation.location.WhereAmIView;
import com.eficksan.whereami.presentation.maps.MapMessagesPresenter;
import com.eficksan.whereami.presentation.maps.MapMessagesView;

import dagger.Component;

/**
 * Created by Aleksei Ivshin
 * on 28.08.2016.
 */
@FragmentScope
@Component(dependencies = ActivityComponent.class, modules = MapsModule.class)
public interface MapsComponent {

    void inject(MapMessagesView view);

    void inject(MapMessagesPresenter presenter);
}
