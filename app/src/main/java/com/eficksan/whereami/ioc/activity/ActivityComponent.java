package com.eficksan.whereami.ioc.activity;

import android.content.Context;
import android.support.annotation.Nullable;

import com.eficksan.whereami.data.auth.UsersDataSource;
import com.eficksan.whereami.data.messages.MessagesDataSource;
import com.eficksan.whereami.data.votes.VotesDataSource;
import com.eficksan.whereami.ioc.app.AppComponent;
import com.eficksan.whereami.presentation.MainActivity;
import com.eficksan.whereami.presentation.routing.Router;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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

    @Named("currentUserId")
    String currentUserId();

    MessagesDataSource messageDataSource();

    UsersDataSource userDataSource();

    VotesDataSource votesDataSource();

    @Named("job")
    Scheduler jobScheduler();

    @Named("ui")
    Scheduler uiScheduler();

    @Nullable
    FirebaseUser currentUser();

    Router router();

    void inject(MainActivity mainActivity);
}
