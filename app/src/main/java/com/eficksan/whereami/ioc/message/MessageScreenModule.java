package com.eficksan.whereami.ioc.message;

import com.eficksan.whereami.domain.messages.FindMessageInteractor;
import com.eficksan.whereami.domain.users.FindUserInteractor;
import com.eficksan.whereami.domain.votes.DidUserVoteInteractor;
import com.eficksan.whereami.domain.votes.FetchingVotesCountInteractor;
import com.eficksan.whereami.domain.votes.VotingInteractor;
import com.eficksan.whereami.ioc.fragments.FragmentScope;
import com.eficksan.whereami.presentation.message.MessageDetailsPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Aleksei Ivshin
 * on 28.08.2016.
 */
@Module
public class MessageScreenModule {

    @Provides
    @FragmentScope
    public MessageDetailsPresenter provideMessageDetailsPresenter(
            FindUserInteractor findUserInteractor,
            FindMessageInteractor findMessageInteractor,
            DidUserVoteInteractor didUserVoteInteractor,
            VotingInteractor votingInteractor,
            FetchingVotesCountInteractor votesCountInteractor) {
        return new MessageDetailsPresenter(
                findUserInteractor,
                findMessageInteractor,
                didUserVoteInteractor,
                votingInteractor,
                votesCountInteractor);
    }
}
