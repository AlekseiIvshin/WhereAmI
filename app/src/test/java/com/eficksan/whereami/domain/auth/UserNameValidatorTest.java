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
public class UserNameValidatorTest {

    UserNameValidator mValidator = new UserNameValidator();

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"UseraANmsd2", false},
                {"username", true},
                {"USERNAME", true},
                {"USER_NAME", true},
                {"usEr.Name", true},
                {"user", false}, // too short
                {"username@", false}, // special symbol “@” is not allow her
                {"111111111", false}, // required at least one char
                {"_username", false}, // should not start from _
                {".username", false}, // should not start from .
                {".username", false}, // should not start from .
                {"user__name", false},
                {"user..name", false},
                {"user._name", false},
                {"user_.name", false},
        });
    }

    @Parameterized.Parameter
    public String testUsername;

    @Parameterized.Parameter(value = 1)
    public boolean testExpectedResult;

    @Test
    public void shouldValidatePasswords() throws Exception {
        // Then
        boolean actualResult = mValidator.validate(testUsername);

        // When
        assertEquals(
                String.format("For '%s' expected:<%b> but was:<%b>", testUsername, testExpectedResult, actualResult),
                testExpectedResult,
                actualResult);
    }
}
