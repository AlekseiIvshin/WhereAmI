package com.eficksan.whereami.presentation.auth.signin;

import android.view.View;

import com.eficksan.whereami.R;
import com.eficksan.whereami.data.auth.SignInData;
import com.eficksan.whereami.domain.auth.SignInInteractor;
import com.eficksan.whereami.presentation.BasePresenter;
import com.eficksan.whereami.presentation.routing.Screens;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Controls sign in flow.
 */
public class SignInPresenter extends BasePresenter implements View.OnClickListener {

    private final SignInInteractor signInInteractor;

    private SignInView mSignInView;
    private Subscriber<Boolean> mSignInSubscriber;

    @Inject
    public SignInPresenter(SignInInteractor signInInteractor) {
        this.signInInteractor = signInInteractor;
    }

    public void setView(SignInView signInView) {
        this.mSignInView = signInView;
    }

    public void onStart() {
        mSignInSubscriber = new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Boolean isSucceed) {
                mSignInView.hideProgress();
                if (isSucceed) {
                    mSignInView.hideSignInError();
                } else {
                    mSignInView.showSignInError(R.string.error_auth_sign_in);
                    mSignInView.showResetPassword();
                }
            }
        };
        mSignInView.signIn.setOnClickListener(this);
        mSignInView.signUp.setOnClickListener(this);
        mSignInView.resetPassword.setOnClickListener(this);
    }

    public void onStop() {
        mSignInView.signIn.setOnClickListener(null);
        mSignInView.signUp.setOnClickListener(null);
        mSignInView.resetPassword.setOnClickListener(null);
        signInInteractor.unsubscribe();
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId) {
            case R.id.sign_in:
                String email = mSignInView.emailInput.getText().toString();
                String password = mSignInView.passwordInput.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    mSignInView.showSignInError(R.string.error_auth_empty_email_or_password);
                } else {
                    mSignInView.showProgress();
                    mSignInView.hideSignInError();
                    signInInteractor.execute(
                            new SignInData(email, password),
                            mSignInSubscriber);
                }
                break;
            case R.id.sign_up:
                router.showScreen(Screens.SIGN_UP_SCREEN);
                break;
            case R.id.reset_password:
                router.showScreen(Screens.RESET_PASSWORD_SCREEN);
                break;
        }
    }
}
