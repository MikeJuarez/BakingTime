package michael_juarez.bakingtime;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import michael_juarez.bakingtime.Controller.ScreenSizeController;

/*
    Author: Michael Juarez
    This class will create the first screen the user will see.
    It holds a list of recipes.
    Users will be able to click the recipes, then move to the next screen.
    The recipes are pulled from a JSON file hosted by Udacity
 */

public class Recipe_Activity extends AppCompatActivity {

    @BindView(R.id.recipe_activity_toolbar)
    Toolbar mToolBar;

    private boolean mIsTablet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_activity);
        ButterKnife.bind(this);

        //Toolbar Setup
        setSupportActionBar(mToolBar);

        //Check if this device is a tablet
        if (findViewById(R.id.recipe_container_tablet_left) != null)
            mIsTablet = true;
        else
            mIsTablet = false;


        FragmentManager fm = getSupportFragmentManager();
        Fragment recipeFragment = new Recipe_Fragment();

        homeClick();
    }

    @Override
    public void onBackPressed() {
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

    @OnClick(R.id.app_bar_home)
    public void homeClick() {

        FragmentManager fm = getSupportFragmentManager();
        Fragment recipeFragment = new Recipe_Fragment();

        //If this device is a tablet, then load recipe fragment into tablet container
        if (mIsTablet) {
            ScreenSizeController.getInstance(this, mIsTablet, R.id.recipe_container_tablet_right);
            Fragment recipeTabletLeft = new Recipe_Tablet_Left_Fragment();
            fm.beginTransaction()
                    .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                    .replace(R.id.recipe_container_tablet_left, recipeTabletLeft)
                    .commitNow();

            fm.beginTransaction()
                    .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                    .replace(R.id.recipe_container_tablet_right, recipeFragment)
                    .commitNow();
        }

        //If this device is not a tablet, then load recipe fragment into phone container
        ScreenSizeController.getInstance(this, mIsTablet, R.id.recipe_container);
        //Load the recipe fragment
        fm.beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                .replace(R.id.recipe_container, recipeFragment)
                .commitNow();

    }

}
