package com.eficksan.whereami.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eficksan.whereami.R;

import butterknife.ButterKnife;

/**
 * Created by Aleksei Ivshin
 * on 24.04.2016.
 */
public class LocationRequestingFragment extends Fragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_location_requesting, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
