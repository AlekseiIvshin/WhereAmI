package com.eficksan.whereami.presentation.messaging;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eficksan.whereami.R;
import com.eficksan.whereami.presentation.IView;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.functions.Func1;
import rx.subjects.PublishSubject;

/**
 * Messaging view component.
 * Provides methods for managing UI.
 * <p/>
 * Created by Aleksei Ivshin
 * on 24.08.2016.
 */
public class PlacingMessageView implements IView {


    private final Context context;

    @Bind(R.id.input_layout_message)
    public TextInputLayout messageInputLayout;
    @Bind(R.id.input_message)
    public EditText messageInput;
    @Bind(R.id.create_message)
    public FloatingActionButton sendMessage;

    public PlacingMessageView(Context context) {
        this.context = context;
    }

    /**
     * Inject inflated view.
     *
     * @param view iew
     */
    public void takeView(View view) {
        ButterKnife.bind(this, view);

        messageInput.requestFocus();

        InputMethodManager keyboard = (InputMethodManager)
                context.getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.showSoftInput(messageInput, 0);
    }

    @Override
    public void releaseView() {
        InputMethodManager keyboard = (InputMethodManager)
                context.getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.hideSoftInputFromWindow(messageInput.getWindowToken(), 0);
    }

    /**
     * Disable or enable creating message.
     *
     * @param isEnabled flag
     */
    public void setEnableSendMessage(boolean isEnabled) {
        sendMessage.setEnabled(isEnabled);
    }

    /**
     * Shows error message.
     *
     * @param errorResId error resource
     */
    public void showError(int errorResId) {
        notifyUser(errorResId);
    }

    /**
     * Shows success message.
     *
     * @param successResId success resource
     */
    public void showSuccess(int successResId) {
        notifyUser(successResId);
    }

    /**
     * Show error on wrong message text.
     *
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

    public Observable<String> getMessageValuesChannel() {
        return RxTextView.textChanges(messageInput)
                .map(new Func1<CharSequence, String>() {
                    @Override
                    public String call(CharSequence messageValue) {
                        return messageValue.toString();
                    }
                });
    }

    public Observable<Void> getCreateMessageChannel() {
        return RxView.clicks(sendMessage);
    }

    /**
     * Shows message.
     *
     * @param messageResId message resource
     */
    private void notifyUser(int messageResId) {
        Toast.makeText(context, messageResId, Toast.LENGTH_SHORT).show();
    }
}
