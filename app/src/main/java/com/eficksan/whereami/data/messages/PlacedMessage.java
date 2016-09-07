package com.eficksan.whereami.data.messages;

import com.eficksan.whereami.data.auth.User;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Aleksei Ivshin
 * on 07.09.2016.
 */
@IgnoreExtraProperties
public class PlacedMessage {

    PlacingMessage data;
    User user;
}
