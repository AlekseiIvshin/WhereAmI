package com.eficksan.whereami.data.auth;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import rx.Observable;
import rx.Subscriber;

/**
 * Messags repository implementation using {@link <a href="https://firebase.google.com">Firebase</a>} services.
 */
public class UsersDataSource {

    private static final String DB_USERS = "users";

    private static final String TAG = UsersDataSource.class.getSimpleName();
    private final FirebaseDatabase mDatabase;

    public UsersDataSource(FirebaseDatabase firebaseDatabase) {
        this.mDatabase = firebaseDatabase;
    }

    /**
     * Search user by id.
     *
     * @param userId user id
     */
    public Observable<User> findUserById(final String userId) {
        return Observable.create(new Observable.OnSubscribe<User>() {
            @Override
            public void call(final Subscriber<? super User> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    mDatabase.getReference()
                            .child(DB_USERS)
                            .child(userId)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Log.v(TAG, "Found user with id = " + userId);
                                    subscriber.onNext(dataSnapshot.getValue(User.class));
                                    subscriber.onCompleted();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    subscriber.onError(databaseError.toException());
                                }
                            });
                }
            }
        });
    }


    /**
     * Sets user name.
     *
     * @param userName new user name
     * @return true if user name changes will applied, false - otherwise
     */
    public Observable<Boolean> setCurrentUserName(final String userId, final String userName) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    User user = new User();
                    user.id = userId;
                    user.name = userName;

                    mDatabase.getReference().child(DB_USERS).child(userId).setValue(user);
                    subscriber.onNext(true);
                    subscriber.onCompleted();
                }
            }
        });
    }
}
