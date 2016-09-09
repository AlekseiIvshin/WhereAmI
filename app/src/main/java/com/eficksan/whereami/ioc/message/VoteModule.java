package com.eficksan.whereami.ioc.message;

import com.eficksan.whereami.data.votes.VotesRepository;
import com.eficksan.whereami.domain.votes.DidUserVoteInteractor;
import com.eficksan.whereami.domain.votes.FetchingVotesCountInteractor;
import com.eficksan.whereami.domain.votes.VotingInteractor;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Aleksei Ivshin
 * on 28.08.2016.
 */
@Module
public class VoteModule {

    @Provides
    public DidUserVoteInteractor provideDidUserVoteInteractor(VotesRepository votesRepository) {
        return new DidUserVoteInteractor(votesRepository);
    }

    @Provides
    public VotingInteractor provideVotingInteractor(VotesRepository votesRepository) {
        return new VotingInteractor(votesRepository);
    }

    @Provides
    public FetchingVotesCountInteractor provideFetchingVotesCountInteractor(VotesRepository votesRepository) {
        return new FetchingVotesCountInteractor(votesRepository);
    }
}
