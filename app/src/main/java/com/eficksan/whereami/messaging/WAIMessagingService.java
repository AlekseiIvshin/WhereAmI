package com.eficksan.whereami.messaging;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.location.Location;

import com.eficksan.whereami.geo.Constants;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class WAIMessagingService extends IntentService {

    private static final String TAG = WAIMessagingService.class.getSimpleName();
    private static final String ACTION_PREFIX = "com.eficksan.whereami.messaging.action.";
    private static final String ACTION_CREATE_MESSAGE = ACTION_PREFIX+ "CREATE_MESSAGE";
    private static final String ACTION_FETCH_MESSAGES = ACTION_PREFIX+ "ACTION_FETCH_MESSAGES";

    public WAIMessagingService() {
        super(TAG);
    }

    public static void createMessage(Context context, LocationMessage locationMessage) {
        Intent intent = new Intent(context, WAIMessagingService.class);
        intent.setAction(ACTION_CREATE_MESSAGE);
        intent.putExtra(Constants.EXTRA_MESSAGE_DATA, locationMessage);
        context.startService(intent);
    }

    public static void fetchMessages(Context context, Location location) {
        Intent intent = new Intent(context, WAIMessagingService.class);
        intent.setAction(ACTION_FETCH_MESSAGES);
        intent.putExtra(Constants.EXTRA_LOCATION_DATA, location);
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
                }
                break;
                case ACTION_FETCH_MESSAGES: {
                    //TODO: Prototype: get messages from DB.
                    //TODO: App: request server for messages + cache (using Retrofit).

                    //TODO: May be need to use special Retrofit service for fetching messages
                }
                break;
            }
        }
    }
}
