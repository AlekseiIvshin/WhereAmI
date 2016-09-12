package com.eficksan.whereami.ioc.activity;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;

import com.eficksan.whereami.data.auth.UsersRepository;
import com.eficksan.whereami.data.messages.MessagesRepository;
import com.eficksan.whereami.data.votes.VotesDataSource;
import com.eficksan.whereami.ioc.app.AppComponent;
import com.eficksan.whereami.presentation.MainActivity;
import com.eficksan.whereami.presentation.routing.Router;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Named;

import dagger.Component;
import rx.Scheduler;

/**
 * Created by Aleksei Ivshin
 * on 28.08.2016.
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    Context applicationContext();

    FirebaseAuth firebaseAuth();

    FirebaseDatabase firebaseDatabase();

    @Named("currentUserId")
    String currentUserId();

    MessagesRepository messagesRepository();

    UsersRepository usersRepository();

    VotesDataSource votesDataSource();

    @Named("job")
    Scheduler jobScheduler();

    @Named("ui")
    Scheduler uiScheduler();

    @Nullable
    FirebaseUser currentUser();

    Router router();

    Activity activity();

    void inject(MainActivity mainActivity);
}
