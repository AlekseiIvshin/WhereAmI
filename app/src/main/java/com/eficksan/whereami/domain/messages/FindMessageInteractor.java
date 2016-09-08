package com.eficksan.whereami.domain.messages;

import com.eficksan.whereami.data.messages.MessagesRepository;
import com.eficksan.whereami.data.messages.PlacingMessage;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Provides methods for async loading messages.
 */
public class FindMessageInteractor {

    private static final String TAG = FindMessageInteractor.class.getSimpleName();

    private Subscription subscription;
    private final Scheduler jobScheduler;
    private final Scheduler uiScheduler;
    private final MessagesRepository messagesRepository;

    public FindMessageInteractor(MessagesRepository messagesRepository) {
        this.messagesRepository = messagesRepository;
        this.jobScheduler = Schedulers.computation();
        this.uiScheduler = AndroidSchedulers.mainThread();
    }

    public void execute(String messageId, final Subscriber<PlacingMessage> subscriber) {
        subscription = Observable.just(messageId)
                .subscribeOn(jobScheduler)
                .doOnNext(new Action1<String>() {
                    @Override
                    public void call(String messId) {
                        messagesRepository.findMessageById(messId, subscriber);
                    }
                })
                .observeOn(uiScheduler)
                .subscribe();
    }

    public void unsubscribe() {
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }


}
