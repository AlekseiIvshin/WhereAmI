package com.eficksan.whereami.presentation.routing;

import android.os.Bundle;

/**
 * Applicatoin router interface.
 *
 * Created by Aleksei Ivshin
 * on 22.06.2016.
 */
public interface Router {

    /**
     * Show screen.
     * @param nextScreenKey screen key
     * @param args arguments for next screen
     */
    void showScreen(int nextScreenKey, Bundle args);

    /**
     * Close screen.
     * @param key screen key
     */
    void closeScreen(int key);

}
