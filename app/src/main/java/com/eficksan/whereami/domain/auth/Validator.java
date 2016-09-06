package com.eficksan.whereami.domain.auth;

/**
 * Provide method for validation string value.
 */
public interface Validator {

    /**
     * Validates value.
     * @param value value
     * @return true if value is valid, false otherwise
     */
    boolean validate(String value);
}
