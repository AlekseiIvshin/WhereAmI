package com.eficksan.whereami.domain.votes;

import com.eficksan.whereami.App;
import com.eficksan.whereami.BuildConfig;
import com.eficksan.whereami.data.votes.FirebaseDatabaseVotesRepository;
import com.eficksan.whereami.data.votes.Vote;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import rx.observers.TestSubscriber;
import rx.schedulers.TestScheduler;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by Aleksei Ivshin
 * on 10.09.2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class,
        application = App.class)
public class VotingInteractorTest {

    VotingInteractor votingInteractor;
    FirebaseDatabaseVotesRepository mockVotesRepository;

    @Before
    public void setUp() {
        TestScheduler scheduler = new TestScheduler();
        mockVotesRepository = Mockito.mock(FirebaseDatabaseVotesRepository.class);
        votingInteractor = new VotingInteractor(mockVotesRepository,scheduler,scheduler);
    }

    @After
    public void tearDown() {
        if (votingInteractor != null) {
            votingInteractor.unsubscribe();
        }
    }

    @Test
    @Ignore
    public void shouldReturnTrueWhenUserSuccessfullyVoteForMessage() {
        // Given
        final Vote vote = new Vote();
        vote.isVotedFor = true;
        vote.messageId = "messageId";

        when(mockVotesRepository.voteMessage(vote.messageId, vote.isVotedFor)).thenReturn(true);

        TestSubscriber<Boolean> testSubscriber = new TestSubscriber<>();

        // When
        votingInteractor.execute(vote, testSubscriber);

        // Then
        testSubscriber.awaitTerminalEvent();
        testSubscriber.assertNoErrors();
        testSubscriber.assertCompleted();
        testSubscriber.assertValue(true);
    }

    @Test
    @Ignore
    public void shouldReturnTrueWhenUserSuccessfullyVoteAgainstMessage() {
        // Given
        final Vote vote = new Vote();
        vote.isVotedFor = false;
        vote.messageId = "messageId";

        when(mockVotesRepository.voteMessage(vote.messageId, vote.isVotedFor)).thenReturn(true);

        TestSubscriber<Boolean> testSubscriber = new TestSubscriber<>();

        // When
        votingInteractor.execute(vote, testSubscriber);

        // Then
        testSubscriber.awaitTerminalEvent();
        testSubscriber.assertNoErrors();
        testSubscriber.assertCompleted();
        testSubscriber.assertValue(true);
    }

    @Test
    @Ignore
    public void shouldReturnFalseWhenUserVotingWasFailed() {
        // Given
        final Vote vote = new Vote();
        vote.isVotedFor = false;
        vote.messageId = "messageId";

        when(mockVotesRepository.voteMessage(anyString(), anyBoolean())).thenReturn(false);

        TestSubscriber<Boolean> testSubscriber = new TestSubscriber<>();

        // When
        votingInteractor.execute(vote, testSubscriber);

        // Then
        testSubscriber.awaitTerminalEvent();
        testSubscriber.assertNoErrors();
        testSubscriber.assertCompleted();
        testSubscriber.assertValue(false);
    }
}
