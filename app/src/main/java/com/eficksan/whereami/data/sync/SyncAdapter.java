package com.eficksan.whereami.data.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ComponentName;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SyncResult;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.eficksan.placingmessages.IPlaceMessageRepository;
import com.eficksan.placingmessages.PlaceMessage;
import com.eficksan.whereami.domain.messaging.MessagesContainer;
import com.eficksan.whereami.domain.sync.SyncConstants;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Aleksei Ivshin
 * on 29.08.2016.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String TAG = SyncAdapter.class.getSimpleName();

    Context mContext;

    @Inject
    MessagesContainer messagesContainer;

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

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContext = context;
        Intent remoteServiceIntent = new Intent("com.eficksan.messaging.BIND_PLACING_MESSAGE");
        remoteServiceIntent.setPackage("com.eficksan.messaging");
        mContext.bindService(remoteServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mContext = context;
        Intent remoteServiceIntent = new Intent("com.eficksan.messaging.BIND_PLACING_MESSAGE");
        remoteServiceIntent.setPackage("com.eficksan.messaging");
        mContext.bindService(remoteServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onPerformSync(Account account, Bundle bundle, String s, ContentProviderClient contentProviderClient, SyncResult syncResult) {
        Log.v(TAG, "OnPerformSync");

        if (mIsServiceConnected) {
            List<PlaceMessage> messagesByUser = null;
            try {
                messagesByUser = mPlacingMessages.getMessagesByUser(account.name);

            } catch (RemoteException e) {
                Log.e(TAG, e.getMessage(), e);
            }
            if (messagesByUser != null) {
                messagesContainer.setMesssages(messagesByUser);
                Intent intent = new Intent(SyncConstants.ACTION_SYNC_MESSAGE_BROADCAST);
                intent.putExtra(SyncConstants.SYNC_RESULT_CODE, SyncConstants.SYNC_SUCCESS);
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
            }
        }

    }

    @Override
    public void onSyncCanceled() {
        if (mIsServiceConnected) {
            mContext.unbindService(mServiceConnection);
        }
        super.onSyncCanceled();
    }
}
