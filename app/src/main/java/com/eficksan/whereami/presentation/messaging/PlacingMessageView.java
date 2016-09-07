package com.eficksan.whereami.presentation.messaging;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.eficksan.whereami.R;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Messaging view component.
 * Provides methods for managing UI.
 *
 * Created by Aleksei Ivshin
 * on 24.08.2016.
 */
public class PlacingMessageView {

    @Inject
    Context context;

    @Bind(R.id.input_layout_message)
    public TextInputLayout messageInputLayout;
    @Bind(R.id.input_message)
    public EditText messageInput;
    @Bind(R.id.create_message)
    public FloatingActionButton sendMessage;

    /**
     * Inject inflated view.
     * @param view iew
     */
    public void takeView(View view) {
        ButterKnife.bind(this, view);
    }

    /**
     * Disable or enable creating message.
     * @param isEnabled flag
     */
    public void setEnableSendMessage(boolean isEnabled) {
        sendMessage.setEnabled(isEnabled);
    }

    /**
     * Shows error message.
     * @param errorResId error resource
     */
    public void showError(int errorResId) {
        notifyUser(errorResId);
    }

    /**
     * Shows success message.
     * @param successResId success resource
     */
    public void showSuccess(int successResId) {
        notifyUser(successResId);
    }

    /**
     * Show error on wrong message text.
     * @param errorResId error resource id
     */
    public void showMessageValidationError(int errorResId) {
        messageInputLayout.setError(context.getString(errorResId));
    }

    /**
     * Hides error about wrong message.
     */
    public void hideMessageValidationError() {
        messageInputLayout.setError(null);
    }

    /**
     * Shows message.
     * @param messageResId message resource
     */
    private void notifyUser(int messageResId) {
        Toast.makeText(context, messageResId, Toast.LENGTH_SHORT).show();
    }
}
