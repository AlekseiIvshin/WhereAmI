package com.eficksan.whereami.domain.messaging;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.os.ResultReceiver;
import android.util.Log;

import com.eficksan.placingmessages.IPlaceMessageRepository;
import com.eficksan.placingmessages.PlaceMessage;
import com.eficksan.whereami.data.messaging.MessagingService;
import com.eficksan.whereami.domain.Constants;
import com.eficksan.whereami.domain.Interactor;
import com.eficksan.whereami.domain.sync.SyncDelegate;

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
public class MessagingInteractor extends Interactor<LocationMessage, Integer> {

    private static final String TAG = MessagingInteractor.class.getSimpleName();

    private PublishSubject<Integer> mMessageResultChannel;

    private Activity mActivity;

    private IPlaceMessageRepository mPlacingMessages;
    private boolean mIsServiceConnected = false;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mPlacingMessages = IPlaceMessageRepository.Stub.asInterface(iBinder);
            mIsServiceConnected = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mIsServiceConnected = false;
            mPlacingMessages = null;
        }
    };

    public MessagingInteractor(Activity activityContext) {
        super(Schedulers.computation(), AndroidSchedulers.mainThread());
        this.mActivity = activityContext;

        bindRemoteService();
    }

    @Override
    public void execute(LocationMessage parameter, Subscriber<Integer> subscriber) {
        super.execute(parameter, subscriber);
    }

    @Override
    public void unsubscribe() {
        unbindRemoteService();
        super.unsubscribe();
    }

    @Override
    protected Observable<Integer> buildObservable(LocationMessage parameter) {
        mMessageResultChannel = PublishSubject.create();
        ResultReceiver messagingResultReceiver = new ResultReceiver(mActivity.getWindow().getDecorView().getHandler()) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                if (resultCode == Constants.SUCCESS_RESULT) {
                    mMessageResultChannel.onCompleted();
                } else {
                    mMessageResultChannel.onError(new MessageDeliverException());
                }
            }
        };
        MessagingService.createMessage(mActivity, parameter, messagingResultReceiver);
        if (mIsServiceConnected) {
            try {
                PlaceMessage placeMessage = mPlacingMessages.addMessage(parameter.latitude, parameter.longitude, parameter.message, SyncDelegate.getAccount(mActivity).name);
                Log.v(TAG, String.format("Message saved: id=%s", placeMessage.id));
            } catch (RemoteException e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
        return mMessageResultChannel;
    }

    private void bindRemoteService() {
        Intent remoteServiceIntent = new Intent("com.eficksan.messaging.BIND_PLACING_MESSAGE");
        remoteServiceIntent.setPackage("com.eficksan.messaging");
//            remoteServiceIntent.setComponent(new ComponentName("com.eficksan.messaging", "com.eficksan.messaging.data.StubMessagingService"));
        mActivity.bindService(remoteServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private void unbindRemoteService() {
        if (mIsServiceConnected) {
            mActivity.unbindService(mServiceConnection);
        }
    }
}
