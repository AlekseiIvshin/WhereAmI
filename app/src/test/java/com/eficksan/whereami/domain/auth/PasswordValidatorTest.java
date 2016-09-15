package com.eficksan.whereami.domain.auth;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

/**
 * Tests cases for email validation interactor.
 */
@RunWith(Parameterized.class)
public class PasswordValidatorTest {

    PasswordValidator mPasswordValidator = new PasswordValidator();

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { "password1A@", true },
                { "pasSWorD12$", true },
                { "pass", false }, // too short
                { "password1a@", false }, // uppercase characters is required
                { "pasSWor12*", false }, // special symbol “*” is not allow here
                { "passWOrd$$", false }, // digit is required
                { "PASSWORD12$", false }, // lower case character is required
        });
    }

    @Parameterized.Parameter
    public String testPassword;

    @Parameterized.Parameter(value = 1)
    public boolean testExpectedResult;

    @Test
    public void shouldValidatePasswords() throws Exception {
        // Then
        boolean actualResult = mPasswordValidator.validate(testPassword);

        // When
        assertEquals(String.format("Expected password '%s' is valid = %b, but it is valid %b", testPassword, testExpectedResult, actualResult), testExpectedResult, actualResult);
    }
}