package com.eficksan.whereami.ioc.app;

import android.content.Context;
import android.support.annotation.Nullable;

import com.eficksan.whereami.data.auth.UsersDataSource;
import com.eficksan.whereami.data.messages.MessagesDataSource;
import com.eficksan.whereami.data.votes.VotesDataSource;
import com.eficksan.whereami.presentation.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Component;
import rx.Scheduler;

/**
 * Created by Aleksei Ivshin
 * on 28.08.2016.
 */
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    Context applicationContext();

    FirebaseAuth firebaseAuth();

    FirebaseDatabase firebaseDatabase();

    @Named("job")
    Scheduler jobScheduler();

    @Named("ui")
    Scheduler uiScheduler();

    @Nullable
    FirebaseUser currentUser();

    @Named("currentUserId")
    String currentUserId();

    MessagesDataSource messageDataSource();

    UsersDataSource userDataSource();

    VotesDataSource votesDataSource();

    void inject(MainActivity mainActivity);
}
