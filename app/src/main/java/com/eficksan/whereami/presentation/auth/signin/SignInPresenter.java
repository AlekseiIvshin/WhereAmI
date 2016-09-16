package com.eficksan.whereami.presentation.auth.signin;

import android.content.Context;
import android.os.Bundle;

import com.eficksan.whereami.R;
import com.eficksan.whereami.data.auth.SignInData;
import com.eficksan.whereami.domain.auth.SignInInteractor;
import com.eficksan.whereami.presentation.common.BasePresenter;
import com.eficksan.whereami.presentation.BaseSubscriber;
import com.eficksan.whereami.presentation.routing.Screens;

import javax.inject.Inject;

import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Controls sign in flow.
 */
public class SignInPresenter extends BasePresenter<SignInView> {

    private final Context context;
    private final SignInInteractor signInInteractor;

    private CompositeSubscription compositeSubscription;

    @Inject
    public SignInPresenter(Context context, SignInInteractor signInInteractor) {
        this.context = context;
        this.signInInteractor = signInInteractor;
    }

    @Override
    public void onCreate(Bundle savedInstanceStates) {
        super.onCreate(savedInstanceStates);
        compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void onViewCreated(SignInView view) {
        super.onViewCreated(view);

        compositeSubscription.add(mView.getSignInChannel().subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                String email = mView.getEmailValue();
                String password = mView.getPasswordValue();

                if (email.isEmpty() || password.isEmpty()) {
                    mView.showSignInError(R.string.error_auth_empty_email_or_password);
                } else {
                    mView.blockControls();
                    mView.showProgress();
                    mView.hideSignInError();
                    mView.hideKeyboard(context);

                    signInInteractor.execute(
                            new SignInData(email, password),
                            new SignInSubscriber(signInInteractor));
                }
            }
        }));
        compositeSubscription.add(mView.getSignUpChannel().subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                mRouter.showScreen(Screens.SIGN_UP_SCREEN);
            }
        }));
    }

    @Override
    public void onViewDestroyed() {
        compositeSubscription.unsubscribe();
        compositeSubscription.clear();
        super.onViewDestroyed();
    }

    @Override
    public void onDestroy() {
        signInInteractor.unsubscribe();
        super.onDestroy();
    }

    protected class SignInSubscriber extends BaseSubscriber<SignInInteractor, Boolean> {

        public SignInSubscriber(SignInInteractor interactor) {
            super(interactor);
        }

        @Override
        public void onNext(Boolean isSucceed) {
            mView.unblockControls();
            mView.hideProgress();
            if (isSucceed) {
                mView.hideSignInError();
            } else {
                mView.showSignInError(R.string.error_auth_sign_in);
            }
        }
    }
}
