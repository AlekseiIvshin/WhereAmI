package com.eficksan.whereami.presentation.message;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.eficksan.whereami.R;
import com.eficksan.whereami.data.auth.User;
import com.eficksan.whereami.data.messages.PlacingMessage;

import java.util.Locale;

import javax.inject.Inject;

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

    @Bind(R.id.message_vote_for)
    Button voteFor;

    @Bind(R.id.message_vote_against)
    Button voteAgainst;

    @Bind(R.id.message_vote_for_count)
    TextView voteForCount;

    @Bind(R.id.message_vote_against_count)
    TextView voteAgainstCount;

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

    public void showVoting() {
        if (View.VISIBLE != voteFor.getVisibility() || View.VISIBLE != voteAgainst.getVisibility()) {
            voteFor.setVisibility(View.VISIBLE);
            voteAgainst.setVisibility(View.VISIBLE);
        }
    }

    public void hideVoting() {
        if (View.GONE != voteFor.getVisibility() || View.GONE != voteAgainst.getVisibility()) {
            voteFor.setVisibility(View.GONE);
            voteAgainst.setVisibility(View.GONE);
        }
    }

    public void showVotesCount(int countVotesFor, int countVotesAgainst) {
        if (View.VISIBLE != voteForCount.getVisibility() || View.VISIBLE != voteAgainstCount.getVisibility()) {
            voteForCount.setVisibility(View.VISIBLE);
            voteAgainstCount.setVisibility(View.VISIBLE);
        }

        voteForCount.setText(String.valueOf(countVotesFor));
        voteAgainstCount.setText(String.valueOf(countVotesAgainst));
    }

}
