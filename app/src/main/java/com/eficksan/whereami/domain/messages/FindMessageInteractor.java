package com.eficksan.whereami.domain.messages;

import com.eficksan.whereami.data.messages.MessagesDataSource;
import com.eficksan.whereami.data.messages.PlacingMessage;
import com.eficksan.whereami.domain.BaseInteractor;

import rx.Observable;
import rx.Scheduler;

/**
 * Provides methods for async loading messages.
 */
public class FindMessageInteractor extends BaseInteractor<String, PlacingMessage> {

    private final MessagesDataSource messagesDataSource;

    public FindMessageInteractor(MessagesDataSource messagesDataSource, Scheduler jobScheduler, Scheduler uiScheduler) {
        super(jobScheduler, uiScheduler);
        this.messagesDataSource = messagesDataSource;
    }

    @Override
    protected Observable<PlacingMessage> buildObservable(String messageId) {
        return messagesDataSource.findMessageById(messageId);
    }
}
