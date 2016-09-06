package com.eficksan.whereami.ioc.auth;

import com.eficksan.whereami.domain.auth.EmailValidator;
import com.eficksan.whereami.domain.auth.PasswordValidator;
import com.eficksan.whereami.domain.auth.SignInInteractor;
import com.eficksan.whereami.domain.auth.ValidatingInteractor;
import com.eficksan.whereami.domain.auth.Validator;
import com.eficksan.whereami.ioc.fragments.FragmentScope;
import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

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
    @FragmentScope
    public SignInInteractor provideSignInInteractor(FirebaseAuth firebaseAuth) {
        return new SignInInteractor(firebaseAuth);
    }

    @Provides
    @FragmentScope
    @Named("email")
    public ValidatingInteractor provideEmailValidatingInteractor() {
        return new ValidatingInteractor(new EmailValidator());
    }

    @Provides
    @FragmentScope
    @Named("password")
    public ValidatingInteractor providePasswordValidatingInteractor() {
        return new ValidatingInteractor(new PasswordValidator());
    }
}
