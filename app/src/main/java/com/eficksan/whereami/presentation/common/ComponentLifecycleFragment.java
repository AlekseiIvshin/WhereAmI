package com.eficksan.whereami.presentation.common;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Fragment shows current user location.
 */
public abstract class ComponentLifecycleFragment extends Fragment {
    public static final String TAG = ComponentLifecycleFragment.class.getSimpleName();

    private boolean mIsDestroyBySystem;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.onSetUpComponent();
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

    public abstract void onSetUpComponent();
    public abstract void onKillComponent();
}
