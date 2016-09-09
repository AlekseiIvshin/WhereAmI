package com.eficksan.whereami.data.messages;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import rx.Subscriber;

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

    /**
     * Fetching messages for location.
     * @param latLng coordinates
     * @param subscriber subscriber
     */
    void fetchMessages(LatLng latLng, Subscriber<List<PlacingMessage>> subscriber);

    /**
     * Find message by id.
     * @param messageId message id
     * @param subscriber subscriber
     */
    void findMessageById(String messageId, Subscriber<PlacingMessage> subscriber);
}
