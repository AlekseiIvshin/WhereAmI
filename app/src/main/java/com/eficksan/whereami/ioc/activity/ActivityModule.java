package com.eficksan.whereami.ioc.activity;

import android.app.Activity;

import com.eficksan.whereami.data.auth.FirebaseDatabaseUsersRepository;
import com.eficksan.whereami.data.auth.UsersRepository;
import com.eficksan.whereami.data.messages.MessagesDataSource;
import com.eficksan.whereami.data.votes.VotesDataSource;
import com.eficksan.whereami.presentation.routing.Router;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Aleksei Ivshin
 * on 28.08.2016.
 */
@Module
public class ActivityModule {

    private final Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    @ActivityScope
    public Activity provideActivity() {
        return activity;
    }

    @Provides
    @ActivityScope
    public Router provideRouter() {
        return (Router) activity;
    }

    @Provides
    public MessagesDataSource provideMessagesDataSource(FirebaseDatabase firebaseDatabase) {
        return new MessagesDataSource(firebaseDatabase);
    }

    @Provides
    public UsersRepository provideUsersRepository(FirebaseDatabase firebaseDatabase, FirebaseAuth firebaseAuth) {
        return new FirebaseDatabaseUsersRepository(firebaseDatabase, firebaseAuth);
    }

    @Provides
    public VotesDataSource provideVotesDataSource(FirebaseDatabase firebaseDatabase) {
        return new VotesDataSource(firebaseDatabase);
    }

    @Provides
    @Named("currentUserId")
    public String provideCurrentUserId(FirebaseAuth firebaseAuth) {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            return currentUser.getUid();
        }
        return null;
    }
}
