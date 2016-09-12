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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageVotes that = (MessageVotes) o;

        if (votesFor != that.votesFor) return false;
        return votesAgainst == that.votesAgainst;

    }

    @Override
    public int hashCode() {
        int result = votesFor;
        result = 31 * result + votesAgainst;
        return result;
    }
}
