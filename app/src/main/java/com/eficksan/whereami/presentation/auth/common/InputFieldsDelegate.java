package com.eficksan.whereami.presentation.auth.common;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.design.widget.TextInputLayout;

/**
 * Created by Aleksei_Ivshin on 9/6/16.
 */
public class InputFieldsDelegate {

    public static void showValidatoinError(TextInputLayout inputLayout, @StringRes int resId, Context context) {
        inputLayout.setError(context.getString(resId));
    }

    public static void hideValidatoinError(TextInputLayout inputLayout) {
        inputLayout.setError(null);
    }
}
