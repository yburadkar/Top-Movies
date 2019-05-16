package com.yb.uadnd.popularmovies;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.yb.uadnd.popularmovies.ui.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withResourceName;
import static androidx.test.espresso.matcher.ViewMatchers.withSubstring;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
public class MainActivityUiTest {
    private static final String LOG_TAG = MainActivityUiTest.class.getSimpleName();
    private MyApp mApp;
    private SimpleIdlingResource mResource;

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(
            MainActivity.class, true, false);

    @Before
    public void setUpTest(){
        mResource = MyApp.getmIdlingResource();
        IdlingRegistry.getInstance().register(mResource);
    }

    @After
    public void cleanUpTest(){
        IdlingRegistry.getInstance().unregister(mResource);
    }

    @Test
    public void checkAppBarTitleDisplay(){
        activityTestRule.launchActivity(null);
        Context context =InstrumentationRegistry.getInstrumentation().getTargetContext();
        String title = context.getString(R.string.popular_movies);

        //Check if title reflects the default start mode Popular
        onView(allOf(isAssignableFrom(TextView.class), withParent(withResourceName("action_bar"))))
                .check(matches(withText(title)));

        //Select Top Rated from the options menu and confirm if the Appbar title changes to match
        openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext());
        title = context.getString(R.string.top_rated_movies);
        onView(withText(title)).perform(click());
        onView(allOf(isAssignableFrom(TextView.class), withParent(withResourceName("action_bar"))))
                .check(matches(withText(title)));

        //Select Favorites from the options menu and confirm if the Appbar title changes to match
        openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext());
        title = context.getString(R.string.your_favorites);
        onView(withText(title)).perform(click());
        onView(allOf(isAssignableFrom(TextView.class), withParent(withResourceName("action_bar"))))
                .check(matches(withText(title)));
    }

    @Test
    public void checkIfMoreMoviesAutoLoadOnScroll(){
        activityTestRule.launchActivity(null);
        onView(withId(R.id.recyclerview))
                .perform(RecyclerViewActions.scrollToPosition(15));
        onView(withId(R.id.recyclerview))
                .perform(RecyclerViewActions.scrollToPosition(25));
        onView(withSubstring("  25")).check(matches(isDisplayed()));
    }

}
