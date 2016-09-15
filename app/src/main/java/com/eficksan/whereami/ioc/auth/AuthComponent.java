package com.eficksan.whereami.ioc.auth;

import com.eficksan.whereami.ioc.app.AppComponent;
import com.eficksan.whereami.ioc.common.AuthModule;
import com.eficksan.whereami.ioc.fragments.FragmentScope;
import com.eficksan.whereami.presentation.auth.signin.SignInFragment;
import com.eficksan.whereami.presentation.auth.signin.SignInPresenter;
import com.eficksan.whereami.presentation.auth.signup.SignUpPresenter;
import com.eficksan.whereami.presentation.auth.signup.SignUpView;

import dagger.Component;

/**
 * Created by Aleksei Ivshin
 * on 28.08.2016.
 */
@FragmentScope
@Component(dependencies = AppComponent.class, modules = {AuthScreensModule.class, AuthModule.class})
public interface AuthComponent {

    void inject(SignInPresenter mPresenter);

    void inject(SignUpPresenter mPresenter);

    void inject(SignUpView signUpView);

    void inject(SignInFragment signInFragment);
}
