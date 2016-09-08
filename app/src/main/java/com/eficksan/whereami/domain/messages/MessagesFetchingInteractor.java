package com.eficksan.whereami.domain.messages;

import android.location.Location;
import android.util.Log;

import com.eficksan.whereami.data.messages.MessagesRepository;
import com.eficksan.whereami.data.messages.PlacingMessage;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

/**
 * Provides methods for async loading messages.
 */
public class MessagesFetchingInteractor {

    private static final String TAG = MessagesFetchingInteractor.class.getSimpleName();

    private Subscription subscription;
    private final Scheduler jobScheduler;
    private final Scheduler uiScheduler;
    private PublishSubject<Location> locationChannel;

    private final MessagesRepository messagesRepository;

    public MessagesFetchingInteractor(MessagesRepository messagesRepository) {
        this.messagesRepository = messagesRepository;
        this.jobScheduler = Schedulers.computation();
        this.uiScheduler = AndroidSchedulers.mainThread();
    }

    public void subscribe(final Subscriber<List<PlacingMessage>> subscriber) {
        locationChannel = PublishSubject.create();
        subscription = locationChannel
                .subscribeOn(jobScheduler)
                .doOnNext(new Action1<Location>() {
                    @Override
                    public void call(Location location) {
                        Log.v(TAG, "Before fetching messages for "+location.toString() );
                        messagesRepository.fetchMessages(new LatLng(location.getLatitude(), location.getLongitude()), subscriber);
                    }
                })
                .observeOn(uiScheduler)
                .subscribe();
    }

    public void execute(Location location) {
        locationChannel.onNext(location);
    }

    public void unsubscribe() {
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }

}
