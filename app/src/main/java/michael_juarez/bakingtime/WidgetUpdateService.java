package michael_juarez.bakingtime;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by user on 8/30/2017.
 */

public class WidgetUpdateService extends IntentService {

    public static final String ACTION_WIDGET_RECIPE_UPDATE = "com.bakingtime.michael_juarez.action.update_widget";
    public static final String EXTRA_WIDGET_INGREDIENTS = "com.bakingtime.michael_juarez.extra.widget_ingredients";

    public WidgetUpdateService() {
        super("michael_juarez.bakingtime.WidgetUpdateService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_WIDGET_RECIPE_UPDATE.equals(action)) {
                String ingredientList = intent.getStringExtra(EXTRA_WIDGET_INGREDIENTS);
                handleActionUpdateWidgetRecipe(ingredientList);
            }
        }
    }

    public static void startWidgetUpdate(Context context, String ingredientList) {
        Intent intent = new Intent(context, WidgetUpdateService.class);
        intent.putExtra(EXTRA_WIDGET_INGREDIENTS, ingredientList);
        intent.setAction(ACTION_WIDGET_RECIPE_UPDATE);
        context.startService(intent);
    }

    /*
     *  Handle action in the provided backgroudn thread with the provided parameters
     */
    private void handleActionUpdateWidgetRecipe(String ingredientList) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingTimeWidgetProvider.class));

        BakingTimeWidgetProvider.updateRecipeAppWidgets(this, appWidgetManager, ingredientList, appWidgetIds);
    }
}
