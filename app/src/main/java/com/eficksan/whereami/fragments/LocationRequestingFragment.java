package com.eficksan.whereami.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eficksan.whereami.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Aleksei Ivshin
 * on 24.04.2016.
 */
public class LocationRequestingFragment extends Fragment{

    @Bind(R.id.location_label)
    TextView mLocationLabel;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_location_requesting, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @OnClick(R.id.request_location)
    public void handleRequestLocation() {
        throw new RuntimeException("Not implemented yet!");
    }
}
