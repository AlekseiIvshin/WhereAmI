package com.eficksan.whereami.domain.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by Aleksei Ivshin
 * on 29.08.2016.
 */
public class SyncDelegate {


    // Constants
    // Content provider authority
    public static final String AUTHORITY = "com.eficksan.messaging.datasync.provider";
    // Account
    public static final String ACCOUNT = "default_account";
    // Sync interval constants
    public static final long SECONDS_PER_MINUTE = 60L;
    public static final long SYNC_INTERVAL_IN_MINUTES = 60L;
    public static final long SYNC_INTERVAL =
            SYNC_INTERVAL_IN_MINUTES *
                    SECONDS_PER_MINUTE;
    // Global variables
    // A content resolver for accessing the provider
    ContentResolver mResolver;

    public void init(Activity activity) {
        // Get the content resolver for your app
        mResolver = activity.getContentResolver();
        /*
         * Turn on periodic syncing
         */
        AccountManager accountManager = (AccountManager) activity.getSystemService(Context.ACCOUNT_SERVICE);

        Account account = new Account(ACCOUNT, "com.eficksan.messaging.datasync");
        accountManager.addAccountExplicitly(account, null, null);
        // Pass the settings flags by inserting them in a bundle
        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        /*
         * Request the sync for the default account, authority, and
         * manual sync settings
         */
        ContentResolver.setIsSyncable(account, AUTHORITY, 1);
        ContentResolver.setSyncAutomatically(account, AUTHORITY, true);
        ContentResolver.requestSync(account, AUTHORITY, settingsBundle);
    }
}
