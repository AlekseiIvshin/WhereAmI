package com.eficksan.whereami.presentation.common;

import android.os.Bundle;

import com.eficksan.whereami.presentation.IView;
import com.eficksan.whereami.presentation.routing.Router;

/**
 * Created by Aleksei_Ivshin on 9/16/16.
 */
public abstract class BasePresenter<VIEW_TYPE extends IView> implements IPresenter<VIEW_TYPE> {

    protected Router mRouter;
    protected VIEW_TYPE mView;

    public void onCreate(Bundle savedInstanceStates) {

    }

    public void takeRouter(Router router) {
        this.mRouter = router;
    }

    public void onViewCreated(VIEW_TYPE view) {
        mView = view;
    }

    public void onStart() {

    }

    public void onSaveInstanceState(Bundle states) {
    }

    public void onStop() {

    }

    /**
     * Releases view component.
     * Callback mirrors fragment or activity callback.
     */
    public void onViewDestroyed() {
        mView.releaseView();
        mView = null;
    }

    public void releaseRouter() {
        mRouter = null;
    }

    public void onDestroy() {

    }

}
