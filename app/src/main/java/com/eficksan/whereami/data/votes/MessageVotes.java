package com.eficksan.whereami.data.votes;

/**
 * Created by Aleksei_Ivshin on 9/9/16.
 */
public class MessageVotes {

    public final int votesFor;
    public final int votesAgainst;

    public MessageVotes(int votesFor, int votesAgainst) {
        this.votesFor = votesFor;
        this.votesAgainst = votesAgainst;
    }
}
