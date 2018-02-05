package com.eficksan.whereami.presentation.message;

import android.os.Bundle;

import com.eficksan.whereami.data.auth.User;
import com.eficksan.whereami.data.messages.PlacingMessage;
import com.eficksan.whereami.data.votes.MessageVotes;
import com.eficksan.whereami.data.votes.Vote;
import com.eficksan.whereami.domain.messages.FindMessageInteractor;
import com.eficksan.whereami.domain.users.FindUserInteractor;
import com.eficksan.whereami.domain.votes.DidUserVoteInteractor;
import com.eficksan.whereami.domain.votes.FetchingVotesCountInteractor;
import com.eficksan.whereami.domain.votes.VotingInteractor;
import com.eficksan.whereami.presentation.common.BasePresenter;

import javax.inject.Inject;

import rx.Subscriber;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Message details presenter.
 */
public class MessageDetailsPresenter extends BasePresenter<MessageDetailsView> {

    final FindUserInteractor findUserInteractor;

    final FindMessageInteractor findMessageInteractor;

    final DidUserVoteInteractor didUserVoteInteractor;

    final VotingInteractor votingInteractor;

    final FetchingVotesCountInteractor votesCountInteractor;

    private String mMessageId;

    protected CompositeSubscription eventsSubcription;

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
            mView.showAuthor(user);

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
            mView.showMessage(placingMessage);
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
        public void onNext(Boolean wasUserVotedAlready) {
            if (!wasUserVotedAlready) {
                mView.showVoting();
            } else {
                mView.hideVoting();
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
                mView.hideVoting();
                votesCountInteractor.execute(mMessageId, votesCountSubscriber);
            }
        }
    };

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
                mView.showVotesCount(messageVotes.votesFor, messageVotes.votesAgainst);
            }
        }
    };

    @Inject
    public MessageDetailsPresenter(
            FindUserInteractor findUserInteractor,
            FindMessageInteractor findMessageInteractor,
            DidUserVoteInteractor didUserVoteInteractor,
            VotingInteractor votingInteractor,
            FetchingVotesCountInteractor votesCountInteractor) {
        this.findUserInteractor = findUserInteractor;
        this.findMessageInteractor = findMessageInteractor;
        this.didUserVoteInteractor = didUserVoteInteractor;
        this.votingInteractor = votingInteractor;
        this.votesCountInteractor = votesCountInteractor;
        eventsSubcription = new CompositeSubscription();
    }

    public void setMessageId(String messageId) {
        this.mMessageId = messageId;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        eventsSubcription.add(mView.getVotingForClickEvents().subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Vote vote = new Vote();
                vote.messageId = mMessageId;
                vote.isVotedFor = true;
                votingInteractor.execute(vote, voteResultSubscriber);
            }
        }));

        eventsSubcription.add(mView.getVotingAgainstClickEvents().subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Vote vote = new Vote();
                vote.messageId = mMessageId;
                vote.isVotedFor = false;
                votingInteractor.execute(vote, voteResultSubscriber);
            }
        }));

        // TODO: check on user authenticated before
        findMessageInteractor.execute(mMessageId, placingMessageSubscriber);
        didUserVoteInteractor.execute(mMessageId, voteAvailabilitySubscriber);
    }

    public void onDestroy() {
        votesCountInteractor.unsubscribe();
        votingInteractor.unsubscribe();
        didUserVoteInteractor.unsubscribe();
        findMessageInteractor.unsubscribe();
        findUserInteractor.unsubscribe();

        eventsSubcription.unsubscribe();
        eventsSubcription.clear();
        super.onDestroy();
    }
}
