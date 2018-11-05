package ict376.murdoch.edu.au.jobtaskrapp;

import android.support.test.rule.ActivityTestRule;
import android.support.v4.app.Fragment;
import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    private MainActivity mActivity = null;


    @Before
    public void setUp() throws Exception {

        mActivity = mActivityTestRule.getActivity();

    }
    @Test
    public void testLaunch()
    {

        View view = mActivity.findViewById(R.id.lgareaFragment);

        assertNotNull(view);
    }


    @After
    public void tearDown() throws Exception {
        mActivity = null;

    }
}