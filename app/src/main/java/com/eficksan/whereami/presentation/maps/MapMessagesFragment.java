package com.eficksan.whereami.presentation.maps;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eficksan.whereami.App;
import com.eficksan.whereami.R;
import com.eficksan.whereami.ioc.maps.MapsComponent;
import com.eficksan.whereami.presentation.common.ComponentLifecycleFragment;
import com.eficksan.whereami.presentation.common.IPresenter;

import javax.inject.Inject;

/**
 * Fragments provides map for showing near messages.
 */
public class MapMessagesFragment extends ComponentLifecycleFragment {

    public static final String TAG = MapMessagesFragment.class.getSimpleName();
    private MapsComponent mMapsComponent;

    @Inject
    MapMessagesView mMapMessagesView;

    @Inject
    MapMessagesPresenter mPresenter;

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

        mMapMessagesView.takeView(view);
        mPresenter.onViewCreated(mMapMessagesView);
        mMapMessagesView.messagesMap.onCreate(savedInstanceState);
    }

    @Override
    public IPresenter getPresenter() {
        return mPresenter;
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
        throw new NullPointerException("It's a trap!");
    }

    @Override
    public void onPause() {
        mMapMessagesView.messagesMap.onPause();
        super.onPause();
    }

    @Override
    public void onSetUpComponent() {
        mMapsComponent = ((App) getActivity().getApplication()).plusMapsComponent();
        mMapsComponent.inject(this);
    }

    @Override
    public void onKillComponent() {
        ((App) getActivity().getApplication()).removeMapsComponent();
    }
}
