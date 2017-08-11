package michael_juarez.bakingtime.Model;

import java.util.List;

/**
 * Created by user on 8/7/2017.
 */

public class Recipe {
    private String mId;
    private String mName;
    private List<Ingredient> mIngredients;
    private List<Step> mSteps;
    private String mServings;
    //private String mImage;

    public Recipe(String id, String name, List<Ingredient> ingredients, List<Step> steps, String servings) {
        mId = id;
        mName = name;
        mIngredients = ingredients;
        mSteps = steps;
        mServings = servings;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public List<Ingredient> getIngredients() {
        return mIngredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        mIngredients = ingredients;
    }

    public List<Step> getSteps() {
        return mSteps;
    }

    public void setSteps(List<Step> steps) {
        mSteps = steps;
    }

    public String getServings() {
        return mServings;
    }

    public void setServings(String servings) {
        mServings = servings;
    }

/*    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        mImage = image;
    }*/
}
