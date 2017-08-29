package michael_juarez.bakingtime.Controller;

import android.content.Context;

/**
 * Created by user on 8/22/2017.
 */

public class ScreenSizeController {

    private static ScreenSizeController sScreenSizeController;
    private boolean mIsTablet;
    private int mContainer;

    public static ScreenSizeController getInstance(Context context, boolean isTablet, int container) {
        if (sScreenSizeController == null){
            sScreenSizeController = new ScreenSizeController(context, isTablet, container);
            return sScreenSizeController;
        }
        else
            return sScreenSizeController;
    }

    private ScreenSizeController(Context context, boolean isTablet, int container){
        mIsTablet = isTablet;
        mContainer = container;
    }

    public boolean getIsTablet() {
        return mIsTablet;
    }

    public int getContainer() {
        return mContainer;
    }
}
