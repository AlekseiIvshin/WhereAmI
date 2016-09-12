package com.eficksan.whereami.ioc.message;

import com.eficksan.whereami.data.votes.VotesDataSource;
import com.eficksan.whereami.data.votes.VotesRepository;
import com.eficksan.whereami.domain.votes.DidUserVoteInteractor;
import com.eficksan.whereami.domain.votes.FetchingVotesCountInteractor;
import com.eficksan.whereami.domain.votes.VotingInteractor;
import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import rx.Scheduler;

/**
 * Created by Aleksei Ivshin
 * on 28.08.2016.
 */
@Module
public class VoteModule {

    @Provides
    public DidUserVoteInteractor provideDidUserVoteInteractor(VotesDataSource votesDataSource, FirebaseAuth firebaseAuth) {
        return new DidUserVoteInteractor(votesDataSource, firebaseAuth);
    }

    @Provides
    public VotingInteractor provideVotingInteractor(VotesRepository votesRepository, @Named("job")Scheduler jobScheduler,@Named("ui")Scheduler uiScheduler) {
        return new VotingInteractor(votesRepository, jobScheduler, uiScheduler);
    }

    @Provides
    public FetchingVotesCountInteractor provideFetchingVotesCountInteractor(VotesRepository votesRepository) {
        return new FetchingVotesCountInteractor(votesRepository);
    }
}
