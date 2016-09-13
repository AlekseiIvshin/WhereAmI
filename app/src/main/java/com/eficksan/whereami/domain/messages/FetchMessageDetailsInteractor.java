package com.eficksan.whereami.domain.messages;

import com.eficksan.whereami.data.messages.MessageDetails;
import com.eficksan.whereami.data.messages.MessagesDataSource;
import com.eficksan.whereami.data.messages.PlacingMessage;
import com.eficksan.whereami.domain.BaseInteractor;

import rx.Observable;
import rx.Scheduler;

/**
 * Provides methods for async loading messages.
 */
public class FetchMessageDetailsInteractor extends BaseInteractor<String, MessageDetails> {

    private final MessagesDataSource messagesDataSource;

    public FetchMessageDetailsInteractor(MessagesDataSource messagesDataSource, Scheduler jobScheduler, Scheduler uiScheduler) {
        super(jobScheduler,uiScheduler);
        this.messagesDataSource = messagesDataSource;
    }

    @Override
    protected Observable<MessageDetails> buildObservable(String messageId) {
        return null;
    }
}
