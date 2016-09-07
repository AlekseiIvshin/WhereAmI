package com.eficksan.whereami.data.messages;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Messags repository implementation using {@link <a href="https://firebase.google.com">Firebase</a>} services.
 */
public class FirebaseDatabaseMessagesRepository implements MessagesRepository {

    private static final String DB_MESSAGES = "messages";

    private static final String TAG = FirebaseDatabaseMessagesRepository.class.getSimpleName();
    private final FirebaseDatabase firebaseDatabase;
    private final FirebaseAuth firebaseAuth;

    public FirebaseDatabaseMessagesRepository(FirebaseDatabase firebaseDatabase, FirebaseAuth firebaseAuth) {
        this.firebaseDatabase = firebaseDatabase;
        this.firebaseAuth = firebaseAuth;
    }

    @Override
    public boolean addMessage(PlacingMessage placingMessage) {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            Log.e(TAG, "Adding message: user was not logged in");
            return false;
        }
        DatabaseReference database = firebaseDatabase.getReference();
        String newMessageKey = database.child(DB_MESSAGES).push().getKey();
        Map<String, Object> messageValues = toMap(placingMessage, currentUser.getUid());

        HashMap<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(String.format("/%s/%s", DB_MESSAGES, newMessageKey), messageValues);
        database.updateChildren(childUpdates);
        return true;
    }

    private static HashMap<String, Object> toMap(PlacingMessage placingMessage, String userId) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userId", userId);
        result.put("latitude", placingMessage.latitude);
        result.put("longitude", placingMessage.longitude);
        result.put("message", placingMessage.message);
        return result;
    }
}
