package com.eficksan.whereami.domain.votes;

import com.eficksan.whereami.data.votes.Vote;
import com.eficksan.whereami.data.votes.VotesDataSource;
import com.eficksan.whereami.domain.BaseInteractor;

import rx.Observable;
import rx.Scheduler;

/**
 * Provides methods for voting message.
 */
public class VotingInteractor extends BaseInteractor<Vote, Boolean> {

    private final VotesDataSource votesRepository;
    private final String userId;

    public VotingInteractor(
            VotesDataSource votesRepository,
            String userId,
            Scheduler jobScheduler, Scheduler uiScheduler) {
        super(jobScheduler, uiScheduler);
        this.votesRepository = votesRepository;
        this.userId = userId;
    }

    @Override
    protected Observable<Boolean> buildObservable(final Vote vote) {
        return votesRepository.voteMessage(userId, vote.messageId, vote.isVotedFor);
    }
}
