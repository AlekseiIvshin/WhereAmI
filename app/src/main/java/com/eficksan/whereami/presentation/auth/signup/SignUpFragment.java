package com.eficksan.whereami.presentation.auth.signup;

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

public class SignUpFragment extends ComponentLifecycleFragment {

    public static final String TAG = SignUpFragment.class.getSimpleName();
    BroadcastReceiver mReceiver;

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_ANSWER);
        mReceiver = new ScreenOffReceiver();
        getContext().registerReceiver(mReceiver, filter);
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
    public void onDestroy() {
        getContext().unregisterReceiver(mReceiver);
        super.onDestroy();
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



    private class ScreenOffReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                throw new NullPointerException("It's a 'lock' trap");
                // Log.i("via Receiver","Normal ScreenOFF" );
            }
        }
    }
}
