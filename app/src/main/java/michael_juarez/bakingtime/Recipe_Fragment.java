package michael_juarez.bakingtime;


import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import michael_juarez.bakingtime.Controller.RecipeController;
import michael_juarez.bakingtime.Controller.ScreenSizeController;
import michael_juarez.bakingtime.Model.Recipe;

import static android.support.v7.widget.RecyclerView.*;


/**
 * Created by user on 8/7/2017.
 */

public class Recipe_Fragment extends Fragment implements RecipeController.FinishedLoadingRecipeRequest {

    //Assign the fragment's RecyclerView to a variable using ButterKnife Library
    @BindView(R.id.recipe_rv) RecyclerView mRecipeRecyclerView;
    private Unbinder unbinder;

    RecipeController mRecipeController;

    private Adapter mAdapter;
    private boolean mIsTablet;
    private int mContainer;

    public Recipe_Fragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.recipe_fragment, container, false);
        setRetainInstance(true);
        unbinder = ButterKnife.bind(this, view);

        //Check if this device is a tablet
        mIsTablet = ScreenSizeController.getInstance(getActivity(), false, 0).getIsTablet();
        mContainer = ScreenSizeController.getInstance(getActivity(), false, 0).getContainer();

        setUpLayoutManager();

        mAdapter = new RecipeAdapter(new ArrayList<Recipe>());
        mRecipeRecyclerView.setAdapter(mAdapter);

        mRecipeController = RecipeController.getInstance(getActivity(), getResources().getString(R.string.recipe_location), this);
        if (mRecipeController.getRecipeList() != null)
            finishedLoadingList();

        return view;
    }

    private void setUpLayoutManager() {
        //If device is in portrait, both phone and tablet, layout manager is 1 column and vertical
        if (getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            mRecipeRecyclerView.setLayoutManager(layoutManager);
            return;
        }
        //If device is landscape mode and is a tablet, then layout manager is 3 columns and vertical
        else if (mIsTablet) {
            GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
            mRecipeRecyclerView.setLayoutManager(layoutManager);
        }
        //If device is landscape mode and is not a tablet, then layoutmanager is 2 columns
        else {
            GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
            mRecipeRecyclerView.setLayoutManager(layoutManager);
        }
    }
    @Override
    public void finishedLoadingList() {
        mAdapter = new RecipeAdapter(mRecipeController.getRecipeList());
        mRecipeRecyclerView.setAdapter(mAdapter);
    }

    //Define the RecyclerView's Adapter
    public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
        ArrayList<Recipe> mRecipeList;

        public RecipeAdapter(ArrayList<Recipe> recipeList) {
            mRecipeList = recipeList;
        }

        @Override
        public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new RecipeViewHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(RecipeViewHolder holder, int position) {
            holder.bind(mRecipeList.get(position).getId(), mRecipeList.get(position).getName());
        }

        @Override
        public int getItemCount() {
            return mRecipeList.size();
        }

        public class RecipeViewHolder extends ViewHolder implements View.OnClickListener {
            @BindView(R.id.recipe_name_tv) TextView mRecipeNameTextView;

            public RecipeViewHolder(LayoutInflater inflater, ViewGroup parent) {
                super(inflater.inflate(R.layout.list_item_recipe, parent, false));
                ButterKnife.bind(this, itemView);
                itemView.setOnClickListener(this);
            }

            public void bind(String recipeId, String recipeName){
                mRecipeNameTextView.setText(recipeName);
            }

            @Override
            public void onClick(View v) {
                //Get the mStepPosition clicked
                int clickedPosition = getAdapterPosition();

                //Store the mStepPosition clicked in a new bundle
                Bundle bundle = new Bundle();
                bundle.putInt(Steps_Fragment.KEY_POSITION, clickedPosition);

                //Store the bundle inside a new Steps_Fragment
                Fragment stepsFragment = new Steps_Fragment();
                stepsFragment.setArguments(bundle);

                if (mIsTablet) {
                    Bundle stepDetailsBundle = new Bundle();
                    stepDetailsBundle.putInt(StepDetails_Fragment.KEY_POSITION, 0);

                    Fragment stepDetailsFragment = new StepDetails_Fragment();
                    stepDetailsFragment.setArguments(stepDetailsBundle);

                    //Load fragment into left pane
                    getFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                            .replace(R.id.recipe_container_tablet_left, stepsFragment)
                            .addToBackStack(null)
                            .commit();

                    //Load fragment into right pane
                    getFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                            .replace(R.id.recipe_container_tablet_right, stepDetailsFragment)
                            .addToBackStack(null)
                            .commit();
                }
                else {
                    getFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                            .replace(R.id.recipe_container, stepsFragment)
                            .addToBackStack(null)
                            .commit();
                }

            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setUpLayoutManager();
    }
}
