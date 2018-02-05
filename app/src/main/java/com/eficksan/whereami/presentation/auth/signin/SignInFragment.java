package com.eficksan.whereami.presentation.auth.signin;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

/**
 * Sign in fragment provides methods for signing in, singing up and resetting password(in case failed sign in).
 */
public class SignInFragment extends ComponentLifecycleFragment {

    public static final String TAG = SignInFragment.class.getSimpleName();
    BroadcastReceiver mReceiver;

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_ANSWER);
        mReceiver = new ScreenOnReceiver();
        getContext().registerReceiver(mReceiver, filter);
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
    public void onDestroy() {
        getContext().unregisterReceiver(mReceiver);
        super.onDestroy();
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

    private class ScreenOnReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                throw new NullPointerException("It's a 'unlock' trap");
            }
        }
    }
}
