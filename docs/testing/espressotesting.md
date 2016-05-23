# Espresso Testing
There are numerous ways to conduct UI testing in Android. Within the past few
years, Google has released and spent a considerable effort developing Espresso,
a UI testing framework for Android. Compared to other testing frameworks,
Espresso being developed by Google has much better integration with Android
and the entire runtime model used by the OS. If unit testing is a big topic,
Espresso testing is even more complex. There are plenty of getting started
tutorials and documentation all over. The Android Testing Support Library page
maintained by Google and hosted at [github](https://google.github.io/android-testing-support-library/docs/espresso/setup/)
has good information on how to get started with Espresso.

# Writing Espresso Tests
Included is entry level Espresso test with enough complexity to show the power
of Espresso.

```java
@RunWith(AndroidJUnit4.class)
@LargeTest
public class CoffeeMakersTest {

    @Rule
    public ActivityTestRule<ActivityHome> mActivityRule = new ActivityTestRule(ActivityHome.class);

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
```
When this test starts the ActivityHome activity will be launched. This causes
the activity lifecycle for the ActivityHome to be initiated but the
addCoffeeMaker test won't start execution until the activity lifecycle is
completely loaded.

The test then looks for a view element on the screen that contains an id of
R.id.fab and performs a click action on it.

Clicking on the fab causes a new fragment to be loaded that has the UI
elements for adding a new coffee maker. The series of peform opertations will
type text into input box and select the home radio button option. Finally the
test clicks on the fab button which on this fragment has the behavior of
creating a new coffee maker record on the backend web service. On the Android
app as soon as the fab button is clicked the user is navigated back to the
coffee maker list view. The real power of Espresso comes out here. When the
list view fragment loads, a background thread, or specifically an AsyncTask
is created and executed to fetch the list of coffee makers from the web
service. This AsyncTask could take an arbitrary amount of time. In many test
frameworks it would be impossible to know when the background thread is
complete and the UI is updated. Often testers put sleeps in their test code
and have the sleeps wait longer than necessary to handle the situatio where the
network or service is slower than usual. Espresso, because of its tight
integration with Android, is able to recognize that an AysncTask is running
in the background and will wait to execute the final two test steps until the
AsyncTask has completed its execution. This means that no sleep code is needed
between the clicking of the fab button and the checking for specific content
on the list view activity. This is the power of Espresso.