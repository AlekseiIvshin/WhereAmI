package com.eficksan.whereami.presentation.auth.signup;

import android.view.View;

import com.eficksan.whereami.R;
import com.eficksan.whereami.data.auth.SignUpData;
import com.eficksan.whereami.domain.auth.EmailValidator;
import com.eficksan.whereami.domain.auth.SignUpInteractor;
import com.eficksan.whereami.domain.auth.UserNameValidator;
import com.eficksan.whereami.presentation.routing.Router;
import com.eficksan.whereami.presentation.routing.Screens;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.Objects;

import javax.inject.Inject;

import rx.Subscriber;
import rx.functions.Action1;

/**
 * Controls sign in flow.
 */
public class SignUpPresenter implements View.OnClickListener {

    @Inject
    Router router;

    @Inject
    SignUpInteractor signUpInteractor;

    @Inject
    EmailValidator emailValidator;

    @Inject
    UserNameValidator userNameValidator;

    @Inject
    UserNameValidator passwordValidator;

    private SignUpView mView;
    private Subscriber<Boolean> mSignUpSubscriber;

    private boolean mIsEmailValid = false;
    private boolean mIsUserNameValid = false;
    private boolean mIsPasswordValid = false;
    private boolean mIsPasswordConfirmationSame = false;

    public void setView(SignUpView view) {
        this.mView = view;
    }

    public void onStart() {
        mSignUpSubscriber = new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Boolean isSucceed) {
                if (isSucceed) {
                    mView.hideSignUpError();
                    router.showScreen(Screens.SIGN_IN_SCREEN);

                } else {
                    mView.showSignUpError(R.string.error_auth_sign_up);
                }
            }
        };
        mView.setSignUpEnabled(mIsEmailValid && mIsUserNameValid && mIsPasswordValid && mIsPasswordConfirmationSame);
        mView.signUp.setOnClickListener(this);
        RxTextView.textChanges(mView.emailInput)
                .subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {
                        if (charSequence.length() > 0) {
                            mIsEmailValid  =emailValidator.validate(charSequence.toString());
                            if (mIsEmailValid) {
                                mView.hideEmailValidationError();
                            } else {
                                mView.showEmailValidationError(R.string.error_auth_email_invalid);
                            }
                            updateSignUpEnable();
                        }
                    }
                });
        RxTextView.textChanges(mView.usernameInput)
                .subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {
                        if (charSequence.length() > 0) {
                            mIsUserNameValid = userNameValidator.validate(charSequence.toString());
                            if (mIsUserNameValid) {
                                mView.hideUserNamelValidationError();
                            } else {
                                mView.showUserNamelValidationError(R.string.error_auth_username_invalid);
                            }
                            updateSignUpEnable();
                        }
                    }
                });
        RxTextView.textChanges(mView.passwordInput)
                .subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {
                        if (charSequence.length() > 0) {
                            mIsPasswordValid = passwordValidator.validate(charSequence.toString());
                            if (mIsPasswordValid) {
                                mView.hidePasswordValidationError();
                            } else {
                                mView.showPasswordValidationError(R.string.error_auth_password_invalid);
                            }
                            updateSignUpEnable();
                        }
                    }
                });
        RxTextView.textChanges(mView.passwordConfirmInput)
                .subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {
                        if (charSequence.length() > 0) {
                            mIsPasswordConfirmationSame = Objects.equals(mView.passwordInput.getText().toString(), mView.passwordConfirmInput.getText().toString());
                            if (mIsPasswordConfirmationSame) {
                                mView.hidePasswordConfirmationError();
                            } else {
                                mView.showPasswordConfirmationError(R.string.error_auth_password_confirmation_is_not_equal);
                            }
                            updateSignUpEnable();
                        }
                    }
                });
    }

    public void onStop() {
        mView.signUp.setOnClickListener(null);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId) {
            case R.id.sign_up:
                String email = mView.emailInput.getText().toString();
                String userName = mView.usernameInput.getText().toString();
                String password = mView.passwordInput.getText().toString();

                if (email.isEmpty() || userName.isEmpty() || password.isEmpty()) {
                    mView.showSignUpError(R.string.error_auth_sign_up_some_fields_empty);
                    return;
                }

                mView.hideSignUpError();
                signUpInteractor.execute(
                        new SignUpData(email, userName, password),
                        mSignUpSubscriber);
                break;
        }
    }

    private void updateSignUpEnable() {
        mView.setSignUpEnabled(mIsEmailValid && mIsUserNameValid && mIsPasswordValid && mIsPasswordConfirmationSame);
    }
}
