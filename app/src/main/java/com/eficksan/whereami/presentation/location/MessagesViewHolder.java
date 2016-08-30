package com.eficksan.whereami.presentation.location;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.eficksan.placingmessages.PlaceMessage;
import com.eficksan.whereami.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Aleksei Ivshin
 * on 30.08.2016.
 */
public class MessagesViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.message_location)
    public TextView messageLocation;

    @Bind(R.id.message_text)
    public TextView messageText;

    @Bind(R.id.message_user)
    public TextView messageUser;

    public MessagesViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setContent(PlaceMessage content) {
        messageText.setText(content.message);
        messageUser.setText(content.userId);
        messageLocation.setText(String.format("%f x %f", content.latitude, content.longitude));
    }
}
