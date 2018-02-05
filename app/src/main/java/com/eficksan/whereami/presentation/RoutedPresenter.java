package com.eficksan.whereami.presentation;

import com.eficksan.whereami.presentation.routing.Router;

/**
 * Created by Aleksei_Ivshin on 9/13/16.
 */
public abstract class RoutedPresenter {

    protected Router router;

    public void takeRouter(Router router) {
        this.router = router;
    }

    public void releaseRouter() {
        router = null;
    }
}
