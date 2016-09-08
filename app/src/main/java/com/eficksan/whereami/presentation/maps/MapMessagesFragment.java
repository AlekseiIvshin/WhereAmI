package com.eficksan.whereami.presentation.maps;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eficksan.whereami.App;
import com.eficksan.whereami.R;
import com.eficksan.whereami.ioc.maps.MapsComponent;

/**
 * Fragments provides map for showing near messages.
 */
public class MapMessagesFragment extends Fragment {

    public static final String TAG = MapMessagesFragment.class.getSimpleName();
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
        MapsComponent mapsComponent = ((App) getActivity().getApplication()).plusMapsComponent();
        mMapMessagesView =new MapMessagesView();
        mapsComponent.inject(mMapMessagesView);
        mMapMessagesView.takeView(view);
        mMapMessagesView.messagesMap.onCreate(savedInstanceState);
        mMapMessagesPresenter = new MapMessagesPresenter();
        mapsComponent.inject(mMapMessagesPresenter);
        mMapMessagesPresenter.setView(mMapMessagesView);
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapMessagesPresenter.onStart();
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
    public void onStop() {
        super.onStop();
        mMapMessagesPresenter.onStop();
    }

    @Override
    public void onDestroyView() {
        mMapMessagesView.onDestroy();
        ((App)getActivity().getApplication()).removeMapsComponent();
        super.onDestroyView();
    }
}
