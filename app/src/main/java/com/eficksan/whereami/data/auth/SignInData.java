package com.eficksan.whereami.data.auth;

/**
 * Sign in data POJO.
 */
public class SignInData {
    public final String email;
    public final String password;

    public SignInData(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
