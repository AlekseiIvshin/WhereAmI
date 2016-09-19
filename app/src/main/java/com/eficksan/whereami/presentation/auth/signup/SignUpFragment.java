package com.eficksan.whereami.presentation.auth.signup;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eficksan.whereami.App;
import com.eficksan.whereami.R;
import com.eficksan.whereami.ioc.auth.AuthComponent;
import com.eficksan.whereami.presentation.common.ComponentLifecycleFragment;
import com.eficksan.whereami.presentation.common.IPresenter;

import javax.inject.Inject;

public class SignUpFragment extends ComponentLifecycleFragment {


    public static final String TAG = SignUpFragment.class.getSimpleName();

    @Inject
    SignUpPresenter mPresenter;

    @Inject
    SignUpView signUpView;

    AuthComponent mAuthComponent;

    public SignUpFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SignUpFragment.
     */
    public static SignUpFragment newInstance() {
        return new SignUpFragment();
    }

    @Override
    public IPresenter getPresenter() {
        return mPresenter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        signUpView.takeView(view);
        mPresenter.onViewCreated(signUpView);
    }

    @Override
    public void onSetUpComponent() {
        mAuthComponent = ((App) getActivity().getApplication()).plusAuthComponent();
        mAuthComponent.inject(this);
    }

    @Override
    public void onKillComponent() {
        ((App) getActivity().getApplication()).removeAuthComponent();
    }
}
