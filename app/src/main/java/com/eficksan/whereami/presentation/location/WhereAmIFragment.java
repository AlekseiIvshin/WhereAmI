package com.eficksan.whereami.presentation.location;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eficksan.whereami.App;
import com.eficksan.whereami.R;
import com.eficksan.whereami.ioc.location.LocationComponent;

/**
 * Fragment shows current user location.
 */
public class WhereAmIFragment extends Fragment {
    public static final String TAG = WhereAmIFragment.class.getSimpleName();

    LocationComponent mLocationComponent;

    /**
     * Presenter for location requesting.
     */
    WaiPresenter mPresenter;

    /**
     * New instance factory method.
     *
     * @return new fragment instance
     */
    public static WhereAmIFragment newInstance() {
        return new WhereAmIFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationComponent = ((App)getActivity().getApplication()).plusLocationComponent();
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

        WaiViewHolder viewHolder = new WaiViewHolder();
        viewHolder.takeView(getView());

        WaiView waiView = new WaiView(viewHolder);
        mPresenter = new WaiPresenter();
        mLocationComponent.inject(waiView);
        mLocationComponent.inject(mPresenter);
        mPresenter.setView(waiView);
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
    public void onDestroy() {
        mLocationComponent = null;
        ((App)getActivity().getApplication()).removeLocationComponent();
        super.onDestroy();
    }
}
