package com.eficksan.whereami.ioc.app;

import android.content.Context;
import android.support.annotation.Nullable;

import com.eficksan.whereami.data.auth.UsersDataSource;
import com.eficksan.whereami.data.messages.MessagesDataSource;
import com.eficksan.whereami.data.votes.VotesDataSource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Aleksei Ivshin
 * on 28.08.2016.
 */
@Module
public class AppModule {
    private final Context applicationContext;

    public AppModule(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Provides
    @Singleton
    public Context provideContext() {
        return applicationContext;
    }

    @Provides
    @Singleton
    public FirebaseAuth provideFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    @Provides
    @Singleton
    public FirebaseDatabase provideFirebaseDatabase() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        return FirebaseDatabase.getInstance();
    }

    @Provides
    @Nullable
    public FirebaseUser provideCurrentUser(FirebaseAuth firebaseAuth) {
        return firebaseAuth.getCurrentUser();
    }

    @Provides
    @Named("job")
    public Scheduler provideJobScheduler() {
        return Schedulers.computation();
    }

    @Provides
    @Named("ui")
    public Scheduler provideUiScheduler() {
        return AndroidSchedulers.mainThread();
    }

    @Provides
    public MessagesDataSource provideMessagesDataSource(FirebaseDatabase firebaseDatabase) {
        return new MessagesDataSource(firebaseDatabase);
    }

    @Provides
    public UsersDataSource provideUsersDataSource(FirebaseDatabase firebaseDatabase) {
        return new UsersDataSource(firebaseDatabase);
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
