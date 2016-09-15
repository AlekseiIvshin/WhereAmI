package com.eficksan.whereami.data.votes;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import rx.Observable;
import rx.Subscriber;

/**
 * Provides methods for request votes data.
 */
public class VotesDataSource {

    private static final String DB_VOTES = "votes";

    private final FirebaseDatabase mDatabase;

    public VotesDataSource(FirebaseDatabase mDatabase) {
        this.mDatabase = mDatabase;
    }

    /**
     * Fetches user message vote.
     *
     * @param messageId message id
     * @param userId    user id
     * @return vote channel
     */
    public Observable<Boolean> fetchUserMessageVote(final String messageId, @NonNull final String userId) {

        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(final Subscriber<? super Boolean> subscriber) {
                mDatabase.getReference().child(DB_VOTES).child(messageId).child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Boolean isVotedBefore = dataSnapshot.getValue(Boolean.class);
                        subscriber.onNext(isVotedBefore);
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

    /**
     * Votes for message.
     *
     * @param userId     current user id
     * @param messageId  message id
     * @param isVotedFor is vote for or against
     * @return true if operation executed or persisted
     */
    public Observable<Boolean> voteMessage(@NonNull final String userId, @NonNull final String messageId, final boolean isVotedFor) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(final Subscriber<? super Boolean> subscriber) {
                DatabaseReference database = mDatabase.getReference();
                database.child(DB_VOTES).child(messageId).child(userId).setValue(isVotedFor);
                subscriber.onNext(true);
                subscriber.onCompleted();
            }
        });
    }

    /**
     * Fetches message votes count.
     *
     * @param messageId message id
     * @return votes channel
     */
    public Observable<MessageVotes> fetchMessageVotes(@NonNull final String messageId) {
        return Observable.create(new Observable.OnSubscribe<MessageVotes>() {
            @Override
            public void call(final Subscriber<? super MessageVotes> subscriber) {
                mDatabase.getReference().child(DB_VOTES).child(messageId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        MessageVotes messageVotes;
                        if (dataSnapshot.getChildrenCount() == 0) {
                            messageVotes = new MessageVotes(0, 0);
                        } else {
                            Iterable<DataSnapshot> votesIterable = dataSnapshot.getChildren();
                            int votesFor = 0;
                            int votesAgainst = 0;

                            DataSnapshot voteSnapshot;
                            for (DataSnapshot aVoteIterator : votesIterable) {
                                voteSnapshot = aVoteIterator;
                                if (voteSnapshot.getValue(Boolean.class)) {
                                    votesFor++;
                                } else {
                                    votesAgainst++;
                                }
                            }
                            messageVotes = new MessageVotes(votesFor, votesAgainst);
                        }
                        subscriber.onNext(messageVotes);
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
