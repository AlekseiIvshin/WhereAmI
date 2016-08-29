package com.eficksan.whereami.presentation.messaging;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eficksan.whereami.App;
import com.eficksan.whereami.R;
import com.eficksan.whereami.domain.Constants;
import com.eficksan.whereami.ioc.messaging.MessagingComponent;

/**
 */
public class MessageFragment extends Fragment {

    public static final String TAG = MessageFragment.class.getSimpleName();
    private MessagingPresenter mPresenter;
    MessagingComponent mMessagingComponent;

    public MessageFragment() {
        // Required empty public constructor
    }

    public static MessageFragment newInstance(Location location) {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.EXTRA_LOCATION_DATA, location);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       mMessagingComponent = ((App) getActivity().getApplication()).plusMessagingComponent();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_message, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MessagingView messagingView = new MessagingView();
        messagingView.takeView(view);
        mPresenter = new MessagingPresenter();
        mMessagingComponent.inject(messagingView);
        mMessagingComponent.inject(mPresenter);
        mPresenter.setView(messagingView);
    }

    @Override
    public void onStart() {
        super.onStart();
        Location mMessageLocation = getArguments().getParcelable(Constants.EXTRA_LOCATION_DATA);
        mPresenter.onStart(mMessageLocation);
    }

    @Override
    public void onStop() {
        mPresenter.onStop();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        mMessagingComponent = null;
        ((App)getActivity().getApplication()).removeMessagingComponent();
        super.onDestroy();
    }
}
