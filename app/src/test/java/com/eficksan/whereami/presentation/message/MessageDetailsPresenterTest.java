package com.eficksan.whereami.presentation.message;

import com.eficksan.whereami.domain.messages.FindMessageInteractor;
import com.eficksan.whereami.domain.users.FindUserInteractor;
import com.eficksan.whereami.domain.votes.DidUserVoteInteractor;
import com.eficksan.whereami.domain.votes.FetchingVotesCountInteractor;
import com.eficksan.whereami.domain.votes.VotingInteractor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;

/**
 * Created by Aleksei_Ivshin on 9/13/16.
 */
public class MessageDetailsPresenterTest {


    FindUserInteractor mockFindUserInteractor;

    FindMessageInteractor mockFindMessageInteractor;

    DidUserVoteInteractor mockDidUserVoteInteractor;

    VotingInteractor mockVotingInteractor;

    FetchingVotesCountInteractor mockVotesCountInteractor;

    MessageDetailsPresenter presenter;
    MessageDetailsView mockView;

    @Before
    public void setUp() throws Exception {
        mockFindMessageInteractor = mock(FindMessageInteractor.class);
        mockFindUserInteractor = mock(FindUserInteractor.class);
        mockDidUserVoteInteractor = mock(DidUserVoteInteractor.class);
        mockVotingInteractor = mock(VotingInteractor.class);
        mockVotesCountInteractor = mock(FetchingVotesCountInteractor.class);
        presenter = new MessageDetailsPresenter(mockFindUserInteractor, mockFindMessageInteractor, mockDidUserVoteInteractor, mockVotingInteractor, mockVotesCountInteractor);

        mockView = mock(MessageDetailsView.class);
        presenter.setView(mockView);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testOnCreate() throws Exception {

    }

    @Test
    public void testOnDestroy() throws Exception {

    }

    @Test
    public void shouldSubscribeOnClickEvents() {

    }

}