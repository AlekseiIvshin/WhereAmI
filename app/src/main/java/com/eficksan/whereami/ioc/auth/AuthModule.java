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
import com.eficksan.whereami.presentation.routing.Router;
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
public class AuthModule {

    @Provides
    public EmailValidator provideEmailValidator() {
        return new EmailValidator();
    }

    @Provides
    public PasswordValidator providePasswordValidator() {
        return new PasswordValidator();
    }

    @Provides
    public UserNameValidator provideUserNameValidator() {
        return new UserNameValidator();
    }

    @Provides
    @FragmentScope
    public SignInPresenter provideSignInPresenter(Router router, SignInInteractor signInInteractor) {
        return new SignInPresenter(router, signInInteractor);
    }

    @Provides
    public SignInView provideSignInView() {
        return new SignInView();
    }

    @Provides
    @FragmentScope
    public SignInInteractor provideSignInInteractor(FirebaseAuth firebaseAuth) {
        return new SignInInteractor(firebaseAuth);
    }

    @Provides
    @FragmentScope
    public SignUpInteractor provideSignUpInteractor(FirebaseAuth firebaseAuth, UsersDataSource usersDataSource, @Named("job") Scheduler jobScheduler, @Named("ui") Scheduler uiScheduler) {
        return new SignUpInteractor(firebaseAuth, usersDataSource, jobScheduler, uiScheduler);
    }

    @Provides
    @FragmentScope
    @Named("email")
    public ValidatingInteractor provideEmailValidatingInteractor() {
        return new ValidatingInteractor(new EmailValidator());
    }

    @Provides
    @FragmentScope
    @Named("username")
    public ValidatingInteractor provideUserNameValidatingInteractor() {
        return new ValidatingInteractor(new UserNameValidator());
    }

    @Provides
    @FragmentScope
    @Named("password")
    public ValidatingInteractor providePasswordValidatingInteractor() {
        return new ValidatingInteractor(new PasswordValidator());
    }
}
