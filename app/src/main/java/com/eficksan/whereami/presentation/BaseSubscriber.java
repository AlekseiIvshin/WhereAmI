package com.eficksan.whereami.presentation;

import android.util.Log;

import com.eficksan.whereami.domain.Interactor;

import rx.Subscriber;

/**
 * Created by Aleksei_Ivshin on 9/16/16.
 */
public abstract class BaseSubscriber<INTERACTOR_TYPE extends Interactor, RESULT_TYPE> extends Subscriber<RESULT_TYPE> {

    private static final String TAG = BaseSubscriber.class.getSimpleName();
    public final INTERACTOR_TYPE interactor;

    public BaseSubscriber(INTERACTOR_TYPE interactor) {
        this.interactor = interactor;
    }

    @Override
    public void onCompleted() {
        interactor.unsubscribe();
    }

    @Override
    public void onError(Throwable e) {
        Log.e(TAG, e.getMessage(), e);
        interactor.unsubscribe();
    }
}
