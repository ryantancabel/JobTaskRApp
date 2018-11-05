package ict376.murdoch.edu.au.jobtaskrapp;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentContainer;
import android.app.Instrumentation;
import android.support.test.rule.ActivityTestRule;

import com.android21buttons.fragmenttestrule.FragmentTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

public class LoginFragmentTest {

    @Rule
    public FragmentTestRule<?, LoginFragment> fragmentTestRule =
            FragmentTestRule.create(LoginFragment.class);

    private android.support.v4.app.Fragment mloginFragment = null;

    Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(SidePanelActivity.class.getName(),null,false);

    @Before
    public void setUp() throws Exception{

        mloginFragment = fragmentTestRule.getFragment();
    }

    @Test
    public void clickButtonLogIn() {

        assertNotNull(mloginFragment.getActivity());

        Activity slidepanelActivity = getInstrumentation().waitForMonitorWithTimeout(monitor,5000);

        assertNotNull(slidepanelActivity);

        slidepanelActivity.finish();
        //onView(withText(R.string.login)).check(matches(isDisplayed()));
    }

    @After
    public void tearDown() throws Exception
    {
        mloginFragment = null;
    }


}