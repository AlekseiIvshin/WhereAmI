package com.eficksan.whereami.ioc.common;

import com.eficksan.whereami.data.auth.UsersDataSource;
import com.eficksan.whereami.domain.auth.EmailValidator;
import com.eficksan.whereami.domain.auth.PasswordValidator;
import com.eficksan.whereami.domain.auth.SignInInteractor;
import com.eficksan.whereami.domain.auth.SignUpInteractor;
import com.eficksan.whereami.domain.auth.UserNameValidator;
import com.eficksan.whereami.domain.auth.ValidatingInteractor;
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
    public SignInInteractor provideSignInInteractor(FirebaseAuth firebaseAuth) {
        return new SignInInteractor(firebaseAuth);
    }

    @Provides
    public SignUpInteractor provideSignUpInteractor(
            FirebaseAuth firebaseAuth,
            UsersDataSource usersDataSource,
            @Named("job") Scheduler jobScheduler, @Named("ui") Scheduler uiScheduler) {
        return new SignUpInteractor(firebaseAuth, usersDataSource, jobScheduler, uiScheduler);
    }

    @Provides
    @Named("email")
    public ValidatingInteractor provideEmailValidatingInteractor() {
        return new ValidatingInteractor(new EmailValidator());
    }

    @Provides
    @Named("username")
    public ValidatingInteractor provideUserNameValidatingInteractor() {
        return new ValidatingInteractor(new UserNameValidator());
    }

    @Provides
    @Named("password")
    public ValidatingInteractor providePasswordValidatingInteractor() {
        return new ValidatingInteractor(new PasswordValidator());
    }
}
