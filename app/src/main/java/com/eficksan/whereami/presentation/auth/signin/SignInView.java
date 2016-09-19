package com.eficksan.whereami.presentation.auth.signin;

import android.content.Context;
import android.support.annotation.StringRes;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.eficksan.whereami.R;
import com.eficksan.whereami.presentation.IView;
import com.jakewharton.rxbinding.view.RxView;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;

/**
 * Sign in view.
 */
public class SignInView implements IView {

    @Bind(R.id.sign_in_input_email)
    EditText emailInput;

    @Bind(R.id.sign_in_input_password)
    EditText passwordInput;

    @Bind(R.id.sign_in)
    Button signIn;

    @Bind(R.id.sign_up)
    Button signUp;

    @Bind(R.id.auth_results)
    TextView signInResults;

    @Bind(R.id.progress_bar)
    ProgressBar progressBar;

    @Override
    public void takeView(View view) {
        ButterKnife.bind(this, view);
        progressBar.setIndeterminate(true);
    }

    @Override
    public void releaseView() {

    }

    /**
     * Shows sign in error message.
     *
     * @param errorResId sign in error
     */
    public void showSignInError(@StringRes int errorResId) {
        signInResults.setText(errorResId);
        if (signInResults.getVisibility() != View.VISIBLE) {
            signInResults.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Hides sign in error.
     */
    public void hideSignInError() {
        signInResults.setText(null);
        if (signInResults.getVisibility() != View.GONE) {
            signInResults.setVisibility(View.GONE);
        }
    }

    /**
     * Shows sign in progress.
     */
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    /**
     * Hides sign in progress.
     */
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    /**
     * Gets email value.
     *
     * @return email
     */
    public String getEmailValue() {
        return emailInput.getText().toString();
    }

    /**
     * Gets password value.
     *
     * @return password
     */
    public String getPasswordValue() {
        return passwordInput.getText().toString();
    }

    /**
     * Gets channel of click on sign in.
     *
     * @return clicks channel
     */
    public Observable<Void> getSignInChannel() {
        return RxView.clicks(signIn);
    }

    /**
     * Gets channel of click on sign up.
     *
     * @return clicks channel
     */
    public Observable<Void> getSignUpChannel() {
        return RxView.clicks(signUp);
    }

    public void blockControls() {
        signUp.setClickable(false);
        signIn.setClickable(false);
    }

    public void unblockControls() {
        signUp.setClickable(true);
        signIn.setClickable(true);
    }

    public void hideKeyboard(Context context) {
        InputMethodManager keyboard = (InputMethodManager)
                context.getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.hideSoftInputFromWindow(emailInput.getWindowToken(), 0);
    }
}
