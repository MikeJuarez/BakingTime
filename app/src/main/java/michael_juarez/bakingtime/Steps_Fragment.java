package michael_juarez.bakingtime;

import android.app.Fragment;
import android.os.Bundle;
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

/**
 * Created by user on 8/10/2017.
 */

public class Steps_Fragment extends Fragment implements RecipeController.FinishedLoadingRecipeRequest {

    //Assign the fragment's RecyclerView to a variable using ButterKnife Library
    @BindView(R.id.recipe_rv) RecyclerView mRecipeRecyclerView;

    private Unbinder unbinder;
    RecipeController mRecipeController;
    RecyclerView.Adapter mAdapter;

    public Steps_Fragment(){}

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
        /*if (mRecipeController.getRecipeList() != null)
            finishedLoadingList();*/

        return view;
    }

    @Override
    public void finishedLoadingList() {
        /*mAdapter = new RecipeAdapter(mRecipeController.getRecipeList());
        mRecipeRecyclerView.setAdapter(mAdapter);*/
    }

    //Define the RecyclerView's Adapter
    public class RecipeAdapter extends RecyclerView.Adapter<StepViewHolder> {
        ArrayList<Recipe> mStepList;

        public RecipeAdapter(ArrayList<Recipe> stepList) {
            mStepList = stepList;
        }

        @Override
        public StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new StepViewHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(StepViewHolder holder, int position) {
            holder.bind(mStepList.get(position).getId(), mStepList.get(position).getName(),
                    mStepList.get(position).getServings() + " servings." + "\n" +
                            mStepList.get(position).getSteps().size() + " steps to create this recipe.");
        }

        @Override
        public int getItemCount() {
            return mStepList.size();
        }
    }

    public class StepViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.recipe_name_tv)
        TextView mRecipeNameTextView;
        @BindView(R.id.recipe_steps_tv) TextView mRecipeStepsTextView;

        public StepViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_step, parent, false));
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


