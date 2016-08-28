package com.eficksan.whereami.presentation.messaging;

import android.location.Location;
import android.os.Bundle;

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
 * Created by Aleksei Ivshin
 * on 24.08.2016.
 */
public class MessagingPresenter {

    @Inject
    Router router;
    @Inject
    MessagingInteractor messagingInteractor;

    private MessagingView messagingView;
    private Location currentLocation;

    public void onStart(Location currentLocation) {
        this.currentLocation = currentLocation;
        setListeners();
    }

    public void onStop() {
        messagingInteractor.unsubscribe();
    }

    private void setListeners() {
        RxTextView.textChanges(messagingView.viewHolder.messageInput)
                .subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {
                        int messageLength = charSequence.length();
                        if (0 < messageLength && messageLength < Constants.MAX_MESSAGE_SIZE) {
                            messagingView.changeEnableSendMessage(true);
                        } else {
                            messagingView.changeEnableSendMessage(false);
                        }

                        if (messageLength == 0) {
                            messagingView.showMessageError(R.string.error_message_empty);
                        } else if (messageLength >= Constants.MAX_MESSAGE_SIZE) {
                            messagingView.showMessageError(R.string.error_message_too_long);
                        } else {
                            messagingView.hideMessageError();
                        }
                    }
                });

        RxView.clicks(messagingView.viewHolder.sendMessage)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (currentLocation == null) {
                            messagingView.showError(R.string.error_location_required);
                            return;
                        }
                        final String message = MessagingPresenter.this.messagingView.viewHolder.messageInput.getText().toString();
                        messagingInteractor.execute(new LocationMessage(currentLocation, message), new Subscriber<Integer>() {
                            @Override
                            public void onCompleted() {
                                messagingView.showSuccess(R.string.success_message_was_delivered);
                                router.showScreen(Screens.LOCATION_SCREEN, Bundle.EMPTY);
                            }

                            @Override
                            public void onError(Throwable e) {
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

    public void setView(MessagingView view) {
        this.messagingView = view;
    }
}
