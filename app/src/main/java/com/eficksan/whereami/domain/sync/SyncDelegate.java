package com.eficksan.whereami.domain.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Aleksei Ivshin
 * on 29.08.2016.
 */
public class SyncDelegate {
    // Constants
    private static final String TAG = SyncDelegate.class.getSimpleName();
    // Content provider authority
    public static final String AUTHORITY = "com.eficksan.messaging.datasync.provider";
    public static final String ACCOUNT_TYPE = "com.eficksan.messaging.datasync";
    // Account
    public static final String ACCOUNT = "default_account";
    // Sync interval constants
    public static final long SECONDS_PER_MINUTE = 60L;
    public static final long SYNC_INTERVAL_IN_MINUTES = 1L;
    public static final long SYNC_INTERVAL =
            SYNC_INTERVAL_IN_MINUTES *
                    SECONDS_PER_MINUTE;

    public static void startSync(Context context) {
        Account account = getAccount(context);
//        ContentResolver.setIsSyncable(account, AUTHORITY, 1);
        ContentResolver.setSyncAutomatically(account, AUTHORITY, true);
        ContentResolver.addPeriodicSync(account, AUTHORITY, Bundle.EMPTY, SYNC_INTERVAL);
        Log.v(TAG, String.format("Start periodic sync every %d", SYNC_INTERVAL));
    }

    public static void stopSync(Context context) {
        Account account = getAccount(context);
        ContentResolver.removePeriodicSync(account, AUTHORITY, Bundle.EMPTY);
        Log.v(TAG, "Stop periodic sync");
    }

    public static Account getAccount(Context context) {
        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        Account account = new Account(ACCOUNT, ACCOUNT_TYPE);
        accountManager.addAccountExplicitly(account, null, null);
        return account;
    }
}
