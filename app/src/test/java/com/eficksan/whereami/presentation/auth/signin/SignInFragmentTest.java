package com.eficksan.whereami.presentation.auth.signin;

import com.eficksan.whereami.BuildConfig;
import com.eficksan.whereami.R;
import com.eficksan.whereami.ShadowApp;
import com.eficksan.whereami.presentation.MainActivity;

import org.hamcrest.core.IsNull;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static org.hamcrest.core.Is.is;


/**
 * Created by Aleksei_Ivshin on 9/12/16.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, application = ShadowApp.class, sdk = 23)
public class SignInFragmentTest {

    @Test
    @Ignore
    public void shouldCallPresentersOnStartAndOnStop() {
        // Given
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        ((ShadowApp) RuntimeEnvironment.application).plusActivityComponent(mainActivity);
        SignInFragment fragment = SignInFragment.newInstance();

        // When
        mainActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();

        // Then
        Assert.assertThat(fragment.getView(), is(IsNull.notNullValue()));
    }
}
