package coffeenow.com.coffeenowapp;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

import coffeenow.com.coffeenowapp.activities.ActivityHome;
import coffeenow.com.coffeenowapp.models.CoffeeMaker;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class CoffeeMakersTest {

    @Rule
    public ActivityTestRule<ActivityHome> mActivityRule = new ActivityTestRule(ActivityHome.class);

    @Test
    public void headingAppears() {
        onView(withText("Coffee makers")).check(matches(isDisplayed()));
    }

    @Test
    public void fabAppears() {
        onView(withId(R.id.fab)).check(matches(isDisplayed()))
                .check(matches(isClickable()));
    }

    @Test
    public void addCoffeeMaker() {
        String uniqueID = UUID.randomUUID().toString();
        onView(withId(R.id.fab)).perform(click());

        onView(withText("Add a new coffee maker")).check(matches(isDisplayed()));

        onView(withId(R.id.create_coffee_maker_name)).perform(typeText(uniqueID));
        onView(withId(R.id.create_coffee_maker_location_home)).perform(click());
        onView(withId(R.id.create_coffee_maker_volume)).perform(typeText("12"));
        onView(withId(R.id.fab)).perform(click());

        onView(withText("Coffee makers")).check(matches(isDisplayed()));
        onData(allOf(is(instanceOf(CoffeeMaker.class)), hasSibling(withText(uniqueID))));
    }
}
