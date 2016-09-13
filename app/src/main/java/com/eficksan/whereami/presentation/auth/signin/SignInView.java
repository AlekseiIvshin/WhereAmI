package com.eficksan.whereami.presentation.auth.signin;

import android.support.annotation.StringRes;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.eficksan.whereami.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Aleksei_Ivshin on 9/6/16.
 */
public class SignInView {

    @Bind(R.id.sign_in_input_email)
    EditText emailInput;

    @Bind(R.id.sign_in_input_password)
    EditText passwordInput;

    @Bind(R.id.sign_in)
    Button signIn;

    @Bind(R.id.sign_up)
    Button signUp;

    @Bind(R.id.reset_password)
    Button resetPassword;

    @Bind(R.id.auth_results)
    TextView signInResults;

    @Bind(R.id.progress_bar)
    ProgressBar progressBar;

    public void takeView(View view) {
        ButterKnife.bind(this, view);
        progressBar.setIndeterminate(true);
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
        blockControls();
        progressBar.setVisibility(View.VISIBLE);
    }

    /**
     * Hides sign in progress.
     */
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
        unblockControls();
    }

    private void blockControls() {
        signUp.setClickable(true);
        signIn.setClickable(true);
        resetPassword.setClickable(true);
    }

    private void unblockControls() {
        signUp.setClickable(false);
        signIn.setClickable(false);
        resetPassword.setClickable(false);
    }

    public void showResetPassword() {
        if (View.VISIBLE != resetPassword.getVisibility()) {
            resetPassword.setVisibility(View.VISIBLE);
        }
    }
}
