package com.eficksan.whereami.presentation.auth.signin;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eficksan.whereami.App;
import com.eficksan.whereami.R;
import com.eficksan.whereami.ioc.auth.AuthComponent;
import com.eficksan.whereami.presentation.common.ComponentLifecycleFragment;
import com.eficksan.whereami.presentation.common.IPresenter;
import com.eficksan.whereami.presentation.routing.Router;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Sign in fragment provides methods for signing in, singing up and resetting password(in case failed sign in).
 */
public class SignInFragment extends ComponentLifecycleFragment {

    public static final String TAG = SignInFragment.class.getSimpleName();

    @Inject
    SignInPresenter mPresenter;

    @Inject
    SignInView signInView;

    AuthComponent mAuthComponent;

    public SignInFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SignInFragment.
     */
    public static SignInFragment newInstance() {
        return new SignInFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        signInView.takeView(view);
        mPresenter.onViewCreated(signInView);
    }

    @Override
    public void onKillComponent() {
        ((App) getActivity().getApplication()).removeAuthComponent();
    }

    @Override
    public IPresenter getPresenter() {
        return mPresenter;
    }

    @Override
    public void onSetUpComponent() {
        mAuthComponent = ((App) getActivity().getApplication()).plusAuthComponent();
        mAuthComponent.inject(this);
    }
}
