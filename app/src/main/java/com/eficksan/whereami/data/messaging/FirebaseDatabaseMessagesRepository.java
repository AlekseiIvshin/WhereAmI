package com.eficksan.whereami.data.messaging;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Messags repository implementation using {@link <a href="https://firebase.google.com">Firebase</a>} services.
 */
public class FirebaseDatabaseMessagesRepository{

    private static final String DB_MESSAGES = "messages";

    private static final String TAG = FirebaseDatabaseMessagesRepository.class.getSimpleName();
    private final FirebaseDatabase firebaseDatabase;
    private final FirebaseAuth firebaseAuth;

    public FirebaseDatabaseMessagesRepository(FirebaseDatabase firebaseDatabase, FirebaseAuth firebaseAuth) {
        this.firebaseDatabase = firebaseDatabase;
        this.firebaseAuth = firebaseAuth;
    }

    public Task<Void> addMessage(PlacingMessage placingMessage) {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            Log.e(TAG, "Adding message: user was not logged in");
            return null;
        }
        PlacingMessageEntity placingMessageEntity = new PlacingMessageEntity(placingMessage, currentUser.getUid());
        DatabaseReference database = firebaseDatabase.getReference();
        String newMessageKey = database.child(DB_MESSAGES).push().getKey();
        Map<String, Object> messageValues = placingMessageEntity.toMap();


        HashMap<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(String.format("/%s/%s",DB_MESSAGES, newMessageKey),messageValues);
        return database.updateChildren(childUpdates);
    }

    @IgnoreExtraProperties
    private static class PlacingMessageEntity {
        public double latitude;
        public double longitude;
        public String message;
        public String userId;

        public PlacingMessageEntity() {

        }

        public PlacingMessageEntity(PlacingMessage placingMessage, String userId) {
            this.latitude = placingMessage.latitude;
            this.longitude = placingMessage.longitude;
            this.message = placingMessage.message;
            this.userId = userId;
        }

        @Exclude
        public Map<String, Object> toMap() {
            HashMap<String, Object> result = new HashMap<>();
            result.put("userId", userId);
            result.put("latitude", latitude);
            result.put("longitude", longitude);
            result.put("message", message);
            return result;
        }
    }
}
