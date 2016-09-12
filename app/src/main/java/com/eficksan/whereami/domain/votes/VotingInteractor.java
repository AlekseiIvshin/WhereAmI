package com.eficksan.whereami.domain.votes;

import com.eficksan.whereami.data.votes.Vote;
import com.eficksan.whereami.data.votes.VotesRepository;
import com.eficksan.whereami.domain.BaseInteractor;

import rx.Observable;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Provides methods for voting message.
 */
public class VotingInteractor extends BaseInteractor<Vote, Boolean>{

    private final VotesRepository votesRepository;

    public VotingInteractor(VotesRepository votesRepository, Scheduler jobScheduler, Scheduler uiScheduler) {
        super(jobScheduler, uiScheduler);
        this.votesRepository = votesRepository;
    }

    @Override
    protected Observable<Boolean> buildObservable(final Vote parameter) {
        return Observable.just(parameter)
                .subscribeOn(jobScheduler)
                .map(new Func1<Vote, Boolean>() {
                    @Override
                    public Boolean call(Vote vote) {
                        return votesRepository.voteMessage(parameter.messageId, parameter.isVotedFor);
                    }
                });
    }
}
