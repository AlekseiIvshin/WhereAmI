package rx.plugins;

import rx.plugins.RxJavaPlugins;

/**
 * Created by Aleksei Ivshin
 * on 11.09.2016.
 */
public class RxJavaTestPlugins extends RxJavaPlugins {
    RxJavaTestPlugins() {
        super();
    }

    public static void resetPlugins(){
        getInstance().reset();
    }
}
