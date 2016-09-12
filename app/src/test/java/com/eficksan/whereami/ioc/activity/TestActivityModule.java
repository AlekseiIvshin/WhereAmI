package com.eficksan.whereami.ioc.activity;

import android.app.Activity;

import com.eficksan.whereami.data.auth.UsersRepository;
import com.eficksan.whereami.data.messages.MessagesRepository;
import com.eficksan.whereami.data.votes.VotesDataSource;
import com.eficksan.whereami.presentation.routing.Router;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import dagger.Module;
import dagger.Provides;

import static org.mockito.Mockito.mock;

/**
 * Created by Aleksei Ivshin
 * on 28.08.2016.
 */
@Module
@ActivityScope
public class TestActivityModule extends ActivityModule {

    private final Activity activity;

    public TestActivityModule(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Provides
    public Activity provideActivity() {
        return activity;
    }

    @Provides
    public Router provideRouter() {
        return mock(Router.class);
    }

    @Provides
    public MessagesRepository provideMessagesRepository(FirebaseDatabase firebaseDatabase, FirebaseAuth firebaseAuth) {
        return mock(MessagesRepository.class);
    }

    @Provides
    public UsersRepository provideUsersRepository(FirebaseDatabase firebaseDatabase, FirebaseAuth firebaseAuth) {
        return mock(UsersRepository.class);
    }

    @Provides
    public VotesDataSource provideVotesDataSource(FirebaseDatabase firebaseDatabase) {
        return mock(VotesDataSource.class);
    }
}
