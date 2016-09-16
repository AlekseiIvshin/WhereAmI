package com.eficksan.whereami.ioc.maps;

import com.eficksan.whereami.ioc.app.AppComponent;
import com.eficksan.whereami.ioc.common.LocationModule;
import com.eficksan.whereami.ioc.fragments.FragmentScope;
import com.eficksan.whereami.presentation.maps.MapMessagesPresenter;
import com.eficksan.whereami.presentation.maps.MapMessagesView;

import dagger.Component;

/**
 * Created by Aleksei Ivshin
 * on 28.08.2016.
 */
@FragmentScope
@Component(dependencies = AppComponent.class, modules = {MapsScreenModule.class, LocationModule.class})
public interface MapsComponent {

    void inject(MapMessagesView view);

    void inject(MapMessagesPresenter presenter);
}
