package com.eficksan.whereami.presentation.auth.signup;

import android.os.Bundle;

import com.eficksan.whereami.R;
import com.eficksan.whereami.data.auth.SignUpData;
import com.eficksan.whereami.domain.auth.EmailValidator;
import com.eficksan.whereami.domain.auth.PasswordValidator;
import com.eficksan.whereami.domain.auth.SignUpInteractor;
import com.eficksan.whereami.domain.auth.UserNameValidator;
import com.eficksan.whereami.presentation.common.BasePresenter;
import com.eficksan.whereami.presentation.routing.Screens;

import java.util.Objects;

import javax.inject.Inject;

import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

/**
 * Controls sign in flow.
 */
public class SignUpPresenter extends BasePresenter<SignUpView> {

    private final SignUpInteractor signUpInteractor;

    private final EmailValidator emailValidator;

    private final UserNameValidator userNameValidator;

    private final PasswordValidator passwordValidator;

    private boolean mIsEmailValid = false;
    private boolean mIsUserNameValid = false;
    private boolean mIsPasswordValid = false;
    private boolean mIsPasswordConfirmationSame = false;
    private CompositeSubscription compositeSubscription;
    private String mEmail = "";
    private String mUserName = "";
    private String mPassword = "";

    @Inject
    public SignUpPresenter(
            SignUpInteractor signUpInteractor,
            EmailValidator emailValidator,
            UserNameValidator userNameValidator,
            PasswordValidator passwordValidator) {
        this.signUpInteractor = signUpInteractor;
        this.emailValidator = emailValidator;
        this.userNameValidator = userNameValidator;
        this.passwordValidator = passwordValidator;
    }

    @Override
    public void onCreate(Bundle savedInstanceStates) {
        super.onCreate(savedInstanceStates);
        compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void onViewCreated(SignUpView view) {
        super.onViewCreated(view);

        compositeSubscription.add(mView.getEmailValueChannel()
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String email) {
                        mIsEmailValid = emailValidator.validate(email);
                        if (mIsEmailValid || email.isEmpty()) {
                            mView.hideEmailValidationError();
                        } else {
                            mView.showEmailValidationError(R.string.error_auth_email_invalid);
                        }
                        handleFieldsValidation();
                        return mIsEmailValid;
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String validEmail) {
                        mEmail = validEmail;
                    }
                }));

        compositeSubscription.add(mView.getUserNameValueChannel()
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String userName) {
                        mIsUserNameValid = userNameValidator.validate(userName);
                        if (mIsUserNameValid || userName.isEmpty()) {
                            mView.hideUserNamelValidationError();
                        } else {
                            mView.showUserNamelValidationError(R.string.error_auth_username_invalid);
                        }
                        handleFieldsValidation();
                        return mIsUserNameValid;
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String validUserName) {
                        mUserName = validUserName;
                    }
                }));

        compositeSubscription.add(mView.getPasswordValueChannel()
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String password) {
                        mIsPasswordValid = passwordValidator.validate(password);
                        if (mIsPasswordValid || password.isEmpty()) {
                            mView.hidePasswordValidationError();
                        } else {
                            mView.showPasswordValidationError(R.string.error_auth_password_invalid);
                        }
                        handleFieldsValidation();
                        return mIsPasswordValid;
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String validPassword) {
                        mPassword = validPassword;
                    }
                }));

        compositeSubscription.add(mView.getPasswordConfirmValueChannel()
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String passwordConfirmation) {
                        if (!passwordConfirmation.isEmpty()) {
                            mIsPasswordConfirmationSame = !passwordConfirmation.isEmpty()
                                    && Objects.equals(mPassword, passwordConfirmation);
                            if (mIsPasswordConfirmationSame || passwordConfirmation.isEmpty()) {
                                mView.hidePasswordConfirmationError();
                            } else {
                                mView.showPasswordConfirmationError(
                                        R.string.error_auth_password_confirmation_is_not_equal);
                            }
                            handleFieldsValidation();
                        }
                    }
                }));

        compositeSubscription.add(mView.getSignUpClicksChannel()
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (mEmail.isEmpty() || mUserName.isEmpty() || mPassword.isEmpty()) {
                            mView.showSignUpError(R.string.error_auth_sign_up_some_fields_empty);
                            return;
                        }
                        mView.hideSignUpError();
                        signUpInteractor.execute(
                                new SignUpData(mEmail, mUserName, mPassword),
                                new SignUpSubscriber());
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
        signUpInteractor.unsubscribe();
        super.onDestroy();
    }

    private void handleFieldsValidation() {
        mView.setSignUpEnabled(mIsEmailValid && mIsUserNameValid && mIsPasswordValid && mIsPasswordConfirmationSame);
    }

    private class SignUpSubscriber extends Subscriber<Boolean> {
        @Override
        public void onCompleted() {
            signUpInteractor.unsubscribe();
        }

        @Override
        public void onError(Throwable e) {
            signUpInteractor.unsubscribe();
        }

        @Override
        public void onNext(Boolean isSucceed) {
            if (isSucceed) {
                mView.hideSignUpError();
                mRouter.showScreen(Screens.SIGN_IN_SCREEN);
            } else {
                mView.showSignUpError(R.string.error_auth_sign_up);
            }
        }
    }
}
