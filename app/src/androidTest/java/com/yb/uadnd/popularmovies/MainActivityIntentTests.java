package com.yb.uadnd.popularmovies;

import android.app.Activity;
import android.app.Instrumentation.ActivityResult;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.yb.uadnd.popularmovies.ui.MainActivity;
import com.yb.uadnd.popularmovies.ui.MovieDetailActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static androidx.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
public class MainActivityIntentTests {

    @Rule
    public IntentsTestRule<MainActivity> mIntentsTestRule = new IntentsTestRule<>(MainActivity.class);

    @Before
    public void stubAllExternalIntents(){
        intending(not(isInternal())).respondWith(new ActivityResult(Activity.RESULT_OK, null));
    }

    @Test
    public void movieItem_click_opensMovieDetail(){
        onView(withId(R.id.recyclerview))
                .perform(click());

        intended(allOf(
                hasExtraWithKey(MovieDetailActivity.EXTRA_JSON),
                hasComponent(MovieDetailActivity.class.getName())
        ));
    }
}
