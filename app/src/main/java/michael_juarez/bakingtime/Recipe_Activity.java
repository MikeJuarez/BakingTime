package michael_juarez.bakingtime;



import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
    Author: Michael Juarez
    This class will create the first screen the user will see.
    It holds a list of recipes.
    Users will be able to click the recipes, then move to the next screen.
    The recipes are pulled from a JSON file hosted by Udacity
 */

public class Recipe_Activity extends AppCompatActivity {

    //Find main container to hold the fragment and assign variable
    @BindView(R.id.recipe_container) FrameLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_activity);
        ButterKnife.bind(this);

        FragmentManager fm = getSupportFragmentManager();
        Fragment recipeFragment = new Recipe_Fragment();

        //Load the recipe fragment
        fm.beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                .replace(R.id.recipe_container, recipeFragment)
                .commit();
    }
}
