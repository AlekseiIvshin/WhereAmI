package com.eficksan.whereami.data.messages;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import rx.Subscriber;

/**
 * Messags repository implementation using {@link <a href="https://firebase.google.com">Firebase</a>} services.
 */
public class FirebaseDatabaseMessagesRepository implements MessagesRepository {

    private static final String DB_MESSAGES = "messages";

    private static final String TAG = FirebaseDatabaseMessagesRepository.class.getSimpleName();
    private final FirebaseDatabase mDatabase;
    private final FirebaseAuth mAuth;

    public FirebaseDatabaseMessagesRepository(FirebaseDatabase firebaseDatabase, FirebaseAuth firebaseAuth) {
        this.mDatabase = firebaseDatabase;
        this.mAuth = firebaseAuth;
    }


    @Override
    public boolean addMessage(PlacingMessage placingMessage) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Log.e(TAG, "Adding message: user was not logged in");
            return false;
        }
        DatabaseReference database = mDatabase.getReference();
        placingMessage.userId = currentUser.getUid();
        database.child(DB_MESSAGES).push().setValue(placingMessage);
        return true;
    }

    @Override
    public void fetchMessages(LatLng latLng, final Subscriber<List<PlacingMessage>> subscriber) {
        mDatabase.getReference().child(DB_MESSAGES).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.v(TAG, "Fetch messages: fetched list key = " + dataSnapshot.getKey());
                Iterable<DataSnapshot> messagesIterator = dataSnapshot.getChildren();
                List<PlacingMessage> placingMessages = new LinkedList<>();
                PlacingMessage placingMessage;
                DataSnapshot snapshot;
                for (DataSnapshot aMessagesIterator : messagesIterator) {
                    snapshot = aMessagesIterator;
                    placingMessage = snapshot.getValue(PlacingMessage.class);
                    placingMessage.messageId = snapshot.getKey();
                    placingMessages.add(placingMessage);
                }
                subscriber.onNext(placingMessages);
                //TODO: maybe return in stream?
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                subscriber.onError(databaseError.toException());
            }
        });
    }

    @Override
    public void findMessageById(final String messageId, final Subscriber<PlacingMessage> subscriber) {
        mDatabase.getReference().child(DB_MESSAGES).child(messageId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.v(TAG, "Found message with id = " + messageId);
                subscriber.onNext(dataSnapshot.getValue(PlacingMessage.class));
                subscriber.onCompleted();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                subscriber.onError(databaseError.toException());
            }
        });
    }
}
