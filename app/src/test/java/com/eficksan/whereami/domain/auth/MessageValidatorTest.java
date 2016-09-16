package com.eficksan.whereami.domain.auth;

import com.eficksan.whereami.domain.messages.PlaceMessageValidator;

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
public class MessageValidatorTest {

    PlaceMessageValidator placeMessageValidator = new PlaceMessageValidator();

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"Simple message", true},
                {"Lorem ipsum dolor sit amet, consectetur adipiscing elit. "
                        + "Integer eget nisi aliquam, pretium erat non, "
                        + "gravida lorem. Maecenas vestibulum dignissim.", false}, // too long
                {"", false}, // empty
        });
    }

    @Parameterized.Parameter
    public String testMessage;

    @Parameterized.Parameter(value = 1)
    public boolean testExpectedResult;

    @Test
    public void shouldValidateEmails() throws Exception {
        // Then
        boolean actualResult = placeMessageValidator.validate(testMessage);

        // When
        assertEquals(testExpectedResult, actualResult);
    }
}
