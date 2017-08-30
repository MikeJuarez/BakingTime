package michael_juarez.bakingtime;

import android.app.Activity;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;

import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anyOf;

/**
 * Created by Michael Juarez on 8/29/2017.
 */

@RunWith(AndroidJUnit4.class)
public class ClickRecipeLoadStepsTest {

    @Rule
    public ActivityTestRule<Recipe_Activity> mActivityTestRule = new ActivityTestRule<>(Recipe_Activity.class);

    @Test
    public void clickHomeButton_LoadsRecipeFragment() {

        onView(withId(R.id.app_bar_home)).perform(click());

        onView(withId(R.id.recipe_rv)).check(matches(isDisplayed()));
    }

    @Test
    public void clickRecipe_LoadFirstFourRecipeFirstStep()  {
        for (int i = 0; i < 4; i++) {
            //Click first item in list
            onView(withId(R.id.recipe_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(i, click()));

            //Click instructions
            onView(withId(R.id.step_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

            clickHomeButton_LoadsRecipeFragment();
        }
    }

    @Test
    public void clickRecipe_LoadFirstFourRecipeInstructions()  {
        for (int i = 0; i < 4; i++) {
            onView(withId(R.id.recipe_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(i, click()));

            //Click first item in list
            onView(withId(R.id.step_fragment_ingredient_ll)).perform(click());

            clickHomeButton_LoadsRecipeFragment();
        }
    }


    //  Checks to make sure previous button is hidden on first step
    //  and next button his hidden on last step
    @Test
    public void checkNextButtonsRecipe1() {
        onView(withId(R.id.recipe_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        //Click instructions
        onView(withId(R.id.step_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        //Click next button
        for (int i = 0; i < 6; i++) {
            if (i == 6) {
                onView(withId(R.id.step_details_next_button)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
                return;
            }
            //Make sure previous button his hidden on first step
            if (i == 0)
                onView(withId(R.id.step_details_previous_button)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));

            onView(withId(R.id.step_details_next_button)).perform(click());
        }
    }

    //  Checks to make sure previous button is hidden on first step
    //  and next button his hidden on last step
    @Test
    public void checkPreviousButtonsRecipe1() {
        onView(withId(R.id.recipe_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        //Click instructions
        onView(withId(R.id.step_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(6, click()));

        //Click next button
        for (int i = 6; i > 0; i--) {
            //Make sure previous button his hidden on first step
            if (i == 0) {
                onView(withId(R.id.step_details_previous_button)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
                return;
            }

            if (i == 6) {
                onView(withId(R.id.step_details_next_button)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));                ;
            }

            onView(withId(R.id.step_details_previous_button)).perform(click());
        }
    }

}
