package com.eficksan.whereami.domain.messages;

import com.eficksan.whereami.domain.Constants;
import com.eficksan.whereami.domain.Validator;

/**
 * Provides method for validation message.
 */
public class PlaceMessageValidator implements Validator {

    @Override
    public boolean validate(String message) {
        return (message != null && !message.isEmpty() && message.length() < Constants.MAX_MESSAGE_SIZE);
    }
}
