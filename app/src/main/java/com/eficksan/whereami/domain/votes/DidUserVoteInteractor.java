package com.eficksan.whereami.domain.votes;

import com.eficksan.whereami.data.messages.MessagesRepository;
import com.eficksan.whereami.data.messages.PlacingMessage;
import com.eficksan.whereami.data.votes.VotesRepository;
import com.eficksan.whereami.domain.BaseInteractor;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Aleksei_Ivshin on 9/9/16.
 */
public class DidUserVoteInteractor {

    private Subscription subscription;
    private final Scheduler jobScheduler;
    private final Scheduler uiScheduler;
    private final VotesRepository votesRepository;

    public DidUserVoteInteractor(VotesRepository votesRepository) {
        this.votesRepository = votesRepository;
        this.jobScheduler = Schedulers.computation();
        this.uiScheduler = AndroidSchedulers.mainThread();
    }

    public void execute(String messageId, final Subscriber<Boolean> subscriber) {
        subscription = Observable.just(messageId)
                .subscribeOn(jobScheduler)
                .doOnNext(new Action1<String>() {
                    @Override
                    public void call(String messId) {
                        votesRepository.canVoteMessage(messId, subscriber);
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
