package com.eficksan.whereami.presentation.message;

import com.eficksan.whereami.data.auth.User;
import com.eficksan.whereami.data.messages.PlacingMessage;
import com.eficksan.whereami.data.votes.MessageVotes;
import com.eficksan.whereami.data.votes.Vote;
import com.eficksan.whereami.domain.messages.FindMessageInteractor;
import com.eficksan.whereami.domain.users.FindUserInteractor;
import com.eficksan.whereami.domain.votes.DidUserVoteInteractor;
import com.eficksan.whereami.domain.votes.FetchingVotesCountInteractor;
import com.eficksan.whereami.domain.votes.VotingInteractor;

import javax.inject.Inject;

import rx.Subscriber;
import rx.functions.Action1;

/**
 * Message details presenter.
 */
public class MessageDetailsPresenter {

    @Inject
    FindUserInteractor findUserInteractor;

    @Inject
    FindMessageInteractor findMessageInteractor;

    @Inject
    DidUserVoteInteractor didUserVoteInteractor;

    @Inject
    VotingInteractor votingInteractor;

    @Inject
    FetchingVotesCountInteractor votesCountInteractor;

    private String mMessageId;

    private Subscriber<User> userSubscriber = new Subscriber<User>() {
        @Override
        public void onCompleted() {
            findUserInteractor.unsubscribe();
        }

        @Override
        public void onError(Throwable e) {
            findUserInteractor.unsubscribe();
        }

        @Override
        public void onNext(User user) {
            mDetailsView.showAuthor(user);

        }
    };

    private Subscriber<PlacingMessage> placingMessageSubscriber = new Subscriber<PlacingMessage>() {
        @Override
        public void onCompleted() {
            findMessageInteractor.unsubscribe();
        }

        @Override
        public void onError(Throwable e) {
            findMessageInteractor.unsubscribe();
        }

        @Override
        public void onNext(PlacingMessage placingMessage) {
            mDetailsView.showMessage(placingMessage);
            findUserInteractor.execute(placingMessage.userId, userSubscriber);
        }
    };

    private Subscriber<Boolean> voteAvailabilitySubscriber = new Subscriber<Boolean>() {
        @Override
        public void onCompleted() {
            didUserVoteInteractor.unsubscribe();
        }

        @Override
        public void onError(Throwable e) {
            didUserVoteInteractor.unsubscribe();
        }

        @Override
        public void onNext(Boolean aBoolean) {
            if (aBoolean) {
                mDetailsView.showVoting();
            } else {
                mDetailsView.hideVoting();
                votesCountInteractor.execute(mMessageId, votesCountSubscriber);
            }
        }
    };

    private Subscriber<Boolean> voteResultSubscriber = new Subscriber<Boolean>() {
        @Override
        public void onCompleted() {
            votingInteractor.unsubscribe();
        }

        @Override
        public void onError(Throwable e) {
            votingInteractor.unsubscribe();
        }

        @Override
        public void onNext(Boolean aBoolean) {
            if (aBoolean) {
                mDetailsView.hideVoting();
                votesCountInteractor.execute(mMessageId, votesCountSubscriber);
            }
        }
    };

    private MessageDetailsView mDetailsView;
    private Subscriber<MessageVotes> votesCountSubscriber = new Subscriber<MessageVotes>() {
        @Override
        public void onCompleted() {
            votesCountInteractor.unsubscribe();
        }

        @Override
        public void onError(Throwable e) {
            votesCountInteractor.unsubscribe();
        }

        @Override
        public void onNext(MessageVotes messageVotes) {
            if (messageVotes.votesFor > 0 || messageVotes.votesAgainst > 0) {
                mDetailsView.showVotesCount(messageVotes.votesFor, messageVotes.votesAgainst);
            }
        }
    };

    public void setView(MessageDetailsView detailsView) {
        this.mDetailsView = detailsView;
    }

    public void onCreate(final String messageId) {
        mMessageId = messageId;
        mDetailsView.subscribeOnVotingForClick(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Vote vote = new Vote();
                vote.messageId = messageId;
                vote.isVotedFor = true;
                votingInteractor.execute(vote, voteResultSubscriber);
            }
        });

        mDetailsView.subscribeOnVotingAgainstClick(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Vote vote = new Vote();
                vote.messageId = messageId;
                vote.isVotedFor = false;
                votingInteractor.execute(vote, voteResultSubscriber);
            }
        });

        // TODO: check on user authenticated before
        findMessageInteractor.execute(messageId, placingMessageSubscriber);
        didUserVoteInteractor.execute(messageId, voteAvailabilitySubscriber);
    }

    public void onDestroy() {
        votesCountInteractor.unsubscribe();
        votingInteractor.unsubscribe();
        didUserVoteInteractor.unsubscribe();
        findMessageInteractor.unsubscribe();
        findUserInteractor.unsubscribe();

        mDetailsView.destroy();
    }
}
