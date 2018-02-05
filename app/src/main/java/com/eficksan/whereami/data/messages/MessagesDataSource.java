package com.eficksan.whereami.data.messages;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Provides methods for request votes data.
 */
public class MessagesDataSource {

    private static final String DB_MESSAGES = "messages";

    private final FirebaseDatabase mDatabase;

    public MessagesDataSource(FirebaseDatabase mDatabase) {
        this.mDatabase = mDatabase;
    }

    /**
     * Add message for location.
     *
     * @param placingMessage messgae
     * @return true - message adding is applied, false - otherwise
     */
    public Observable<Boolean> addMessage(final PlacingMessage placingMessage, final String userId) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(final Subscriber<? super Boolean> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    DatabaseReference database = mDatabase.getReference();
                    database.child(DB_MESSAGES).push().setValue(new PlacingMessage(placingMessage, userId));
                    subscriber.onNext(true);
                    subscriber.onCompleted();
                }
            }
        });
    }

    /**
     * Fetching messages for location.
     *
     * @param latLng coordinates
     */
    public Observable<List<PlacingMessage>> fetchMessages(LatLng latLng) {
        return Observable.create(new Observable.OnSubscribe<List<PlacingMessage>>() {
            @Override
            public void call(final Subscriber<? super List<PlacingMessage>> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    mDatabase.getReference()
                            .child(DB_MESSAGES)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (!subscriber.isUnsubscribed()) {
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
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    if (!subscriber.isUnsubscribed()) {
                                        subscriber.onError(databaseError.toException());
                                    }
                                }
                            });
                }
            }
        });
    }

    /**
     * Find message by id.
     *
     * @param messageId message id
     */
    public Observable<PlacingMessage> findMessageById(final String messageId) {
        return Observable.create(new Observable.OnSubscribe<PlacingMessage>() {
            @Override
            public void call(final Subscriber<? super PlacingMessage> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    mDatabase.getReference()
                            .child(DB_MESSAGES)
                            .child(messageId)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (!subscriber.isUnsubscribed()) {
                                        subscriber.onNext(dataSnapshot.getValue(PlacingMessage.class));
                                        subscriber.onCompleted();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    if (!subscriber.isUnsubscribed()) {
                                        subscriber.onError(databaseError.toException());
                                    }
                                }
                            });
                }
            }
        });
    }


}
