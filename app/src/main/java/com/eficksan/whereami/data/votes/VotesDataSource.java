package com.eficksan.whereami.data.votes;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Aleksei_Ivshin on 9/12/16.
 */
public class VotesDataSource {

    private static final String DB_VOTES = "votes";

    private final FirebaseDatabase mDatabase;

    public VotesDataSource(FirebaseDatabase mDatabase) {
        this.mDatabase = mDatabase;
    }

    public Observable<Boolean> fetchUserMessageVote(final String messageId, final String userId) {

        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(final Subscriber<? super Boolean> subscriber) {
                mDatabase.getReference().child(DB_VOTES).child(messageId).child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Boolean isVotedBefore = dataSnapshot.getValue(Boolean.class);
                        subscriber.onNext(isVotedBefore == null || !isVotedBefore);
                        subscriber.onCompleted();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        subscriber.onError(databaseError.toException());
                    }
                });
            }
        });

    }
}
