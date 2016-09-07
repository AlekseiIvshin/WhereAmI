package com.eficksan.whereami.data.messages;

/**
 * Provides methods for basic operations on messages.
 */
public interface MessagesRepository {

    /**
     * Add message for location.
     * @param placingMessage messgae
     * @return true - message adding is applied, false - otherwise
     */
    boolean addMessage(PlacingMessage placingMessage);
}
