package com.eficksan.whereami.data.votes;

import rx.Subscriber;

/**
 * Provides methods for updating message votes.
 */
public interface VotesRepository {

    /**
     * Checks on user did not vote message yet.
     * @param messageId message id
     * @param subscriber subscriber
     */
    void canVoteMessage(String messageId, Subscriber<Boolean> subscriber);

    /**
     * Vote for message.
     * @param messageId message id
     */
    boolean voteMessage(String messageId, boolean isVotedFor);

    /**
     * Fetches votes count.
     * @param messageId messaged id
     * @param subscriber subscriber
     */
    void getVotesCount(String messageId, Subscriber<MessageVotes> subscriber);
}
