package com.eficksan.whereami.presentation.auth.signup;


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
import com.eficksan.whereami.presentation.auth.signin.SignInPresenter;
import com.eficksan.whereami.presentation.auth.signin.SignInView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment {


    public static final String TAG = SignUpFragment.class.getSimpleName();
    private SignUpPresenter mPresenter;

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
    public void onAttach(Context context) {
        super.onAttach(context);
        mAuthComponent = ((App)getActivity().getApplication()).plusAuthComponent();
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
        SignUpView signUpView = new SignUpView();
        signUpView.takeView(view);
        mAuthComponent.inject(signUpView);

        mPresenter = new SignUpPresenter();
        mPresenter.setView(signUpView);
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
