package aka.heyden.memorizeapp.util;

import android.content.Context;
import android.content.Intent;

import aka.heyden.memorizeapp.R;
import aka.heyden.memorizeapp.presenter.ScreenController;
import aka.heyden.memorizeapp.view.EditBackgroundActivity;

/**
 * Created by Han In-Gyu on 2016-07-27.<br><br>
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

    public static void disableClockScreen(Context mContext){
        CustomIntent intent = new CustomIntent(mContext.getResources().getString(R.string.unlock_screen));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    public static void openColorPicker(Context mContext){
        CustomIntent intent = new CustomIntent(mContext, EditBackgroundActivity.class);
        intent.putExtra("type", Constant.CUSTOM_COLOR);
        mContext.startActivity(intent);
    }
}
