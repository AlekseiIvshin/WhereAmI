package com.eficksan.whereami.presentation.auth.signup;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.eficksan.whereami.R;
import com.eficksan.whereami.presentation.auth.common.InputFieldsDelegate;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Aleksei_Ivshin on 9/6/16.
 */
public class SignUpView {

    @Inject
    Context context;

    @Bind(R.id.input_layout_email)
    TextInputLayout emailInputLayout;
    @Bind(R.id.input_email)
    EditText emailInput;

    @Bind(R.id.input_layout_username)
    TextInputLayout usernameInputLayout;
    @Bind(R.id.input_username)
    EditText usernameInput;

    @Bind(R.id.input_layout_password)
    TextInputLayout passwordInputLayout;
    @Bind(R.id.input_password)
    EditText passwordInput;

    @Bind(R.id.input_layout_password_confirm)
    TextInputLayout passwordConfirmInputLayout;
    @Bind(R.id.input_password_confirm)
    EditText passwordConfirmInput;

    @Bind(R.id.sign_up)
    Button signUp;

    @Bind(R.id.auth_results)
    TextView signUpResults;

    public void takeView(View view) {
        ButterKnife.bind(this, view);
    }

    /**
     * Shows sign in error message.
     *
     * @param errorResId sign in error
     */
    public void showSignUpError(@StringRes int errorResId) {
        signUpResults.setText(errorResId);
        if (signUpResults.getVisibility() != View.VISIBLE) {
            signUpResults.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Hides sign in error.
     */
    public void hideSignUpError() {
        signUpResults.setText(null);
        if (signUpResults.getVisibility() != View.GONE) {
            signUpResults.setVisibility(View.GONE);
        }
    }

    public void showEmailValidationError(@StringRes int errorResId) {
        InputFieldsDelegate.showValidatoinError(emailInputLayout,errorResId,context);
    }

    public void hideEmailValidationError(){
        InputFieldsDelegate.hideValidatoinError(emailInputLayout);
    }

    public void showUserNamelValidationError(@StringRes int errorResId) {
        InputFieldsDelegate.showValidatoinError(usernameInputLayout,errorResId,context);
    }

    public void hideUserNamelValidationError(){
        InputFieldsDelegate.hideValidatoinError(usernameInputLayout);
    }

    public void showPasswordValidationError(@StringRes int errorResId) {
        InputFieldsDelegate.showValidatoinError(passwordInputLayout,errorResId,context);
    }

    public void hidePasswordValidationError(){
        InputFieldsDelegate.hideValidatoinError(passwordInputLayout);
    }

    public void showPasswordConfirmationError(int errorResId) {
        passwordConfirmInputLayout.setError(context.getString(errorResId));
    }

    public void hidePasswordConfirmationError() {
        passwordConfirmInputLayout.setError(null);
    }

    public void setSignUpEnabled(boolean signUpEnabled) {
        signUp.setClickable(signUpEnabled);
    }
}