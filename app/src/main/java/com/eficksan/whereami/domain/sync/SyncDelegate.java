package com.eficksan.whereami.domain.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
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
//        ContentResolver.addPeriodicSync(account, AUTHORITY, Bundle.EMPTY, SYNC_INTERVAL);

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

    public static void stopSync(Context context) {
        Account account = getAccount(context);
        ContentResolver.removePeriodicSync(account, AUTHORITY, Bundle.EMPTY);
    }

    public static Account getAccount(Context context) {
        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        Account account = new Account(ACCOUNT, ACCOUNT_TYPE);
        accountManager.addAccountExplicitly(account, null, null);
        return account;
    }
}
