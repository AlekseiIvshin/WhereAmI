package com.eficksan.whereami.ioc.auth;

import com.eficksan.whereami.data.auth.UsersRepository;
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

import org.mockito.Mockito;

import dagger.Module;

import static org.mockito.Mockito.mock;

/**
 * Created by Aleksei_Ivshin on 9/12/16.
 */
@Module
@FragmentScope
public class TestAuthModule  extends AuthModule{

    @Override
    public EmailValidator provideEmailValidator() {
        return mock(EmailValidator.class);
    }

    @Override
    public PasswordValidator providePasswordValidator() {
        return mock(PasswordValidator.class);
    }

    @Override
    public UserNameValidator provideUserNameValidator() {
        return mock(UserNameValidator.class);
    }

    @Override
    public SignInPresenter provideSignInPresenter(Router router, SignInInteractor signInInteractor) {
        return mock(SignInPresenter.class);
    }

    @Override
    public SignInView provideSignInView() {
        return mock(SignInView.class);
    }

    @Override
    public SignInInteractor provideSignInInteractor(FirebaseAuth firebaseAuth) {
        return mock(SignInInteractor.class);
    }

    @Override
    public SignUpInteractor provideSignUpInteractor(FirebaseAuth firebaseAuth, UsersRepository usersRepository) {
        return mock(SignUpInteractor.class);
    }

    @Override
    public ValidatingInteractor provideEmailValidatingInteractor() {
        return mock(ValidatingInteractor.class);
    }

    @Override
    public ValidatingInteractor provideUserNameValidatingInteractor() {
        return mock(ValidatingInteractor.class);
    }

    @Override
    public ValidatingInteractor providePasswordValidatingInteractor() {
        return mock(ValidatingInteractor.class);
    }
}
