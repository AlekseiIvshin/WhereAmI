package com.eficksan.whereami.domain.auth;

import com.eficksan.whereami.domain.Validator;

import java.util.regex.Pattern;

/**
 * Provides validation entered email.
 */
public class UserNameValidator implements Validator {
    private static final String PATTERN = "^(?=.{8,20}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.0-9])$";
    private final Pattern mPattern;

    public UserNameValidator() {
        mPattern = Pattern.compile(PATTERN);
    }

    @Override
    public boolean validate(String userName) {
        return mPattern.matcher(userName).matches();
    }
}
