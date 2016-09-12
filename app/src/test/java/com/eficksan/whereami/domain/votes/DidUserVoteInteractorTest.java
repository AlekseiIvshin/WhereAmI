package com.eficksan.whereami.domain.votes;

import com.eficksan.whereami.App;
import com.eficksan.whereami.BuildConfig;
import com.eficksan.whereami.data.votes.VotesDataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import rx.Observable;
import rx.Subscriber;
import rx.observers.TestSubscriber;
import rx.schedulers.TestScheduler;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class,
        application = App.class)
public class DidUserVoteInteractorTest {

    static String messageId = "messageId";

    VotesDataSource mockVotesDataSource;

    DidUserVoteInteractor interactor;

    @Before
    public void setUp() {
        TestScheduler scheduler = new TestScheduler();
        mockVotesDataSource = mock(VotesDataSource.class);
        interactor = new DidUserVoteInteractor(mockVotesDataSource, "UserId", scheduler, scheduler);
    }

    @After
    public void tearDown() {
        interactor.unsubscribe();
    }

    @Test
    public void shouldReturnTrueWhenUserVotedForBefore() {
        // Given
        final Boolean userVote = true;

        TestSubscriber<Boolean> testSubscriber = new TestSubscriber<>();
        when(mockVotesDataSource.fetchUserMessageVote(anyString(), anyString())).thenReturn(Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(userVote);
                subscriber.onCompleted();
            }
        }));

        // When
        interactor.execute(messageId, testSubscriber);

        // Then
        testSubscriber.awaitTerminalEvent();
        testSubscriber.onCompleted();
        testSubscriber.assertValue(true);
    }

    @Test
    public void shouldReturnTrueWhenUserVotedAgainstBefore() {
        // Given
        final Boolean userVote = false;

        TestSubscriber<Boolean> testSubscriber = new TestSubscriber<>();
        when(mockVotesDataSource.fetchUserMessageVote(anyString(), anyString())).thenReturn(Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(userVote);
                subscriber.onCompleted();
            }
        }));

        // When
        interactor.execute(messageId, testSubscriber);

        // Then
        testSubscriber.awaitTerminalEvent();
        testSubscriber.onCompleted();
        testSubscriber.assertValue(true);
    }

    @Test
    public void shouldReturnTrueWhenDidNotUserVoteBefore() {
        // Given
        final Boolean userVote = null;

        TestSubscriber<Boolean> testSubscriber = new TestSubscriber<>();
        when(mockVotesDataSource.fetchUserMessageVote(anyString(), anyString())).thenReturn(Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(userVote);
                subscriber.onCompleted();
            }
        }));

        // When
        interactor.execute(messageId, testSubscriber);

        // Then
        testSubscriber.awaitTerminalEvent();
        testSubscriber.onCompleted();
        testSubscriber.assertValue(false);
    }
}
