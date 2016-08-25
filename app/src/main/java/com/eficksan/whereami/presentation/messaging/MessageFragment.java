package com.eficksan.whereami.presentation.messaging;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eficksan.whereami.R;
import com.eficksan.whereami.domain.messaging.MessagingInteractor;
import com.eficksan.whereami.domain.Constants;
import com.eficksan.whereami.presentation.routing.Router;

/**
 */
public class MessageFragment extends Fragment {

    public static final String TAG = MessageFragment.class.getSimpleName();
    private MessagingPresenter messagingPresenter;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_message, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        MessagingViewHolder viewHolder = new MessagingViewHolder();
        viewHolder.takeView(getView());

        Location mMessageLocation = getArguments().getParcelable(Constants.EXTRA_LOCATION_DATA);
        messagingPresenter = new MessagingPresenter();
        messagingPresenter.onStart((Router) getActivity(), new MessagingView(getActivity().getApplicationContext(), viewHolder), new MessagingInteractor(getActivity()), mMessageLocation);
    }

    @Override
    public void onStop() {
        messagingPresenter.onStop();
        super.onStop();
    }
}
