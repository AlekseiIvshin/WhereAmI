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
public class EmailValidatorTest {

    EmailValidator mEmailValidator = new EmailValidator();

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"test@yahoo.com", true},
                {"test-100@yahoo.com", true},
                {"test.100@yahoo.com", true},
                {"test111@test.com", true},
                {"test-100@test.net", true},
                {"test.100@test.com.au", true},
                {"test@1.com", true},
                {"test@gmail.com.com", true},
                {"test+100@gmail.com", true},
                {"test-100@yahoo-test.com", true},
                {"test", false},
                {"test@.com.my", false},
                {"test123@gmail.a", false},
                {"test123@.com", false},
                {"test123@.com.com", false},
                {".test@test.com", false},
                {"test()*@gmail.com", false},
                {"test@%*.com", false},
                {"test..2002@gmail.com", false},
                {"test.@gmail.com", false},
                {"test@test@gmail.com", false},
                {"mkyong@gmail.com.1a", false},
        });
    }

    @Parameterized.Parameter
    public String testEmail;

    @Parameterized.Parameter(value = 1)
    public boolean testExpectedResult;

    @Test
    public void shouldValidateEmails() throws Exception {
        // Then
        boolean actualResult = mEmailValidator.validate(testEmail);

        // When
        assertEquals(
                String.format(
                        "Email '%s' expected is valid = %b, but it is valid = %b",
                        testEmail, testExpectedResult, actualResult),
                testExpectedResult,
                actualResult);
    }
}
