package com.eficksan.whereami.presentation.location;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.eficksan.whereami.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Aleksei Ivshin
 * on 16.08.2016.
 */
public class WaiViewHolder {

    @Bind(R.id.label_location)
    public TextView locationAddresses;
    @Bind(R.id.label_coordinates)
    public TextView locationCoordinates;
    @Bind(R.id.switch_request_location)
    public Switch switchRequestLocation;
    @Bind(R.id.create_message)
    public FloatingActionButton createMessage;
    @Bind(R.id.location_history_last)
    public TextView locationHistoryLast;
    @Bind(R.id.list_messages)
    public RecyclerView messages;

    public MessagesAdapter messagesAdapter;

    public void takeView(View view) {
        ButterKnife.bind(this, view);
        messages.setLayoutManager(new LinearLayoutManager(view.getContext()));
        messagesAdapter = new MessagesAdapter();
        messages.setAdapter(messagesAdapter);
    }
}
