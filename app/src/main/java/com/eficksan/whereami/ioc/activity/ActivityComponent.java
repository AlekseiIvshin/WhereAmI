package com.eficksan.whereami.ioc.activity;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;

import com.eficksan.whereami.data.auth.UsersRepository;
import com.eficksan.whereami.data.messages.FirebaseDatabaseMessagesRepository;
import com.eficksan.whereami.data.messages.MessagesRepository;
import com.eficksan.whereami.data.votes.FirebaseDatabaseVotesRepository;
import com.eficksan.whereami.data.votes.VotesRepository;
import com.eficksan.whereami.ioc.app.AppComponent;
import com.eficksan.whereami.presentation.MainActivity;
import com.eficksan.whereami.presentation.routing.Router;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import dagger.Component;

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

    MessagesRepository messagesRepository();

    UsersRepository usersRepository();

    VotesRepository votesRepository();

    FirebaseDatabaseVotesRepository votesRepositoryFirebase();

    @Nullable
    FirebaseUser currentUser();

    Router router();

    Activity activity();

    void inject(MainActivity mainActivity);
}
