package com.eficksan.whereami.domain.messaging;

import com.eficksan.whereami.data.messaging.FirebaseDatabaseMessagesRepository;
import com.eficksan.whereami.data.messaging.MessagesRepository;
import com.eficksan.whereami.data.messaging.PlacingMessage;
import com.eficksan.whereami.domain.BaseInteractor;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import java.util.concurrent.ExecutionException;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Provides method for sending message to server.
 */
public class PlacingMessageInteractor extends BaseInteractor<PlacingMessage, Boolean> {

    private final MessagesRepository mMessagesRepository;

    public PlacingMessageInteractor(MessagesRepository messagesRepository) {
        super(Schedulers.computation(), AndroidSchedulers.mainThread());
        this.mMessagesRepository = messagesRepository;
    }

    @Override
    protected Observable<Boolean> buildObservable(final PlacingMessage parameter) {
        return Observable.just(parameter)
                .subscribeOn(jobScheduler)
                .map(new Func1<PlacingMessage, Boolean>() {
                    @Override
                    public Boolean call(PlacingMessage locationMessage) {
                        return mMessagesRepository.addMessage(parameter);
                    }
                });
    }
}
