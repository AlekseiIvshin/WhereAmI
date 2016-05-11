package com.eficksan.whereami;

import android.app.Application;

import com.eficksan.whereami.ioc.Graph;

/**
 * Created by Aleksei Ivshin
 * on 25.04.2016.
 */
public class App extends Application {

    private Graph mGraph;

    @Override
    public void onCreate() {
        super.onCreate();

        mGraph = Graph.Initializer.init(this);
    }

    public Graph getObjectGraph() {
        return mGraph;
    }
}
