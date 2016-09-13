package com.eficksan.whereami.domain.messages;

import com.eficksan.whereami.data.messages.MessagesDataSource;
import com.eficksan.whereami.data.messages.PlacingMessage;
import com.eficksan.whereami.domain.BaseInteractor;

import rx.Observable;
import rx.Scheduler;

/**
 * Provides method for sending message to server.
 */
public class PlacingMessageInteractor extends BaseInteractor<PlacingMessage, Boolean> {

    private final MessagesDataSource messagesDataSource;

    public PlacingMessageInteractor(MessagesDataSource messagesDataSource, Scheduler jobScheduler, Scheduler uiScheduler) {
        super(jobScheduler, uiScheduler);
        this.messagesDataSource = messagesDataSource;
    }

    @Override
    protected Observable<Boolean> buildObservable(final PlacingMessage message) {
        return messagesDataSource.addMessage(message);
    }
}
