package com.eficksan.whereami.ioc.location;

import com.eficksan.whereami.ioc.activity.ActivityComponent;
import com.eficksan.whereami.ioc.fragments.FragmentScope;
import com.eficksan.whereami.presentation.location.WhereAmIPresenter;
import com.eficksan.whereami.presentation.location.WhereAmIView;

import dagger.Component;

/**
 * Created by Aleksei Ivshin
 * on 28.08.2016.
 */
@FragmentScope
@Component(dependencies = ActivityComponent.class, modules = LocationModule.class)
public interface LocationComponent {

    void inject(WhereAmIView whereAmIView);

    void inject(WhereAmIPresenter whereAmIPresenter);
}
