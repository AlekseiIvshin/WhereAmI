package com.eficksan.whereami.domain.messages;

import com.eficksan.whereami.data.messages.MessagesDataSource;
import com.eficksan.whereami.data.messages.PlacingMessage;
import com.eficksan.whereami.domain.BaseInteractor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import rx.Observable;
import rx.Scheduler;

/**
 * Provides method for sending message to server.
 */
public class PlacingMessageInteractor extends BaseInteractor<PlacingMessage, Boolean> {

    private final MessagesDataSource messagesDataSource;
    private final FirebaseAuth firebaseAuth;

    public PlacingMessageInteractor(MessagesDataSource messagesDataSource, FirebaseAuth firebaseAuth, Scheduler uiScheduler, Scheduler jobScheduler) {
        super(jobScheduler, uiScheduler);
        this.messagesDataSource = messagesDataSource;
        this.firebaseAuth = firebaseAuth;
    }

    @Override
    protected Observable<Boolean> buildObservable(final PlacingMessage message) {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser==null) {
            return Observable.error(new IllegalArgumentException("There is not authenticated user"));
        }
        return messagesDataSource.addMessage(message, currentUser.getUid());
    }
}
