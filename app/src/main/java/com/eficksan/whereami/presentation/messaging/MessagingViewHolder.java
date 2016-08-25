package com.eficksan.whereami.presentation.messaging;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;

import com.eficksan.whereami.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Aleksei Ivshin
 * on 24.08.2016.
 */
public class MessagingViewHolder {

    @Bind(R.id.input_layout_message)
    public TextInputLayout messageInputLayout;
    @Bind(R.id.input_message)
    public EditText messageInput;
    @Bind(R.id.create_message)
    public FloatingActionButton sendMessage;

    public void takeView(View view) {
        ButterKnife.bind(this, view);
    }
}
