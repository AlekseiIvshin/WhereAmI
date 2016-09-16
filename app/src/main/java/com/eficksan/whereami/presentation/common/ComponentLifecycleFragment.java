package com.eficksan.whereami.presentation.common;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eficksan.whereami.App;
import com.eficksan.whereami.R;
import com.eficksan.whereami.ioc.location.LocationComponent;
import com.eficksan.whereami.presentation.location.WhereAmIPresenter;
import com.eficksan.whereami.presentation.location.WhereAmIView;
import com.eficksan.whereami.presentation.routing.Router;

import javax.inject.Inject;

/**
 * Fragment shows current user location.
 */
public abstract class ComponentLifecycleFragment extends Fragment {
    public static final String TAG = ComponentLifecycleFragment.class.getSimpleName();

    private boolean mIsDestroyBySystem;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.onAddComponent();
    }

    @Override
    public void onResume() {
        super.onResume();
        mIsDestroyBySystem = false;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mIsDestroyBySystem = true;
    }

    @Override
    public void onDestroy() {
        if (!mIsDestroyBySystem) {
            this.onKillComponent();
        }
        super.onDestroy();
    }

    public abstract void onAddComponent();
    public abstract void onKillComponent();
}
