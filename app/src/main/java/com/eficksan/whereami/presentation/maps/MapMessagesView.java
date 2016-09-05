package com.eficksan.whereami.presentation.maps;

import android.os.Bundle;
import android.view.View;

import com.eficksan.whereami.R;
import com.google.android.gms.maps.MapView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Aleksei_Ivshin on 9/5/16.
 */
public class MapMessagesView {

    @Bind(R.id.messages_map)
    public MapView messagesMap;

    public void takeView(View view) {
        ButterKnife.bind(this, view);
    }

}
