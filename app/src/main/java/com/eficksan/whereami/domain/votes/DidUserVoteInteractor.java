package com.eficksan.whereami.domain.votes;

import com.eficksan.whereami.data.votes.FirebaseDatabaseVotesRepository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.subjects.PublishSubject;

/**
 * Interactor for checking user available to vote message.
 */
public class DidUserVoteInteractor {

    private Subscription subscription;
    private final FirebaseDatabaseVotesRepository votesRepository;

    public DidUserVoteInteractor(FirebaseDatabaseVotesRepository votesRepository) {
        this.votesRepository = votesRepository;
    }

    public void execute(final String messageId, final Subscriber<Boolean> subscriber) {
        final PublishSubject<Boolean> canUserVoteChannel = PublishSubject.create();
        final ValueEventListener mValueListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean isVotedBefore = dataSnapshot.getValue(Boolean.class);
                canUserVoteChannel.onNext(isVotedBefore == null);
                canUserVoteChannel.onCompleted();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                canUserVoteChannel.onError(databaseError.toException());
            }
        };
        canUserVoteChannel.doOnSubscribe(new Action0() {
            @Override
            public void call() {
                votesRepository.canVoteMessage(messageId, mValueListener);
            }
        });

        subscription = canUserVoteChannel.subscribe(subscriber);
    }

    public void unsubscribe() {
        subscription.unsubscribe();
    }
}
