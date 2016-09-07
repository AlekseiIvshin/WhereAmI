package com.eficksan.whereami.ioc.messaging;

import com.eficksan.whereami.ioc.activity.ActivityComponent;
import com.eficksan.whereami.ioc.fragments.FragmentScope;
import com.eficksan.whereami.presentation.messaging.PlacingMessagePresenter;
import com.eficksan.whereami.presentation.messaging.PlacingMessageView;
import com.google.firebase.database.FirebaseDatabase;

import dagger.Component;

/**
 * Created by Aleksei Ivshin
 * on 28.08.2016.
 */
@FragmentScope
@Component(dependencies = ActivityComponent.class, modules = MessagingModule.class)
public interface MessagingComponent {

    void inject(PlacingMessageView placingMessageView);

    void inject(PlacingMessagePresenter mPresenter);
}
