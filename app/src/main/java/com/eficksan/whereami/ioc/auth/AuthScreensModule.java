package com.eficksan.whereami.ioc.auth;

import android.content.Context;

import com.eficksan.whereami.domain.auth.EmailValidator;
import com.eficksan.whereami.domain.auth.PasswordValidator;
import com.eficksan.whereami.domain.auth.SignInInteractor;
import com.eficksan.whereami.domain.auth.SignUpInteractor;
import com.eficksan.whereami.domain.auth.UserNameValidator;
import com.eficksan.whereami.ioc.fragments.FragmentScope;
import com.eficksan.whereami.presentation.auth.signin.SignInPresenter;
import com.eficksan.whereami.presentation.auth.signin.SignInView;
import com.eficksan.whereami.presentation.auth.signup.SignUpPresenter;
import com.eficksan.whereami.presentation.auth.signup.SignUpView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Aleksei Ivshin
 * on 28.08.2016.
 */
@Module
public class AuthScreensModule {

    @Provides
    @FragmentScope
    public SignInPresenter provideSignInPresenter(SignInInteractor signInInteractor, Context context) {
        return new SignInPresenter(context, signInInteractor);
    }

    @Provides
    public SignInView provideSignInView() {
        return new SignInView();
    }

    @Provides
    @FragmentScope
    public SignUpPresenter provideSignUpPresenter(
            SignUpInteractor signUpInteractor,
            EmailValidator emailValidator,
            UserNameValidator userNameValidator,
            PasswordValidator passwordValidator) {
        return new SignUpPresenter(signUpInteractor, emailValidator, userNameValidator, passwordValidator);
    }

    @Provides
    public SignUpView provideSignUpView(Context context) {
        return new SignUpView(context);
    }

}
