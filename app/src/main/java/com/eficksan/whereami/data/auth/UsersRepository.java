package com.eficksan.whereami.data.auth;

import rx.Subscriber;

/**
 * Created by Aleksei Ivshin
 * on 07.09.2016.
 */
public interface UsersRepository {

    /**
     * Sets user name.
     * @param userName new user name
     * @return true if user name changes will applied, false - otherwise
     */
    boolean setCurrentUserName(String userName);

    /**
     * Search user by id
     * @param userId user id
     * @param subscriber subscriber
     */
    void findUserById(String userId, Subscriber<User> subscriber);
}
