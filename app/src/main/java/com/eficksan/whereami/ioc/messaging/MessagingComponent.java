package com.eficksan.whereami.ioc.messaging;

import com.eficksan.whereami.ioc.app.AppComponent;
import com.eficksan.whereami.ioc.common.MessagesModule;
import com.eficksan.whereami.ioc.fragments.FragmentScope;
import com.eficksan.whereami.presentation.messaging.PlacingMessageFragment;
import com.eficksan.whereami.presentation.messaging.PlacingMessagePresenter;
import com.eficksan.whereami.presentation.messaging.PlacingMessageView;

import dagger.Component;

/**
 * Created by Aleksei Ivshin
 * on 28.08.2016.
 */
@FragmentScope
@Component(
        dependencies = AppComponent.class,
        modules = {PlaceMessagingScreenModule.class, MessagesModule.class})
public interface MessagingComponent {

    void inject(PlacingMessageView placingMessageView);

    void inject(PlacingMessagePresenter mPresenter);

    void inject(PlacingMessageFragment placingMessageFragment);
}
