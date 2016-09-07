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

/**
 * Sign in fragment provides methods for signing in, singing up and resetting password(in case failed sign in).
 */
public class SignInFragment extends Fragment {

    public static final String TAG = SignInFragment.class.getSimpleName();
    private SignInPresenter mPresenter;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SignInFragment.
     */
    public static SignInFragment newInstance() {
        return new SignInFragment();
    }

    AuthComponent mAuthComponent;

    public SignInFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mAuthComponent = ((App)getActivity().getApplication()).plusAuthComponent();
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
        SignInView signInView = new SignInView();
        signInView.takeView(view);

        mPresenter = new SignInPresenter();
        mPresenter.setView(signInView);
        mAuthComponent.inject(mPresenter);
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.onStart();
    }

    @Override
    public void onStop() {
        mPresenter.onStop();
        super.onStop();
    }

    @Override
    public void onDetach() {
        ((App)getActivity().getApplication()).removeAuthComponent();
        super.onDetach();
    }
}