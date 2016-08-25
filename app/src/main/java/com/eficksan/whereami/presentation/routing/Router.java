package com.eficksan.whereami.presentation.routing;

import android.os.Bundle;

/**
 * Created by Aleksei Ivshin
 * on 22.06.2016.
 */
public interface Router {

    void showScreen(int key, Bundle args);

    void closeScreen(int key);

}
