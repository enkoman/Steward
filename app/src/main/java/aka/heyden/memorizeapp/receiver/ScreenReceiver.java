package aka.heyden.memorizeapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import aka.heyden.memorizeapp.model.ScreenController;
import aka.heyden.memorizeapp.util.CustomIntent;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by N4047 on 2016-07-22.
 */

public class ScreenReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        CustomIntent serviceIntent = new CustomIntent(context, ScreenController.class);
        serviceIntent.putExtra("screenState", (intent.getAction()==null)?"EMPTY":intent.getAction());
        context.startService(serviceIntent);
    }
}