package com.eficksan.whereami.data.auth;

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
}
