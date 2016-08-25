package com.eficksan.whereami.presentation.messaging;

import android.content.Context;
import android.widget.Toast;

import java.lang.ref.WeakReference;

/**
 * Created by Aleksei Ivshin
 * on 24.08.2016.
 */
public class MessagingView {

    public final WeakReference<Context> refContext;
    public final MessagingViewHolder viewHolder;

    public MessagingView(Context refContext, MessagingViewHolder viewHolder) {
        this.refContext = new WeakReference<>(refContext);
        this.viewHolder = viewHolder;
    }

    public void showMessageError(int errorMessageRes) {
        Context context = refContext.get();
        if (context != null) {
            viewHolder.messageInputLayout.setError(context.getString(errorMessageRes));
        }
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
        Context context = refContext.get();
        if (context != null) {
            Toast.makeText(context,messageResId, Toast.LENGTH_SHORT).show();
        }
    }

    public void hideMessageError() {
        viewHolder.messageInputLayout.setError(null);
    }
}
