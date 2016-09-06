package com.eficksan.whereami.presentation.auth.signin;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.eficksan.whereami.R;
import com.eficksan.whereami.presentation.auth.common.InputFieldsDelegate;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Aleksei_Ivshin on 9/6/16.
 */
public class SignInView {

    @Inject
    Context context;

    @Bind(R.id.input_email)
    EditText emailInput;

    @Bind(R.id.input_layout_email)
    protected TextInputLayout emailInputLayout;

    @Bind(R.id.input_password)
    EditText passwordInput;

    @Bind(R.id.input_layout_password)
    protected TextInputLayout passwordInputLayout;

    @Bind(R.id.progress_bar)
    ProgressBar progressBar;

    public void takeView(View view) {
        ButterKnife.bind(this, view);
        progressBar.setIndeterminate(true);
    }

    public void showEmailValidationError(@StringRes int resId) {
        InputFieldsDelegate.showValidatoinError(emailInputLayout, resId, context);
    }

    public void hideEmailValidationError(@StringRes int resId) {
        InputFieldsDelegate.hideValidatoinError(emailInputLayout);
    }

    public void showPasswordValidationError(@StringRes int resId) {
        InputFieldsDelegate.showValidatoinError(passwordInputLayout, resId, context);
    }

    public void hidePasswordValidationError(@StringRes int resId) {
        InputFieldsDelegate.hideValidatoinError(passwordInputLayout);
    }

    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }
}
