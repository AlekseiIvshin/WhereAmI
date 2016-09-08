package com.eficksan.whereami.domain.users;

import com.eficksan.whereami.data.auth.User;
import com.eficksan.whereami.data.auth.UsersRepository;
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
public class FindUserInteractor {

    private Subscription subscription;
    private final Scheduler jobScheduler;
    private final Scheduler uiScheduler;
    private final UsersRepository usersRepository;

    public FindUserInteractor( UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
        this.jobScheduler = Schedulers.computation();
        this.uiScheduler = AndroidSchedulers.mainThread();
    }

    public void execute(String userId, final Subscriber<User> subscriber) {
        subscription = Observable.just(userId)
                .subscribeOn(jobScheduler)
                .doOnNext(new Action1<String>() {
                    @Override
                    public void call(String messId) {
                        usersRepository.findUserById(messId, subscriber);
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
