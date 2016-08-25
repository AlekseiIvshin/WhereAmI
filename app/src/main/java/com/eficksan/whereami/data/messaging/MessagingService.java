package com.eficksan.whereami.data.messaging;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.os.ResultReceiver;

import com.eficksan.whereami.domain.messaging.LocationMessage;
import com.eficksan.whereami.domain.Constants;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MessagingService extends IntentService {

    private static final String TAG = MessagingService.class.getSimpleName();
    private static final String ACTION_PREFIX = "com.eficksan.whereami.messaging.action.";
    private static final String ACTION_CREATE_MESSAGE = ACTION_PREFIX + "CREATE_MESSAGE";

    public MessagingService() {
        super(TAG);
    }

    public static void createMessage(Context context, LocationMessage locationMessage, ResultReceiver resultReceiver) {
        Intent intent = new Intent(context, MessagingService.class);
        intent.setAction(ACTION_CREATE_MESSAGE);
        intent.putExtra(Constants.EXTRA_MESSAGE_DATA, locationMessage);
        intent.putExtra(Constants.EXTRA_RESULT_RECEIVER, resultReceiver);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            switch (action) {
                case ACTION_CREATE_MESSAGE: {
//                    final LocationMessage locationMessage = intent.getStringExtra(Constants.EXTRA_MESSAGE_DATA);
                    //TODO: Prototype: save messages in DB.
                    //TODO: App: add sending to server.

                    //TODO: user broadcast receiver for notify that message was created

                    ResultReceiver resultReceiver = intent.getParcelableExtra(Constants.EXTRA_RESULT_RECEIVER);
                    resultReceiver.send(Constants.SUCCESS_RESULT, Bundle.EMPTY);
                }
                break;
            }
        }
    }
}
