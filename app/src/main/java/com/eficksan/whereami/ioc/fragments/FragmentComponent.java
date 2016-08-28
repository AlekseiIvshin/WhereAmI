package com.eficksan.whereami.ioc.fragments;

import com.eficksan.whereami.ioc.activity.ActivityComponent;
import com.eficksan.whereami.ioc.app.AppComponent;

import dagger.Component;

/**
 * Created by Aleksei Ivshin
 * on 28.08.2016.
 */
@FragmentScope
@Component(dependencies = ActivityComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {
}
