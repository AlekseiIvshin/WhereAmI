package com.eficksan.whereami.data.votes;

import android.util.Log;

import com.eficksan.whereami.data.messages.PlacingMessage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.List;

import rx.Subscriber;

/**
 * Messags repository implementation using {@link <a href="https://firebase.google.com">Firebase</a>} services.
 */
public class FirebaseDatabaseVotesRepository implements VotesRepository {

    private static final String DB_VOTES = "votes";

    private static final String TAG = FirebaseDatabaseVotesRepository.class.getSimpleName();
    private final FirebaseDatabase mDatabase;
    private final FirebaseAuth mAuth;

    public FirebaseDatabaseVotesRepository(FirebaseDatabase firebaseDatabase, FirebaseAuth firebaseAuth) {
        this.mDatabase = firebaseDatabase;
        this.mAuth = firebaseAuth;
    }

    @Override
    public void canVoteMessage(String messageId, final Subscriber<Boolean> subscriber) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            subscriber.onError(new IllegalAccessError("User is not authenticated"));
            return;
        }
        mDatabase.getReference().child(DB_VOTES).child(messageId).child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
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

    @Override
    public boolean voteMessage(String messageId, boolean isVotedFor) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Log.e(TAG, "Adding message: user was not logged in");
            return false;
        }
        DatabaseReference database = mDatabase.getReference();
        database.child(DB_VOTES).child(messageId).child(currentUser.getUid()).setValue(isVotedFor);
        return true;
    }

    @Override
    public void getVotesCount(String messageId, final Subscriber<MessageVotes> subscriber) {
        mDatabase.getReference().child(DB_VOTES).child(messageId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0) {
                    subscriber.onNext(new MessageVotes(0, 0));
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
                    subscriber.onNext(new MessageVotes(votesFor, votesAgainst));
                }

                subscriber.onCompleted();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                subscriber.onError(databaseError.toException());
            }
        });
    }
}
