package michael_juarez.bakingtime;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
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
import michael_juarez.bakingtime.Model.Ingredient;

/**
 * Created by user on 8/26/2017.
 */

public class StepIngredients_Fragment extends Fragment {
    public static String STEP_INGREDIENTS_KEY_POSITION = "michael_juarez.baketime.step_ingredients_fragment.key_position";

    @BindView(R.id.step_ingredients_rv)
    RecyclerView mRecyclerView;

    private RecipeController mRecipeController;
    private int mPosition;
    private ArrayList mIngredientsList;
    private Unbinder unbinder;

    private RecyclerView.Adapter mAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.step_ingredients_fragment, container, false);
        setRetainInstance(true);
        unbinder = ButterKnife.bind(this, view);

        mPosition = getArguments().getInt(STEP_INGREDIENTS_KEY_POSITION);

        mRecipeController = RecipeController.getInstance(getActivity(), "", null);

        mIngredientsList = (ArrayList) mRecipeController.getRecipeList().get(mPosition).getIngredients();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        mAdapter = new IngredientsAdapter(mIngredientsList);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder> {

        private ArrayList mAdapterIngredientsList;

        public IngredientsAdapter(ArrayList ingredientsList) {
            mAdapterIngredientsList = ingredientsList;
        }

        @Override
        public IngredientsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new IngredientsViewHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(IngredientsViewHolder holder, int position) {
            holder.bind((Ingredient) mAdapterIngredientsList.get(position));
        }

        @Override
        public int getItemCount() {
            return mAdapterIngredientsList.size();
        }

        public class IngredientsViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.list_item_ingredient_name_tv)
            TextView mIngredientNameTextView;
            @BindView(R.id.list_item_ingredient_quantity_tv)
            TextView mIngredientQuantity;
            @BindView(R.id.list_item_ingredient_measure_tv)
            TextView mIngredientMeasure;

            public IngredientsViewHolder(LayoutInflater inflater, ViewGroup parent) {
                super(inflater.inflate(R.layout.list_item_ingredient, parent, false));
                ButterKnife.bind(this, itemView);
            }

            public void bind(Ingredient ingredient) {
                mIngredientNameTextView.setText(ingredient.getIngredient());
                mIngredientQuantity.setText(ingredient.getQuantity());
                mIngredientMeasure.setText(ingredient.getMeasure());

            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
