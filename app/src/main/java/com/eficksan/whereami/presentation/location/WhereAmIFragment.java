package com.eficksan.whereami.presentation.location;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eficksan.whereami.R;
import com.eficksan.whereami.domain.location.ListenLocationInteractor;
import com.eficksan.whereami.domain.location.LocationServiceInteractor;
import com.eficksan.whereami.routing.Router;

/**
 * Fragment shows current user location.
 */
public class WhereAmIFragment extends Fragment {
    public static final String TAG = WhereAmIFragment.class.getSimpleName();
    WaiPresenter waiPresenter;

    /**
     * New instance factory method.
     *
     * @return new fragment instance
     */
    public static WhereAmIFragment newInstance() {
        return new WhereAmIFragment();
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
        viewHolder.takeView(view);

        WaiView waiView = new WaiView(viewHolder, getActivity().getApplicationContext());
        waiPresenter = new WaiPresenter();
        waiPresenter.onStart((Router) getActivity(), waiView, new ListenLocationInteractor(getActivity()), new LocationServiceInteractor(getActivity()));
    }

    @Override
    public void onDestroyView() {
        waiPresenter.onStop();
        super.onDestroyView();
    }
}
