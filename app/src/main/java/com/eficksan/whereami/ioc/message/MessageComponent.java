package com.eficksan.whereami.ioc.message;

import com.eficksan.whereami.ioc.activity.ActivityComponent;
import com.eficksan.whereami.ioc.fragments.FragmentScope;
import com.eficksan.whereami.ioc.messaging.MessagingModule;
import com.eficksan.whereami.presentation.message.MessageDetailsFragment;
import com.eficksan.whereami.presentation.message.MessageDetailsPresenter;
import com.eficksan.whereami.presentation.messaging.PlacingMessagePresenter;
import com.eficksan.whereami.presentation.messaging.PlacingMessageView;

import dagger.Component;

/**
 * Created by Aleksei Ivshin
 * on 28.08.2016.
 */
@FragmentScope
@Component(dependencies = ActivityComponent.class, modules = {MessageModule.class, VoteModule.class})
public interface MessageComponent {

    void inject(MessageDetailsPresenter mPresenter);

}
