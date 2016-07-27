package aka.heyden.memorizeapp.view;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import aka.heyden.memorizeapp.util.NavigationUtil;
import aka.heyden.memorizeapp.R;

/**
 * Created by N4047 on 2016-07-22.
 */

public class SettingActivity extends Activity implements View.OnClickListener{
    private Button screen;
    private boolean isScreenOn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        init();
    }

    private void init(){
        screen = (Button) findViewById(R.id.screen);
        screen.setOnClickListener(this);

        // 스크린 락 유무 확인
        isScreenOn = getPreferences();
        uiUpdate(isScreenOn);
    }

    private void uiUpdate(boolean flag){
        screen.setText(flag?"끌까요?":"킬까요?");
    }

    private void screenOn(){
        Log.d("?", "screenOn");
        savePreferences(true);
        this.isScreenOn = true;
        uiUpdate(this.isScreenOn);
        NavigationUtil.startScreenController(this);
    }
    private void screenOff(){
        Log.d("?", "screenOff");
        savePreferences(false);
        this.isScreenOn = false;
        uiUpdate(this.isScreenOn);
        NavigationUtil.stopScreenController(this);
    }

    // 값 불러오기
    private boolean getPreferences(){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        return pref.getBoolean("screenLock", false);
    }

    // 값 저장하기
    private void savePreferences(boolean flag){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("screenLock", flag);
        editor.commit();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.screen){
            if(this.isScreenOn){
                screenOff();
            }else{
                screenOn();
            }
        }
    }
}
