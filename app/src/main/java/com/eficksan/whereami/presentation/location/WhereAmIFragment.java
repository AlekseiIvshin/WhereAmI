package com.eficksan.whereami.presentation.location;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eficksan.whereami.App;
import com.eficksan.whereami.R;
import com.eficksan.whereami.ioc.location.LocationComponent;
import com.eficksan.whereami.presentation.common.ComponentLifecycleFragment;
import com.eficksan.whereami.presentation.common.IPresenter;

import javax.inject.Inject;

/**
 * Fragment shows current user location.
 */
public class WhereAmIFragment extends ComponentLifecycleFragment {
    public static final String TAG = WhereAmIFragment.class.getSimpleName();

    LocationComponent mLocationComponent;

    @Inject
    WhereAmIPresenter mPresenter;

    @Inject
    WhereAmIView whereAmIView;

    /**
     * New instance factory method.
     *
     * @return new fragment instance
     */
    public static WhereAmIFragment newInstance() {
        return new WhereAmIFragment();
    }

    @Override
    public IPresenter getPresenter() {
        return mPresenter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_where_am_i, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        whereAmIView.takeView(view);
        mPresenter.setView(whereAmIView);
    }

    @Override
    public void onSetUpComponent() {
        mLocationComponent = ((App) getActivity().getApplication()).plusLocationComponent();
        mLocationComponent.inject(this);
    }

    @Override
    public void onKillComponent() {
        ((App) getActivity().getApplication()).removeLocationComponent();
    }
}
