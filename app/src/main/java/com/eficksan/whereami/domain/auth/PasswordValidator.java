package com.eficksan.whereami.domain.auth;

import java.util.regex.Pattern;

/**
 * Provides validation entered password.
 */
public class PasswordValidator {
    private static final String PASSWORD_PATTERN = "^(\\S*(?=\\S*[a-z]+)(?=\\S*[A-Z]+)(?=\\S*[0-9])\\S*){6,20}$";
    private final Pattern mPattern;

    public PasswordValidator() {
        mPattern = Pattern.compile(PASSWORD_PATTERN);
    }

    public boolean validate(String password) {
        return mPattern.matcher(password).matches();
    }
}
