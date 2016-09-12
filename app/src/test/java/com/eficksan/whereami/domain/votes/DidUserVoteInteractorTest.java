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
import rx.Scheduler;
import rx.Subscriber;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class,
        application = App.class)
public class DidUserVoteInteractorTest {

    static String messageId = "messageId";

    VotesDataSource mockVotesDataSource;

    DidUserVoteInteractor interactor;
    Scheduler scheduler = Schedulers.immediate();

    @Before
    public void setUp() {
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
        Observable<Boolean> stubChannel = Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(userVote);
                subscriber.onCompleted();
            }
        });

        when(mockVotesDataSource.fetchUserMessageVote(anyString(), anyString()))
                .thenReturn(stubChannel);

        // When
        interactor.execute(messageId, testSubscriber);

        // Then
        testSubscriber.awaitTerminalEvent();
        verify(mockVotesDataSource, times(1)).fetchUserMessageVote(anyString(), anyString());
        testSubscriber.assertCompleted();
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
        verify(mockVotesDataSource, times(1)).fetchUserMessageVote(anyString(), anyString());
        testSubscriber.assertCompleted();
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
        verify(mockVotesDataSource, times(1)).fetchUserMessageVote(anyString(), anyString());
        testSubscriber.assertCompleted();
        testSubscriber.assertValue(false);
    }
}
