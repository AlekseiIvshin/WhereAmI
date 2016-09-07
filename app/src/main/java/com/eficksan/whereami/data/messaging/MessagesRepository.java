package com.eficksan.whereami.data.messaging;

/**
 * Provides methods for basic operations on messages.
 */
public interface MessagesRepository {

    /**
     * Add message for location.
     * @param placingMessage messgae
     * @return true - message was added, false - otherwise
     */
    boolean addMessage(PlacingMessage placingMessage);
}
