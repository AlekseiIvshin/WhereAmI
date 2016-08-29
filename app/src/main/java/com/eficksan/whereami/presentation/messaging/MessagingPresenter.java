package com.eficksan.whereami.presentation.messaging;

import android.location.Location;

import com.eficksan.whereami.R;
import com.eficksan.whereami.domain.Constants;
import com.eficksan.whereami.domain.messaging.LocationMessage;
import com.eficksan.whereami.domain.messaging.MessageDeliverException;
import com.eficksan.whereami.domain.messaging.MessagingInteractor;
import com.eficksan.whereami.presentation.routing.Router;
import com.eficksan.whereami.presentation.routing.Screens;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;

import javax.inject.Inject;

import rx.Subscriber;
import rx.functions.Action1;

/**
 * Messaging presenter component.
 * Provides logic from Interactors to View part.
 *
 * Created by Aleksei Ivshin
 * on 24.08.2016.
 */
public class MessagingPresenter {

    @Inject
    Router router;

    @Inject
    MessagingInteractor messagingInteractor;

    private MessagingView messagingView;

    private Location mMessageLocation;

    /**
     * Sets view to presenter.
     * @param view view
     */
    public void setView(MessagingView view) {
        this.messagingView = view;
    }

    /**
     * On start presentation.
     * @param messageLocation current location, message will be created using this location
     */
    public void onStart(Location messageLocation) {
        this.mMessageLocation = messageLocation;
        setListeners();
    }

    /**
     * On stop presentation.
     */
    public void onStop() {
        messagingInteractor.unsubscribe();
    }

    /**
     * Validate message.
     * @param message entered message
     * @return if there is wrong message - error resource id, otherwise - 0
     */
    public int validateMessage(String message) {
        int messageLength = message.length();
        if (messageLength == 0) {
            return R.string.error_message_empty;
        } else if (messageLength >= Constants.MAX_MESSAGE_SIZE) {
            return R.string.error_message_too_long;
        }
        return 0;
    }

    /**
     * Set event listeners to View.
     */
    private void setListeners() {
        // Subscribe to message text changes
        RxTextView.textChanges(messagingView.viewHolder.messageInput)
                .subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence messageText) {
                        int errorResourceId = validateMessage(messageText.toString());

                        if (errorResourceId == 0) {
                            messagingView.hideMessageError();
                            messagingView.changeEnableSendMessage(true);
                        }else {
                            messagingView.showMessageError(errorResourceId);
                            messagingView.changeEnableSendMessage(false);
                        }
                    }
                });

        // Subscribe to sending messages
        RxView.clicks(messagingView.viewHolder.sendMessage)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        // For creating message need location
                        if (mMessageLocation == null) {
                            messagingView.showError(R.string.error_location_required);
                            return;
                        }
                        final String message = MessagingPresenter.this.messagingView.viewHolder.messageInput.getText().toString();
                        messagingInteractor.execute(new LocationMessage(mMessageLocation, message), new Subscriber<Integer>() {
                            @Override
                            public void onCompleted() {
                                // Message was sent. Return to previous screen.
                                messagingView.showSuccess(R.string.success_message_was_delivered);
                                router.closeScreen(Screens.MESSAGING_SCREEN);
                            }

                            @Override
                            public void onError(Throwable e) {
                                // Message was not sent. Show error and wait for next try.
                                if (e instanceof MessageDeliverException) {
                                    messagingView.showError(R.string.error_message_deliver);
                                } else {
                                    messagingView.showError(R.string.error_unknown);
                                }
                            }

                            @Override
                            public void onNext(Integer integer) {

                            }
                        });
                    }
                });
    }
}
