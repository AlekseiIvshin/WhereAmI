package com.eficksan.whereami.data.auth;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

/**
 * Messags repository implementation using {@link <a href="https://firebase.google.com">Firebase</a>} services.
 */
public class FirebaseDatabaseUsersRepository implements UsersRepository {

    private static final String DB_USERS = "user";

    private static final String TAG = FirebaseDatabaseUsersRepository.class.getSimpleName();
    private final FirebaseDatabase firebaseDatabase;
    private final FirebaseAuth firebaseAuth;

    public FirebaseDatabaseUsersRepository(FirebaseDatabase firebaseDatabase, FirebaseAuth firebaseAuth) {
        this.firebaseDatabase = firebaseDatabase;
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

        DatabaseReference database = firebaseDatabase.getReference();

        HashMap<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(String.format("/%s/%s", DB_USERS, currentUserId), getUserNameUpdates(currentUserId, userName));
        database.updateChildren(childUpdates);

        return true;
    }

    private HashMap<String, Object> getUserNameUpdates(String userId, String userName) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", userId);
        result.put("name", userName);
        return result;
    }
}
