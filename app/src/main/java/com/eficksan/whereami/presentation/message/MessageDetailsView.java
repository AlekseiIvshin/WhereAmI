package com.eficksan.whereami.presentation.message;

import android.view.View;
import android.widget.TextView;

import com.eficksan.whereami.R;
import com.eficksan.whereami.data.auth.User;
import com.eficksan.whereami.data.messages.PlacingMessage;

import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Message details view.
 */
public class MessageDetailsView {

    @Bind(R.id.message_author)
    TextView author;

    @Bind(R.id.message_location)
    TextView location;

    @Bind(R.id.message_text)
    TextView text;

    public void takeView(View view) {
        ButterKnife.bind(this, view);
    }

    public void showMessage(PlacingMessage placingMessage) {
        location.setText(String.format(Locale.getDefault(), "%fx%f", placingMessage.latitude, placingMessage.longitude));
        text.setText(placingMessage.message);
    }

    public void showAuthor(User user) {
        author.setText(user.name);
    }
}
