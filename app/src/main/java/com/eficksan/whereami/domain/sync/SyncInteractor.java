package com.eficksan.whereami.domain.sync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import com.eficksan.whereami.domain.Interactor;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

/**
 * Created by Aleksei Ivshin
 * on 30.08.2016.
 */
public class SyncInteractor extends Interactor<Integer, Integer> {

    private final PublishSubject<Integer> mSyncResultChannel;

    private final Context mContext;

    private BroadcastReceiver mSyncResultReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int result = intent.getIntExtra(SyncConstants.SYNC_RESULT_CODE, SyncConstants.SYNC_UNKNOWN_ERROR);
            mSyncResultChannel.onNext(result);
        }
    };

    public SyncInteractor(Context context) {
        super(Schedulers.computation(), AndroidSchedulers.mainThread());
        this.mContext = context;
        mSyncResultChannel = PublishSubject.create();
    }

    @Override
    protected Observable<Integer> buildObservable(Integer parameter) {
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mSyncResultReceiver, new IntentFilter(SyncConstants.ACTION_SYNC_MESSAGE_BROADCAST));
        return mSyncResultChannel;
    }

    @Override
    public void unsubscribe() {
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mSyncResultReceiver);
        super.unsubscribe();
    }
}
