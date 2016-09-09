package com.eficksan.whereami.data.votes;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Vote POJO.
 */
@IgnoreExtraProperties
public class Vote {

    public String messageId;
    public String userId;
    public boolean isVotedFor;
}
