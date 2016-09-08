package com.eficksan.whereami.data.auth;

import android.util.Log;

import com.eficksan.whereami.data.messages.PlacingMessage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import rx.Subscriber;

/**
 * Messags repository implementation using {@link <a href="https://firebase.google.com">Firebase</a>} services.
 */
public class FirebaseDatabaseUsersRepository implements UsersRepository {

    private static final String DB_USERS = "users";

    private static final String TAG = FirebaseDatabaseUsersRepository.class.getSimpleName();
    private final FirebaseDatabase mDatabase;
    private final FirebaseAuth firebaseAuth;

    public FirebaseDatabaseUsersRepository(FirebaseDatabase firebaseDatabase, FirebaseAuth firebaseAuth) {
        this.mDatabase = firebaseDatabase;
        this.firebaseAuth = firebaseAuth;
    }

    @Override
    public boolean setCurrentUserName(String userName) {

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            Log.e(TAG, "Adding message: user was not logged in");
            return false;
        }
        String currentUserId = currentUser.getUid();

        DatabaseReference database = mDatabase.getReference();

        User user = new User();
        user.id = currentUserId;
        user.name = userName;

        HashMap<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(String.format("/%s/%s", DB_USERS, currentUserId), user);
        database.updateChildren(childUpdates);

        return true;
    }

    @Override
    public void findUserById(final String userId,final Subscriber<User> subscriber) {
        mDatabase.getReference().child(DB_USERS).child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
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
