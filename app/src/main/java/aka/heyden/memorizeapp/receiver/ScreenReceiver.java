package aka.heyden.memorizeapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import aka.heyden.memorizeapp.model.ScreenController;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by N4047 on 2016-07-22.
 */

public class ScreenReceiver extends BroadcastReceiver {
    private boolean screenOn = false;
    private ScreenController controller;
    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        controller = ScreenController.getInstance();
        controller.setContext(context);
        mContext = context;
        screenOn = getScreenOn();

        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            shuffleData();
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            showScreen();
        }
        setScreenOn(screenOn);
    }

    private boolean getScreenOn(){
        SharedPreferences pref = mContext.getSharedPreferences("pref", MODE_PRIVATE);
        return pref.getBoolean("screenOn", false);
    }

    // 값 저장하기
    private void setScreenOn(boolean flag){
        SharedPreferences pref = mContext.getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("screenOn", flag);
        editor.commit();
    }

    /**
     * 잠금화면 초기화 or 변경된 데이터를 뿌린다
     */
    private void showScreen() {
        if (!screenOn) {
            controller.ShowScreen();
        } else {
            Log.d("??", "showScreen : flag값이 잘못되었습니다");
        }

        this.screenOn = true;
    }

    /**
     * 잠금화면에 사용될 데이터를 조합한다
     */
    private void shuffleData() {
        if (screenOn) {
            controller.shuffleData();
        }else {
            Log.d("??", "shuffleData : flag값이 잘못되었습니다");
        }

        this.screenOn = false;
    }
}