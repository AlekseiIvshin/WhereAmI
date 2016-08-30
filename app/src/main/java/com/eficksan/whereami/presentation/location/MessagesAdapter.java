package com.eficksan.whereami.presentation.location;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eficksan.placingmessages.PlaceMessage;
import com.eficksan.whereami.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Aleksei Ivshin
 * on 30.08.2016.
 */
public class MessagesAdapter extends RecyclerView.Adapter<MessagesViewHolder> {

    private ArrayList<PlaceMessage> mPlaceMessages;

    public void setMessages(List<PlaceMessage> messages) {
        mPlaceMessages = new ArrayList<>();
        mPlaceMessages.addAll(messages);
        notifyDataSetChanged();
    }

    @Override
    public MessagesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place_message, parent, false);
        return new MessagesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MessagesViewHolder holder, int position) {
        holder.setContent(mPlaceMessages.get(position));
    }

    @Override
    public int getItemCount() {
        if (mPlaceMessages != null) {
            return mPlaceMessages.size();
        }
        return 0;
    }
}
