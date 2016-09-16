package com.eficksan.whereami.domain.votes;

import android.support.annotation.NonNull;

import com.eficksan.whereami.data.votes.VotesDataSource;
import com.eficksan.whereami.domain.BaseInteractor;

import rx.Observable;
import rx.Scheduler;
import rx.functions.Func1;

/**
 * Interactor for checking user available to vote message.
 */
public class DidUserVoteInteractor extends BaseInteractor<String, Boolean> {

    private final VotesDataSource dataSource;
    private final String userId;

    public DidUserVoteInteractor(
            VotesDataSource dataSource,
            @NonNull String userId,
            Scheduler jobScheduler, Scheduler uiScheduler) {
        super(jobScheduler, uiScheduler);
        this.dataSource = dataSource;
        this.userId = userId;
    }

    @Override
    protected Observable<Boolean> buildObservable(String messageId) {
        return dataSource.fetchUserMessageVote(messageId, userId)
                .map(new Func1<Boolean, Boolean>() {
                    @Override
                    public Boolean call(Boolean userVote) {
                        return userVote != null;
                    }
                });
    }

}
