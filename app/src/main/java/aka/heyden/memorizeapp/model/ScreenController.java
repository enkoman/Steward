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
import aka.heyden.memorizeapp.util.CustomIntent;
import aka.heyden.memorizeapp.view.FakeActivity;
import aka.heyden.memorizeapp.view.LockScreenView;


/**
 * Created by N4047 on 2016-07-22.
 */

public class ScreenController extends Service implements ViewCallBack.controller, ActivityCallBack{
    private ScreenData mData;
    private ScreenReceiver mReceiver;
    private String action = "";
    private LockScreenView screen;
    private ActivityCallBack.view view;

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
        LockApplication.getInstance().setController(this);
        registerReceiver();
        screen = new LockScreenView(this);
        Log.d("ScreenController", "생성완료");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (nullCheck(intent)) {
            action = intent.getExtras().getString("screenState", "EMPTY");
            if (!action.equals("EMPTY")) {
                /**
                 * 스크린이 꺼지면
                 * 1. 뷰를 초기화하고(null이면 생성, 아니라면 위치정보 리셋)
                 * 2. 뷰에 사용되는 데이터를 set한다
                 * 3. 윈도우매니저를 통해 화면에 추가를 한다
                 *
                 * 스크린이 켜지면
                 * 1. 잠금화면을 해제한다(투명한 OVER_SYSYTEM을 사용한다)
                 */

                // 화면이 꺼졌습니다.
                if (action.equals(Intent.ACTION_SCREEN_OFF)) {
                    initScreen();
                    // 화면이 켜졌습니다.
                } else if (action.equals(Intent.ACTION_SCREEN_ON)) {
                    disableKeyguard();
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LockApplication.getInstance().setController(null);
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
    }

    /**
     * @param intent
     * @return if intent and intent.getExtras() are not null, get the true
     */
    private boolean nullCheck(Intent intent) {
        return ((intent != null) ? ((intent.getExtras() != null) ? true : false) : false);
    }

    /**
     * 1. 혹시모르니 LockScreenView 객체가 null이라면 초기화한다
     * 2. 뷰에 사용되는 데이터를 set한다
     * 3. 윈도우매니저를 통해 화면에 추가를 한다
     */
    private void initScreen() {
        if (screen == null) {
            // 뷰를 생성한다
            screen = new LockScreenView(this);
        }

        screen.initScreen(this);
        screen.changeData(shuffleData());
        screen.showScreen();
    }

    /**
     *  잠금화면을 해제한다(투명 액티비티를 사용)
     */
    private void disableKeyguard() {
        CustomIntent intent = new CustomIntent(this, FakeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
    }

    private ScreenData shuffleData() {
        mData = new ScreenData();
        mData.setWord("Bread");
        return mData;
    }

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver, filter);
    }

    public void setView(ActivityCallBack.view view){
        this.view = view;
    }

    @Override
    public void finish() {
        this.view.fakeFinish();
    }
}
