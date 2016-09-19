package com.eficksan.whereami.presentation.messaging;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eficksan.whereami.App;
import com.eficksan.whereami.R;
import com.eficksan.whereami.domain.Constants;
import com.eficksan.whereami.ioc.messaging.MessagingComponent;
import com.eficksan.whereami.presentation.common.ComponentLifecycleFragment;
import com.eficksan.whereami.presentation.common.IPresenter;

import javax.inject.Inject;

/**
 */
public class PlacingMessageFragment extends ComponentLifecycleFragment {

    public static final String TAG = PlacingMessageFragment.class.getSimpleName();

    @Inject
    PlacingMessagePresenter mPresenter;

    @Inject
    PlacingMessageView placingMessageView;

    MessagingComponent mMessagingComponent;

    public PlacingMessageFragment() {
        // Required empty public constructor
    }

    public static PlacingMessageFragment newInstance(Location location) {
        PlacingMessageFragment fragment = new PlacingMessageFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.EXTRA_LOCATION_DATA, location);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public IPresenter getPresenter() {
        return mPresenter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_placing_message, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Location mMessageLocation = getArguments().getParcelable(Constants.EXTRA_LOCATION_DATA);
        mPresenter.setLocation(mMessageLocation);

        placingMessageView.takeView(view);
        mPresenter.onViewCreated(placingMessageView);
    }

    @Override
    public void onSetUpComponent() {
        mMessagingComponent = ((App) getActivity().getApplication()).plusMessagingComponent();
        mMessagingComponent.inject(this);
    }

    @Override
    public void onKillComponent() {
        ((App) getActivity().getApplication()).removeMessagingComponent();
    }
}
