package com.eficksan.whereami.domain.messaging;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.os.ResultReceiver;
import android.util.Log;

import com.eficksan.whereami.data.messaging.MessagingService;
import com.eficksan.whereami.domain.BaseInteractor;
import com.eficksan.whereami.domain.Constants;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

/**
 * Messaging interactor component.
 * Contains domain logic.
 * <p/>
 * Created by Aleksei Ivshin
 * on 24.08.2016.
 */
public class PlaceMessageInteractor extends BaseInteractor<LocationMessage, Integer> {
    private static final String TAG = PlaceMessageInteractor.class.getSimpleName();

    private PublishSubject<Integer> mMessageResultChannel;

    private Activity mActivity;

    public PlaceMessageInteractor(Activity activityContext) {
        super(Schedulers.computation(), AndroidSchedulers.mainThread());
        this.mActivity = activityContext;
    }

    @Override
    public void execute(LocationMessage parameter, Subscriber<Integer> subscriber) {
        super.execute(parameter, subscriber);
        Log.v(TAG, String.format("Message is sending: %f x %f, msg = '%s'", parameter.latitude, parameter.longitude, parameter.message));
        ResultReceiver messagingResultReceiver = new ResultReceiver(mActivity.getWindow().getDecorView().getHandler()) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                if (resultCode == Constants.SUCCESS_RESULT) {
                    Log.v(TAG, "Message was delivered");
                    mMessageResultChannel.onCompleted();
                } else {
                    Log.v(TAG, "Message was not delivered: resultCode = " + resultCode);
                    mMessageResultChannel.onError(new MessageDeliverException());
                }
            }
        };
        MessagingService.createMessage(mActivity, parameter, messagingResultReceiver);
    }

    @Override
    protected Observable<Integer> buildObservable(LocationMessage parameter) {
        mMessageResultChannel = PublishSubject.create();
        return mMessageResultChannel;
    }

}
