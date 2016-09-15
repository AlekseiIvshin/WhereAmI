package com.eficksan.whereami.ioc.auth;

import com.eficksan.whereami.data.auth.UsersDataSource;
import com.eficksan.whereami.domain.auth.EmailValidator;
import com.eficksan.whereami.domain.auth.PasswordValidator;
import com.eficksan.whereami.domain.auth.SignInInteractor;
import com.eficksan.whereami.domain.auth.SignUpInteractor;
import com.eficksan.whereami.domain.auth.UserNameValidator;
import com.eficksan.whereami.domain.auth.ValidatingInteractor;
import com.eficksan.whereami.ioc.fragments.FragmentScope;
import com.eficksan.whereami.presentation.auth.signin.SignInPresenter;
import com.eficksan.whereami.presentation.auth.signin.SignInView;
import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import rx.Scheduler;

/**
 * Created by Aleksei Ivshin
 * on 28.08.2016.
 */
@Module
public class AuthScreensModule {

    @Provides
    @FragmentScope
    public SignInPresenter provideSignInPresenter(SignInInteractor signInInteractor) {
        return new SignInPresenter(signInInteractor);
    }

    @Provides
    @FragmentScope
    public SignInView provideSignInView() {
        return new SignInView();
    }

}
