package com.eficksan.whereami.domain.messaging;

import com.eficksan.placingmessages.PlaceMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Aleksei Ivshin
 * on 30.08.2016.
 */
public class MessagesContainer {

    private final CopyOnWriteArrayList<PlaceMessage> placeMessages;

    public MessagesContainer() {
        placeMessages = new CopyOnWriteArrayList<>();
    }

    public List<PlaceMessage> getMessages(){
        return placeMessages;
    }

    public void setMessages(List<PlaceMessage> messages) {
        placeMessages.addAll(messages);
    }
}
