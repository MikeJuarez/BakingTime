package michael_juarez.bakingtime;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import michael_juarez.bakingtime.Controller.RecipeController;
import michael_juarez.bakingtime.Model.Step;

import static michael_juarez.bakingtime.Steps_Fragment.KEY_POSITION;

/**
 Step Pattern:
 private String mId;
 private String mShortDescription;
 private String mVideoUrl;
 private String mThumbnailUrl;
 */

public class Step_Details_Fragment extends Fragment implements RecipeController.FinishedLoadingRecipeRequest{

    //Assign the fragment's RecyclerView to a variable using ButterKnife Library
    @BindView(R.id.step_details_instruction_tv) TextView mInstructionTextView;
    @BindView(R.id.step_details_previous_button) Button mPreviousButton;
    @BindView(R.id.step_details_next_button) Button mNextButton;

    private Unbinder unbinder;
    RecipeController mRecipeController;
    private int mStepPosition;
    private int mStepDetailPosition;
    private Step mStep;

    public static final String KEY_POSITION = "michael_juarez.baketime.steps_details_fragment.key_position";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.step_details_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        mRecipeController = RecipeController.getInstance(getActivity(), getResources().getString(R.string.recipe_location), this);

        //Get mStepPosition from bundle passed in.
        mStepPosition = getArguments().getInt(Steps_Fragment.KEY_POSITION);
        mStepDetailPosition = getArguments().getInt(Step_Details_Fragment.KEY_POSITION);

        if (mStepPosition < 0)
            showError();

        mStep = mRecipeController.getRecipeList().get(mStepPosition).getSteps().get(mStepDetailPosition);
        updateUi();

        return view;
    }

    private void updateUi() {
        mInstructionTextView.setText(mStep.getShortDescription());
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

    @Override
    public void finishedLoadingList() {}
}


