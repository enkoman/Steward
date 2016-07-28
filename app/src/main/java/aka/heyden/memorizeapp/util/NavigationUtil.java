package aka.heyden.memorizeapp.util;

import android.content.Context;
import aka.heyden.memorizeapp.model.ScreenController;

/**
 * Created by N4047 on 2016-07-27.
 */

public class NavigationUtil {
    public static void startScreenController(Context mContext){
        CustomIntent intent = new CustomIntent(mContext, ScreenController.class);
        mContext.startService(intent);
    }

    public static void stopScreenController(Context mContext){
        CustomIntent intent = new CustomIntent(mContext, ScreenController.class);
        mContext.stopService(intent);
    }
}
