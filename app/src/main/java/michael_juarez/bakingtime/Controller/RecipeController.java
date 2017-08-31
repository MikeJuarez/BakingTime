package michael_juarez.bakingtime.Controller;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import michael_juarez.bakingtime.Model.Ingredient;
import michael_juarez.bakingtime.Model.Recipe;
import michael_juarez.bakingtime.Model.Step;

/**
 * This class uses the Singleton Pattern*
 * This class ultimately fetches and holds Recipe Data
 * Other classes will use this class to gain access to the data source
 */

public class RecipeController {

    private static RecipeController sRecipeController;
    private  ArrayList<Recipe> mRecipeList;
    private  String mRecipeLocation;
    private  RequestQueue mRequestQueue;
    private static JsonArrayRequest jsonArrayRequest;

    private static FinishedLoadingRecipeRequest mFinishedLoadingRecipeList;

    //Use Singleton pattern to create Controller
    public static RecipeController getInstance(Context context, String recipeLocation, FinishedLoadingRecipeRequest finishedLoadingRecipeList) {
        if (sRecipeController == null){
            sRecipeController = new RecipeController(context, recipeLocation, finishedLoadingRecipeList);
            return sRecipeController;
        }
        else
            return sRecipeController;
    }

    private RecipeController(Context context, String recipeLocation, FinishedLoadingRecipeRequest finishedLoadingRecipeList){
        mFinishedLoadingRecipeList = finishedLoadingRecipeList;
        mRequestQueue = Volley.newRequestQueue(context);
        mRecipeLocation = recipeLocation;
        mRecipeList = getRecipeListNetwork();
    }

    //Create a new recipe list from Udacity JSON file using Volley Library
    public ArrayList<Recipe> getRecipeListNetwork(){
        mRecipeList = new ArrayList<>();
        jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
            mRecipeLocation,
            null,
            new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject JO = response.getJSONObject(i);

                            String id = JO.getString("id"); //Get ID
                            String name = JO.getString("name");//Get name
                            ArrayList<Ingredient> ingredients = getIngredientsList(JO.getJSONArray("ingredients")); //Get ingredients
                            ArrayList<Step> steps = getStepsList(JO.getJSONArray("steps")); //Get steps
                            String servings = JO.getString("servings");  // Get servings
                            String image = JO.getString("image");

                            Recipe recipe = new Recipe(id, name, ingredients, steps, servings, image);
                            mRecipeList.add(recipe);
                        }
                    } catch(JSONException e){
                        e.printStackTrace();
                        return;
                    }
                    mFinishedLoadingRecipeList.finishedLoadingList(false);
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    mFinishedLoadingRecipeList.finishedLoadingList(true);
                }
            });

        mRequestQueue.add(jsonArrayRequest);

        return mRecipeList;
    }

    private ArrayList<Ingredient> getIngredientsList(JSONArray jsonArray) {
        ArrayList<Ingredient> ingredientList = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject JO = jsonArray.getJSONObject(i);
                String quantity = JO.getString("quantity");
                String measure = JO.getString("measure");
                String ingred = JO.getString("ingredient");

                Ingredient ingredient = new Ingredient(quantity, measure, ingred);
                ingredientList.add(ingredient);
            }
        } catch (JSONException e) {
            mFinishedLoadingRecipeList.finishedLoadingList(true);
        }
        return ingredientList;

    }

    private ArrayList<Step> getStepsList(JSONArray jsonArray) {
        ArrayList<Step> stepList = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject JO = jsonArray.getJSONObject(i);
                String id = JO.getString("id");
                String shortDescription = JO.getString("shortDescription");
                String description = JO.getString("description");
                String videoUrl = JO.getString("videoURL");
                String thumbnailUrl = JO.getString("thumbnailURL");

                Step step = new Step(id, shortDescription, description, videoUrl, thumbnailUrl);
                stepList.add(step);
            }
        } catch (Exception e) {
            mFinishedLoadingRecipeList.finishedLoadingList(true);
        }
        return stepList;
    }

    public static void cancelJsonRequest() {
        if (jsonArrayRequest != null && !jsonArrayRequest.hasHadResponseDelivered())
            jsonArrayRequest.cancel();
    }

    //Return the populed recipe list
    public ArrayList<Recipe> getRecipeList() {
        return mRecipeList;
    }

    public interface FinishedLoadingRecipeRequest {
        void finishedLoadingList(boolean hadError);
    }
}
