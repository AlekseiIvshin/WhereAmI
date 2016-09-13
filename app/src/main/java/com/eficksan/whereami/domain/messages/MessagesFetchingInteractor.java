package com.eficksan.whereami.domain.messages;

import com.eficksan.whereami.data.messages.MessagesDataSource;
import com.eficksan.whereami.data.messages.PlacingMessage;
import com.eficksan.whereami.domain.BaseInteractor;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import rx.Observable;
import rx.Scheduler;

/**
 * Provides methods for async loading messages.
 */
public class MessagesFetchingInteractor extends BaseInteractor<LatLng, List<PlacingMessage>> {

    private final MessagesDataSource messagesDataSource;

    public MessagesFetchingInteractor(MessagesDataSource messagesDataSource, Scheduler jobScheduler, Scheduler uiScheduler) {
        super(jobScheduler,uiScheduler);
        this.messagesDataSource = messagesDataSource;
    }

    @Override
    protected Observable<List<PlacingMessage>> buildObservable(LatLng coordinates) {
        return messagesDataSource.fetchMessages(coordinates);
    }

}
