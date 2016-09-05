package com.eficksan.whereami.domain;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by Aleksei Ivshin
 * on 22.08.2016.
 */
public abstract class BaseInteractor<ParameterType, ResultType> implements Interactor<ParameterType, ResultType> {

    private Subscription subscription;
    protected final Scheduler jobScheduler;
    private final Scheduler uiScheduler;

    protected BaseInteractor(Scheduler jobScheduler, Scheduler uiScheduler) {
        this.jobScheduler = jobScheduler;
        this.uiScheduler = uiScheduler;
    }

    protected abstract Observable<ResultType> buildObservable(ParameterType parameter);

    public void execute(ParameterType parameter, Subscriber<ResultType> subscriber) {
        subscription = buildObservable(parameter)
                .subscribeOn(jobScheduler)
                .observeOn(uiScheduler)
                .subscribe(subscriber);
    }

    public void unsubscribe() {
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }
}