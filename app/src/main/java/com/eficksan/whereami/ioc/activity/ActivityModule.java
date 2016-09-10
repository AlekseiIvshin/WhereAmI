package com.eficksan.whereami.ioc.activity;

import android.app.Activity;

import com.eficksan.whereami.data.auth.FirebaseDatabaseUsersRepository;
import com.eficksan.whereami.data.auth.UsersRepository;
import com.eficksan.whereami.data.messages.FirebaseDatabaseMessagesRepository;
import com.eficksan.whereami.data.messages.MessagesRepository;
import com.eficksan.whereami.data.votes.FirebaseDatabaseVotesRepository;
import com.eficksan.whereami.data.votes.VotesRepository;
import com.eficksan.whereami.presentation.routing.Router;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

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
    public MessagesRepository provideMessagesRepository(FirebaseDatabase firebaseDatabase, FirebaseAuth firebaseAuth) {
        return new FirebaseDatabaseMessagesRepository(firebaseDatabase,firebaseAuth);
    }

    @Provides
    public UsersRepository provideUsersRepository(FirebaseDatabase firebaseDatabase, FirebaseAuth firebaseAuth) {
        return new FirebaseDatabaseUsersRepository(firebaseDatabase,firebaseAuth);
    }

    @Provides
    public VotesRepository provideVotesRepository(FirebaseDatabase firebaseDatabase, FirebaseAuth firebaseAuth) {
        return new FirebaseDatabaseVotesRepository(firebaseDatabase,firebaseAuth);
    }

    @Provides
    public FirebaseDatabaseVotesRepository provideFirebaseVotesRepository(FirebaseDatabase firebaseDatabase, FirebaseAuth firebaseAuth) {
        return new FirebaseDatabaseVotesRepository(firebaseDatabase,firebaseAuth);
    }
}
