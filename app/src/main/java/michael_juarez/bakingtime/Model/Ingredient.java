package michael_juarez.bakingtime.Model;

/**
 * This class holds the pattern for ingredients in the recipe
 */

public class Ingredient {
    private String mQuantity;
    private String mMeasure;
    private String mIngredient;

    public Ingredient(String quantity, String measure, String ingredient) {
        mQuantity = quantity;
        mMeasure = measure;
        mIngredient = ingredient;
    }

    public String getQuantity() {
        return mQuantity;
    }

    public void setQuantity(String quantity) {
        mQuantity = quantity;
    }

    public String getMeasure() {
        return mMeasure;
    }

    public void setMeasure(String measure) {
        mMeasure = measure;
    }

    public String getIngredient() {
        return mIngredient;
    }

    public void setIngredient(String ingredient) {
        mIngredient = ingredient;
    }
}
