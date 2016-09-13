package com.eficksan.whereami.presentation.message;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eficksan.whereami.App;
import com.eficksan.whereami.R;
import com.eficksan.whereami.ioc.message.MessageComponent;

import javax.inject.Inject;

/**
 * Provides view of message details.
 */
public class MessageDetailsFragment extends DialogFragment {

    public static final String ARGS_MESSAGE_ID = "ARGS_MESSAGE_ID";
    public static final String TAG = MessageDetailsFragment.class.getSimpleName();

    MessageComponent messageComponent;

    @Inject
    MessageDetailsPresenter mPresenter;

    public MessageDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MessageDetailsFragment.
     */
    public static MessageDetailsFragment newInstance(String messageId) {
        Bundle args = new Bundle();
        args.putString(ARGS_MESSAGE_ID, messageId);

        MessageDetailsFragment fragment = new MessageDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        messageComponent = ((App) getActivity().getApplication()).plusMessageDetailsComponent();
        messageComponent.inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_message_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MessageDetailsView detailsView = new MessageDetailsView();
        detailsView.takeView(view);

        mPresenter.setView(detailsView);

        mPresenter.onCreate(getArguments().getString(ARGS_MESSAGE_ID));
    }

    @Override
    public void onDestroy() {
        mPresenter.onDestroy();
        ((App) getActivity().getApplication()).removeMessageDetailsComponent();
        super.onDestroy();
    }
}
