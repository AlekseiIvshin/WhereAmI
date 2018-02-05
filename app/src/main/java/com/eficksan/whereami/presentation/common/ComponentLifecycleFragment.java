package com.eficksan.whereami.presentation.common;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.eficksan.whereami.presentation.routing.Router;

/**
 * Fragment shows current user location.
 */
public abstract class ComponentLifecycleFragment extends Fragment {
    public static final String TAG = ComponentLifecycleFragment.class.getSimpleName();

    private boolean mIsDestroyBySystem;

    public abstract IPresenter getPresenter();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.onSetUpComponent();
        IPresenter presenter = getPresenter();
        presenter.takeRouter((Router) getActivity());
        presenter.onCreate(savedInstanceState);
    }


    @Override
    public void onStart() {
        super.onStart();
        getPresenter().onStart();
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
        getPresenter().onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        getPresenter().onStop();
        super.onStop();
    }


    @Override
    public void onDestroyView() {
        getPresenter().onViewDestroyed();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        getPresenter().releaseRouter();
        getPresenter().onDestroy();
        if (!mIsDestroyBySystem) {
            this.onKillComponent();
        }
        super.onDestroy();
    }

    public abstract void onSetUpComponent();

    public abstract void onKillComponent();
}
