package michael_juarez.bakingtime;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import michael_juarez.bakingtime.Controller.RecipeController;
import michael_juarez.bakingtime.Model.Step;

/**
 Step Pattern:
 private String mId;
 private String mShortDescription;
 private String mVideoUrl;
 private String mThumbnailUrl;
 */

public class Steps_Fragment extends Fragment implements RecipeController.FinishedLoadingRecipeRequest {

    //Assign the fragment's RecyclerView to a variable using ButterKnife Library
    @BindView(R.id.step_ingredient_tv) TextView mIngredientTextView;
    @BindView(R.id.step_rv) RecyclerView mRecipeRecyclerView;

    private Unbinder unbinder;
    RecipeController mRecipeController;
    RecyclerView.Adapter mAdapter;
    int position;
    ArrayList<Step> mStepsList;

    public static final String KEY_POSITION = "michael_juarez.baketime.steps_fragment.key_position";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.step_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        //Get mStepPosition from bundle passed in.
        position = getArguments().getInt(KEY_POSITION);
        if (position < 0 )
            showError();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecipeRecyclerView.setLayoutManager(layoutManager);

        mRecipeController = RecipeController.getInstance(getActivity(), getResources().getString(R.string.recipe_location), this);
        if (mRecipeController == null)
            showError();

        mStepsList = (ArrayList) mRecipeController.getRecipeList().get(position).getSteps();
        if (mStepsList == null)
            showError();

        mAdapter = new StepAdapter(mStepsList);
        mRecipeRecyclerView.setAdapter(mAdapter);

        mIngredientTextView.setText(R.string.ingredients);

        return view;
    }

    @Override
    public void finishedLoadingList() {}

    //Define the RecyclerView's Adapter
    public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {
        ArrayList<Step> mStepList;

        public StepAdapter(ArrayList<Step> stepList) {
            mStepList = stepList;
        }

        @Override
        public StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new StepViewHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(StepViewHolder holder, int position) {
            holder.bind(mStepList.get(position).getShortDescription());
        }

        @Override
        public int getItemCount() {
            return mStepList.size();
        }

        public class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            @BindView(R.id.list_item_step_description) TextView mStepDescription;
            @BindView(R.id.step_number) TextView mStepNumber;

            public StepViewHolder(LayoutInflater inflater, ViewGroup parent) {
                super(inflater.inflate(R.layout.list_item_step, parent, false));
                ButterKnife.bind(this, itemView);
                itemView.setOnClickListener(this);
            }

            public void bind(String stepDescription) {
                String stepNumber = getActivity().getResources().getString(R.string.step_number);
                stepNumber = stepNumber + (getAdapterPosition() + 1) + ": ";
                mStepDescription.setText(stepDescription);
                mStepNumber.setText(stepNumber);
            }

            @Override
            public void onClick(View v) {
                //Get the mStepPosition clicked
                int clickedPosition = getAdapterPosition();

                //Store the mStepPosition clicked in a new bundle
                Bundle bundle = new Bundle();
                bundle.putInt(Steps_Fragment.KEY_POSITION, position);
                bundle.putInt(Step_Details_Fragment.KEY_POSITION, clickedPosition);

                //Store the bundle inside a new Steps_Fragment
                Fragment stepDetailsFragment = new Step_Details_Fragment();
                stepDetailsFragment.setArguments(bundle);
                getFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                        .replace(R.id.recipe_container, stepDetailsFragment)
                        .addToBackStack(null)
                        .commit();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    //TODO show error
    private void showError(){
        Log.d("STEPS_FRAGMENT", "showError() called.");
    }
}


