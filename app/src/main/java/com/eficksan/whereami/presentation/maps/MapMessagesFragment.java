package com.eficksan.whereami.presentation.maps;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eficksan.whereami.R;

/**
 * Fragments provides map for showing near messages.
 */
public class MapMessagesFragment extends Fragment {

    private MapMessagesView mMapMessagesView;
    private MapMessagesPresenter mMapMessagesPresenter;

    public MapMessagesFragment() {
        // Required empty public constructor
    }

    /**
     * Factory method for creating fragment instnace.
     *
     * @return A new instance of fragment MapMessagesFragment.
     */
    public static MapMessagesFragment newInstance() {
        return new MapMessagesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_messages_map, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMapMessagesView =new MapMessagesView();
        mMapMessagesView.takeView(view);
        mMapMessagesView.messagesMap.onCreate(savedInstanceState);
        mMapMessagesPresenter = new MapMessagesPresenter();
        mMapMessagesPresenter.setView(mMapMessagesView);
        mMapMessagesPresenter.onCreate();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapMessagesView.messagesMap.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapMessagesView.messagesMap.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        mMapMessagesView.messagesMap.onPause();
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        mMapMessagesView.messagesMap.onDestroy();
        mMapMessagesPresenter.onDestroy();
        super.onDestroyView();
    }
}
