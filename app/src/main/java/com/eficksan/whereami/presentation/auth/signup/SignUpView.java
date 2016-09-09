package com.eficksan.whereami.presentation.auth.signup;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.eficksan.whereami.R;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Signing up view.
 * Provides fields for registration user in system.
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

    /**
     * Shows validation error of input field.
     * @param inputLayout error container
     * @param resId error string resource id
     * @param context context
     */
    public static void showValidationError(TextInputLayout inputLayout, @StringRes int resId, Context context) {
        inputLayout.setError(context.getString(resId));
    }

    /**
     * Hides validation error of input field.
     * @param inputLayout error container
     */
    public static void hideValidationError(TextInputLayout inputLayout) {
        inputLayout.setError(null);
    }

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
        showValidationError(emailInputLayout,errorResId,context);
    }

    public void hideEmailValidationError(){
        hideValidationError(emailInputLayout);
    }

    public void showUserNamelValidationError(@StringRes int errorResId) {
        showValidationError(usernameInputLayout,errorResId,context);
    }

    public void hideUserNamelValidationError(){
        hideValidationError(usernameInputLayout);
    }

    public void showPasswordValidationError(@StringRes int errorResId) {
        showValidationError(passwordInputLayout,errorResId,context);
    }

    public void hidePasswordValidationError(){
        hideValidationError(passwordInputLayout);
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
