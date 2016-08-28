package com.eficksan.whereami.presentation.messaging;

import android.content.Context;
import android.widget.Toast;

import javax.inject.Inject;

/**
 * Created by Aleksei Ivshin
 * on 24.08.2016.
 */
public class MessagingView {

    @Inject
    Context context;
    public final MessagingViewHolder viewHolder;

    public MessagingView(MessagingViewHolder viewHolder) {
        this.viewHolder = viewHolder;
    }

    public void showMessageError(int errorMessageRes) {
        viewHolder.messageInputLayout.setError(context.getString(errorMessageRes));
    }

    public void changeEnableSendMessage(boolean isEnabled) {
        viewHolder.sendMessage.setEnabled(isEnabled);
    }

    public void showError(int errorResId) {
        showMessage(errorResId);
    }

    public void showSuccess(int successResId) {
        showMessage(successResId);
    }

    private void showMessage(int messageResId) {
        Toast.makeText(context, messageResId, Toast.LENGTH_SHORT).show();
    }

    public void hideMessageError() {
        viewHolder.messageInputLayout.setError(null);
    }
}
