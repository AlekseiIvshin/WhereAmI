package com.eficksan.whereami.domain.votes;

import com.eficksan.whereami.App;
import com.eficksan.whereami.BuildConfig;
import com.eficksan.whereami.data.votes.MessageVotes;
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
public class FetchingVotesCountInteractorTest {

    static String messageId = "messageId";

    VotesDataSource mockVotesDataSource;

    FetchingVotesCountInteractor interactor;
    Scheduler scheduler = Schedulers.immediate();

    @Before
    public void setUp() {
        mockVotesDataSource = mock(VotesDataSource.class);
        interactor = new FetchingVotesCountInteractor(mockVotesDataSource, scheduler, scheduler);
    }

    @After
    public void tearDown() {
        interactor.unsubscribe();
    }

    @Test
    public void shouldReturnMessageVotes() {
        // Given
        final MessageVotes expectedVotes = new MessageVotes(0,1);
        TestSubscriber<MessageVotes> testSubscriber = new TestSubscriber<>();

        Observable<MessageVotes> stubChannel = Observable.create(new Observable.OnSubscribe<MessageVotes>() {
            @Override
            public void call(Subscriber<? super MessageVotes> subscriber) {
                subscriber.onNext(expectedVotes);
                subscriber.onCompleted();
            }
        });

        when(mockVotesDataSource.fetchMessageVotes(anyString()))
                .thenReturn(stubChannel);

        // When
        interactor.execute(messageId, testSubscriber);

        // Then
        testSubscriber.awaitTerminalEvent();
        verify(mockVotesDataSource, times(1)).fetchMessageVotes(anyString());
        testSubscriber.assertCompleted();
        testSubscriber.assertValue(expectedVotes);
    }

}
