package com.eficksan.whereami.presentation.auth.signin;

import com.eficksan.whereami.domain.auth.EmailValidator;
import com.eficksan.whereami.domain.auth.PasswordValidator;
import com.eficksan.whereami.domain.auth.SignInInteractor;
import com.eficksan.whereami.presentation.routing.Router;

import javax.inject.Inject;

/**
 * Created by Aleksei_Ivshin on 9/6/16.
 */
public class SignInPresenter {

    @Inject
    Router router;

    @Inject
    EmailValidator emailValidator;

    @Inject
    PasswordValidator passwordValidator;

    @Inject
    SignInInteractor signInInteractor;

    private SignInView mSignInView;

    public void onStart() {
        //TODO: Add listeners
    }

    public void onStop() {

    }

    public void setView(SignInView signInView) {
        this.mSignInView = signInView;
    }
}
