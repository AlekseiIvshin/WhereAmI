package com.eficksan.whereami.ioc.message;

import com.eficksan.whereami.ioc.app.AppComponent;
import com.eficksan.whereami.ioc.common.MessagesModule;
import com.eficksan.whereami.ioc.common.UsersModule;
import com.eficksan.whereami.ioc.common.VoteModule;
import com.eficksan.whereami.ioc.fragments.FragmentScope;
import com.eficksan.whereami.presentation.message.MessageDetailsFragment;
import com.eficksan.whereami.presentation.message.MessageDetailsPresenter;

import dagger.Component;

/**
 * Created by Aleksei Ivshin
 * on 28.08.2016.
 */
@FragmentScope
@Component(
        dependencies = AppComponent.class,
        modules = {MessageScreenModule.class, VoteModule.class, MessagesModule.class, UsersModule.class})
public interface MessageComponent {

    void inject(MessageDetailsPresenter mPresenter);

    void inject(MessageDetailsFragment messageDetailsFragment);
}
