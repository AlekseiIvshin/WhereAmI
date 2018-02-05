package com.eficksan.whereami.ioc.common;

import com.eficksan.whereami.data.auth.UsersDataSource;
import com.eficksan.whereami.domain.users.FindUserInteractor;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import rx.Scheduler;

/**
 * Created by Aleksei Ivshin
 * on 28.08.2016.
 */
@Module
public class UsersModule {

    @Provides
    public FindUserInteractor provideFindUserInteractor(
            UsersDataSource usersDataSource,
            @Named("job") Scheduler jobScheduler,
            @Named("ui") Scheduler uiScheduler) {
        return new FindUserInteractor(usersDataSource, jobScheduler, uiScheduler);
    }
}
