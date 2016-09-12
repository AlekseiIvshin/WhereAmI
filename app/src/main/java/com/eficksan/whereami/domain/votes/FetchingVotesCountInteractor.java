package com.eficksan.whereami.domain.votes;

import com.eficksan.whereami.data.votes.MessageVotes;
import com.eficksan.whereami.data.votes.VotesDataSource;
import com.eficksan.whereami.domain.BaseInteractor;

import rx.Observable;
import rx.Scheduler;

/**
 * Interactor fetches message votes count.
 */
public class FetchingVotesCountInteractor extends BaseInteractor<String, MessageVotes> {

    private final VotesDataSource votesDataSource;

    public FetchingVotesCountInteractor(VotesDataSource votesDataSource, Scheduler jobScheduler, Scheduler uiScheduler) {
        super(jobScheduler, uiScheduler);
        this.votesDataSource = votesDataSource;
    }

    @Override
    protected Observable<MessageVotes> buildObservable(String messageId) {
        return votesDataSource.fetchMessageVotes(messageId);
    }
}
