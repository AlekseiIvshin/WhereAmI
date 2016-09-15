package com.eficksan.whereami.domain;

import rx.Subscriber;

/**
 * Created by Aleksei Ivshin
 * on 22.08.2016.
 */
public interface Interactor<ParameterType, ResultType> {

    void execute(ParameterType parameter, Subscriber<ResultType> subscriber);

    void unsubscribe();
}