package com.eficksan.whereami.ioc.message;

import com.eficksan.whereami.data.auth.UsersDataSource;
import com.eficksan.whereami.data.messages.MessagesDataSource;
import com.eficksan.whereami.domain.messages.FindMessageInteractor;
import com.eficksan.whereami.domain.users.FindUserInteractor;
import com.eficksan.whereami.domain.votes.DidUserVoteInteractor;
import com.eficksan.whereami.domain.votes.FetchingVotesCountInteractor;
import com.eficksan.whereami.domain.votes.VotingInteractor;
import com.eficksan.whereami.ioc.fragments.FragmentScope;
import com.eficksan.whereami.presentation.message.MessageDetailsPresenter;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import rx.Scheduler;

/**
 * Created by Aleksei Ivshin
 * on 28.08.2016.
 */
@Module
@FragmentScope
public class MessageModule {

    @Provides
    public MessageDetailsPresenter provideMessageDetailsPresenter(FindUserInteractor findUserInteractor, FindMessageInteractor findMessageInteractor, DidUserVoteInteractor didUserVoteInteractor, VotingInteractor votingInteractor, FetchingVotesCountInteractor votesCountInteractor) {
        return new MessageDetailsPresenter(findUserInteractor, findMessageInteractor, didUserVoteInteractor, votingInteractor, votesCountInteractor);
    }

    @Provides
    public FindMessageInteractor provideFindMessageInteractor(MessagesDataSource messagesDataSource, @Named("job") Scheduler jobScheduler, @Named("ui") Scheduler uiScheduler) {
        return new FindMessageInteractor(messagesDataSource, jobScheduler, uiScheduler);
    }

    @Provides
    public FindUserInteractor provideFindUserInteractor(UsersDataSource usersDataSource, @Named("job") Scheduler jobScheduler, @Named("ui") Scheduler uiScheduler) {
        return new FindUserInteractor(usersDataSource, jobScheduler,uiScheduler);
    }
}
