package michael_juarez.bakingtime;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import michael_juarez.bakingtime.Controller.RecipeController;
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
    Adapter mAdapter;

    public Recipe_Fragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.recipe_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecipeRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new RecipeAdapter(new ArrayList<Recipe>());
        mRecipeRecyclerView.setAdapter(mAdapter);

        mRecipeController = RecipeController.getInstance(getActivity(), getResources().getString(R.string.recipe_location), this);
        if (mRecipeController.getRecipeList() != null)
            finishedLoadingList();

        return view;
    }

    @Override
    public void finishedLoadingList() {
        mAdapter = new RecipeAdapter(mRecipeController.getRecipeList());
        mRecipeRecyclerView.setAdapter(mAdapter);
    }

    //Define the RecyclerView's Adapter
    public class RecipeAdapter extends RecyclerView.Adapter<RecipeViewHolder> {
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
            holder.bind(mRecipeList.get(position).getId(), mRecipeList.get(position).getName(),
                        mRecipeList.get(position).getServings() + " servings." + "\n" +
                        mRecipeList.get(position).getSteps().size() + " steps to create this recipe.");
        }

        @Override
        public int getItemCount() {
            return mRecipeList.size();
        }
    }

    public class RecipeViewHolder extends ViewHolder {
        @BindView(R.id.recipe_name_tv) TextView mRecipeNameTextView;
        @BindView(R.id.recipe_steps_tv) TextView mRecipeStepsTextView;

        public RecipeViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_recipe, parent, false));
            ButterKnife.bind(this, itemView);
        }

        public void bind(String recipeId, String recipeName, String recipeStepsCount) {
            mRecipeNameTextView.setText(recipeName);
            mRecipeStepsTextView.setText(recipeStepsCount);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
