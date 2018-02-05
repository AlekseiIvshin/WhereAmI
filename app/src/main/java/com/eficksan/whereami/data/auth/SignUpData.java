package com.eficksan.whereami.data.auth;

/**
 * Sign in data POJO.
 */
public class SignUpData {
    public final String email;
    public final String userName;
    public final String password;

    public SignUpData(String email, String userName, String password) {
        this.email = email;
        this.userName = userName;
        this.password = password;
    }
}
