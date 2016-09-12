package com.eficksan.whereami.ioc.app;

import android.content.Context;
import android.support.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import org.mockito.Mockito;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.schedulers.TestScheduler;

import static org.mockito.Mockito.mock;

/**
 * Created by Aleksei Ivshin
 * on 28.08.2016.
 */
@Module
public class TestAppModule extends AppModule {
    private final Context applicationContext;

    private final TestScheduler testScheduler = new TestScheduler();

    public TestAppModule(Context applicationContext) {
        super(applicationContext);
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
        return mock(FirebaseAuth.class);
    }

    @Provides
    @Singleton
    public FirebaseDatabase provideFirebaseDatabase() {
        return mock(FirebaseDatabase.class);
    }

    @Provides
    @Nullable
    public FirebaseUser provideCurrentUser(FirebaseAuth firebaseAuth) {
        return mock(FirebaseUser.class);
    }

    @Provides
    @Named("job")
    public Scheduler provideJobScheduler() {
        return testScheduler;
    }

    @Provides
    @Named("ui")
    public Scheduler provideUiScheduler() {
        return testScheduler;
    }

}
