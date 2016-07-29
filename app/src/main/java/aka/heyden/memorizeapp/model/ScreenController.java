package aka.heyden.memorizeapp.model;


import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import aka.heyden.memorizeapp.LockApplication;
import aka.heyden.memorizeapp.data.ScreenData;
import aka.heyden.memorizeapp.receiver.ScreenReceiver;
import aka.heyden.memorizeapp.view.LockActivity;
import aka.heyden.memorizeapp.view.LockScreenView;


/**
 * Created by N4047 on 2016-07-22.
 */

public class ScreenController extends Service {
    private LockActivity mActivity;
    private ScreenData mData;
    private ScreenReceiver mReceiver;
    private String action = "";
    private LockScreenView screen;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        init();
        super.onCreate();
    }

    private void init() {
        Log.d("ScreenController", "생성완료");
        LockApplication.getInstance().setController(this);
        registerReceiver();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (nullCheck(intent)) {
            action = intent.getExtras().getString("screenState", "EMPTY");
            if (!action.equals("EMPTY")) {

                /**
                 * 스크린이 꺼지면
                 * 1. 잠금화면 액티비티를 초기화하고(이미 존재한다면 패스)
                 * 2. 잠금화면 액티비티에 사용되는 데이터를 set한다.
                 *
                 * 스크린이 켜지면
                 * 1. 액티비티의 keyguard를 disable시키고
                 * 2. 애니메이션을 통해 화면이 나타난다(부가적인것)
                 *
                 */

                // 화면이 꺼졌습니다.
                if (action.equals(Intent.ACTION_SCREEN_OFF)) {
                    initScreen();
                    // 화면이 켜졌습니다.
                } else if (action.equals(Intent.ACTION_SCREEN_ON)) {
                    showScreen();
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * @param intent
     * @return if intent and intent.getExtras() are not null, get the true
     */
    private boolean nullCheck(Intent intent) {
        return ((intent != null) ? ((intent.getExtras() != null) ? true : false) : false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LockApplication.getInstance().setController(null);
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
    }

    private void initScreen() {
        if (screen == null) {
            screen = new LockScreenView(this);
        }else{
            screen.changeData(shuffleData());
        }
        /*
        if (this.mActivity == null) {
            Log.d("ScreenController", "mActivity == null");
            Intent intent = new CustomIntent(this, LockActivity.class);
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("screenData", shuffleData());
            startActivity(intent);
        } else {
            Log.d("ScreenController", "mActivity != null");
            this.mActivity.changeData(shuffleData());
        }*/
    }

    private ScreenData shuffleData() {
        mData = new ScreenData();
        mData.setWord("Bread");
        return mData;
    }

    private void showScreen() {
        try {
            screen.showScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver, filter);
    }

    public void setActivity(LockActivity mActivity) {
        this.mActivity = mActivity;
    }
}
