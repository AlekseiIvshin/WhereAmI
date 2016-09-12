package com.eficksan.whereami.presentation;

import com.eficksan.whereami.BuildConfig;
import com.eficksan.whereami.R;
import com.eficksan.whereami.ShadowApp;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

/**
 * Created by Aleksei_Ivshin on 9/12/16.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, application = ShadowApp.class)
public class MainActivityTest {

    @Test
    public void shouldSetupActivity() {
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        Assert.assertNotNull(mainActivity.findViewById(R.id.fragment_container));
    }

}
