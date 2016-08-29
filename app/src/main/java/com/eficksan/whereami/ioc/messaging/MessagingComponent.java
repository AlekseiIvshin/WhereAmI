package com.eficksan.whereami.ioc.messaging;

import com.eficksan.whereami.ioc.activity.ActivityComponent;
import com.eficksan.whereami.ioc.fragments.FragmentScope;
import com.eficksan.whereami.presentation.messaging.MessagingPresenter;
import com.eficksan.whereami.presentation.messaging.MessagingView;

import dagger.Component;

/**
 * Created by Aleksei Ivshin
 * on 28.08.2016.
 */
@FragmentScope
@Component(dependencies = ActivityComponent.class, modules = MessagingModule.class)
public interface MessagingComponent {

    void inject(MessagingView messagingView);

    void inject(MessagingPresenter mPresenter);
}
