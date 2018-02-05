package com.eficksan.whereami.domain.users;

import com.eficksan.whereami.data.auth.User;
import com.eficksan.whereami.data.auth.UsersDataSource;
import com.eficksan.whereami.domain.BaseInteractor;

import rx.Observable;
import rx.Scheduler;

/**
 * Provides methods for async loading messages.
 */
public class FindUserInteractor extends BaseInteractor<String, User> {

    private final UsersDataSource usersDataSource;

    public FindUserInteractor(UsersDataSource usersDataSource, Scheduler jobScheduler, Scheduler uiScheduler) {
        super(jobScheduler, uiScheduler);
        this.usersDataSource = usersDataSource;
    }

    @Override
    protected Observable<User> buildObservable(String userId) {
        return usersDataSource.findUserById(userId);
    }

}
