package com.eficksan.whereami.domain;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.observers.TestSubscriber;
import rx.schedulers.TestScheduler;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Aleksei Ivshin
 * on 12.09.2016.
 */
@RunWith(JUnit4.class)
public class BaseInteractorTest {

    private BaseInteractorImpl interactor;

    @Before
    public void setUp() {
        TestScheduler testScheduler = new TestScheduler();
        interactor = new BaseInteractorImpl(testScheduler, testScheduler);
    }

    @Test
    public void shouldBuildObservableAndReturnCorrectResult() {
        // Given
        TestSubscriber<String> testSubscriber = new TestSubscriber<>();

        // When
        interactor.execute("Test", testSubscriber);

        // Then
        assertThat(testSubscriber.getOnNextEvents().size(), is(0));
    }

    @Test
    public void shouldUnsubscribeWhenItCalled() {
        // Given
        TestSubscriber<String> testSubscriber = new TestSubscriber<>();

        // When
        interactor.execute("Test", testSubscriber);
        interactor.unsubscribe();

        // Then
        assertThat(testSubscriber.isUnsubscribed(), is(true));

    }

    private static class BaseInteractorImpl extends BaseInteractor<String, String> {

        protected BaseInteractorImpl(Scheduler jobScheduler, Scheduler uiScheduler) {
            super(jobScheduler, uiScheduler);
        }

        @Override
        protected Observable<String> buildObservable(String parameter) {
            return Observable.empty();
        }

        @Override
        public void execute(String parameter, Subscriber<String> subscriber) {
            super.execute(parameter, subscriber);
        }
    }

}
