package com.eficksan.whereami.domain.votes;

import com.eficksan.whereami.data.votes.Vote;
import com.eficksan.whereami.data.votes.VotesDataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by Aleksei Ivshin
 * on 10.09.2016.
 */
@RunWith(JUnit4.class)
public class VotingInteractorTest {

    VotingInteractor votingInteractor;
    VotesDataSource mockVotesRepository;

    @Before
    public void setUp() {
        Scheduler scheduler = Schedulers.immediate();
        mockVotesRepository = Mockito.mock(VotesDataSource.class);
        votingInteractor = new VotingInteractor(mockVotesRepository, "UserId", scheduler, scheduler);
    }

    @After
    public void tearDown() {
        if (votingInteractor != null) {
            votingInteractor.unsubscribe();
        }
    }

    @Test
    public void shouldReturnTrueWhenUserSuccessfullyVoteMessage() {
        // Given
        final Vote vote = new Vote();
        vote.isVotedFor = true;
        vote.messageId = "MESSAGE_ID";

        when(mockVotesRepository.voteMessage(anyString(), anyString(), anyBoolean()))
                .thenReturn(Observable.create(new Observable.OnSubscribe<Boolean>() {
                    @Override
                    public void call(Subscriber<? super Boolean> subscriber) {
                        subscriber.onNext(true);
                        subscriber.onCompleted();
                    }
                }));

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
    public void shouldReturnFalseWhenUserVotingWasFailed() {
        // Given
        final Vote vote = new Vote();
        vote.isVotedFor = false;
        vote.messageId = "MESSAGE_ID";

        when(mockVotesRepository.voteMessage(anyString(), anyString(), anyBoolean()))
                .thenReturn(Observable.create(new Observable.OnSubscribe<Boolean>() {
                    @Override
                    public void call(Subscriber<? super Boolean> subscriber) {
                        subscriber.onNext(false);
                        subscriber.onCompleted();
                    }
                }));

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
