package michael_juarez.bakingtime;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.ButterKnife;
import michael_juarez.bakingtime.Controller.RecipeController;
import michael_juarez.bakingtime.Controller.ScreenSizeController;
import michael_juarez.bakingtime.Model.Recipe;

/**
 * Created by user on 8/22/2017.
 */

public class Recipe_Tablet_Left_Fragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setRetainInstance(true);
        View view = inflater.inflate(R.layout.recipe_tablet_left_fragment, container, false);
        return view;
    }
}
