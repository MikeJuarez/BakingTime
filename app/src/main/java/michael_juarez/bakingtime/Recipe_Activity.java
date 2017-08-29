package michael_juarez.bakingtime;



import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import java.util.zip.Inflater;

import butterknife.BindView;
import butterknife.ButterKnife;
import michael_juarez.bakingtime.Controller.RecipeController;
import michael_juarez.bakingtime.Controller.ScreenSizeController;

/*
    Author: Michael Juarez
    This class will create the first screen the user will see.
    It holds a list of recipes.
    Users will be able to click the recipes, then move to the next screen.
    The recipes are pulled from a JSON file hosted by Udacity
 */

public class Recipe_Activity extends AppCompatActivity {

    //Find main container to hold the fragment and assign variable
    //private FrameLayout mContainer;

    private boolean mIsTablet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_activity);

        //Check if this device is a tablet
        if (findViewById(R.id.recipe_container_tablet_left) != null)
            mIsTablet = true;
        else
            mIsTablet = false;

        //ButterKnife.bind(this);

        FragmentManager fm = getSupportFragmentManager();
        Fragment recipeFragment = new Recipe_Fragment();

        //If this device is a tablet, then load recipe fragment into tablet container
        if (mIsTablet) {
            ScreenSizeController.getInstance(this, mIsTablet, R.id.recipe_container_tablet_right);
            Fragment recipeTabletLeft = new Recipe_Tablet_Left_Fragment();
            fm.beginTransaction()
                    .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                    .replace(R.id.recipe_container_tablet_left, recipeTabletLeft)
                    .commit();

            fm.beginTransaction()
                    .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                    .replace(R.id.recipe_container_tablet_right, recipeFragment)
                    .commit();
        }
        //If this device is not a tablet, then load recipe fragment into phone container
        else {
            ScreenSizeController.getInstance(this, mIsTablet,R.id.recipe_container);
            //Load the recipe fragment
            fm.beginTransaction()
                    .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                    .replace(R.id.recipe_container, recipeFragment)
                    .commit();
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

        if (mIsTablet) {
            Fragment rightSideFragment = getSupportFragmentManager().findFragmentById(R.id.recipe_container_tablet_right);

            if (rightSideFragment instanceof Recipe_Fragment) {
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                        .replace(R.id.recipe_container_tablet_left, new Recipe_Tablet_Left_Fragment())
                        .commit();
            }
        }

    }

}
