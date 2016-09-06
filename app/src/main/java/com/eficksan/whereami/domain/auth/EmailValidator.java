package com.eficksan.whereami.domain.auth;

import java.util.regex.Pattern;

/**
 * Provides validation entered email.
 */
public class EmailValidator {
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private final Pattern mPattern;

    public EmailValidator() {
        mPattern = Pattern.compile(EMAIL_PATTERN);
    }

    public boolean validate(String email) {
        return mPattern.matcher(email).matches();
    }
}
