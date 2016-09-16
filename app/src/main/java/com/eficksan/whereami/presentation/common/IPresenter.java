package com.eficksan.whereami.presentation.common;

import android.os.Bundle;

import com.eficksan.whereami.presentation.IView;
import com.eficksan.whereami.presentation.routing.Router;

/**
 * Created by Aleksei_Ivshin on 9/16/16.
 */
public interface IPresenter<VIEW_TYPE extends IView> {

    void onCreate(Bundle savedInstanceStates);

    void takeRouter(Router router);

    void onViewCreated(VIEW_TYPE view);

    void onStart();

    void onSaveInstanceState(Bundle states);

    void onStop();

    /**
     * Releases view component.
     * Callback mirrors fragment or activity callback.
     */
    void onViewDestroyed();

    void releaseRouter();

    void onDestroy();

}
