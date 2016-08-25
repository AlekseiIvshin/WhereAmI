package com.eficksan.whereami.domain.messaging;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.os.ResultReceiver;

import com.eficksan.whereami.data.messaging.MessagingService;
import com.eficksan.whereami.domain.Constants;
import com.eficksan.whereami.domain.Interactor;

import java.lang.ref.WeakReference;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

/**
 * Created by Aleksei Ivshin
 * on 24.08.2016.
 */
public class MessagingInteractor extends Interactor<LocationMessage, Integer> {

    private PublishSubject<Integer> mMessageResultChannel;
    private final WeakReference<Activity> mRefActivityContext;
    private ResultReceiver mResultReceiver;

    public MessagingInteractor(Activity activityContext) {
        super(Schedulers.computation(), AndroidSchedulers.mainThread());
        mMessageResultChannel = PublishSubject.create();
        this.mRefActivityContext = new WeakReference<>(activityContext);
        mResultReceiver = new ResultReceiver(activityContext.getWindow().getDecorView().getHandler()) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                if (resultCode == Constants.SUCCESS_RESULT) {
                    mMessageResultChannel.onCompleted();
                } else {
                    mMessageResultChannel.onError(new MessageDeliverException());
                }
            }
        };
    }

    @Override
    protected Observable<Integer> buildObservable(LocationMessage parameter) {
        Activity activity = mRefActivityContext.get();
        if (activity != null) {
            MessagingService.createMessage(activity, parameter, mResultReceiver);
        }
        return mMessageResultChannel;
    }
}
