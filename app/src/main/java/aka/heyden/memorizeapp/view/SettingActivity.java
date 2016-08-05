package aka.heyden.memorizeapp.view;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.nobrain.android.permissions.AndroidPermissions;

import aka.heyden.memorizeapp.util.NavigationUtil;
import aka.heyden.memorizeapp.R;

/**
 * Created by N4047 on 2016-07-22.
 */

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
    private Button screen;
    private boolean isScreenOn;
    private static final String[] permissions = {Manifest.permission.DISABLE_KEYGUARD};
    public static final int REQUEST_CODE = 6161;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        init();
    }

    private void init() {
        screen = (Button) findViewById(R.id.screen);
        screen.setOnClickListener(this);

        // 스크린 락 유무 확인
        isScreenOn = getPreferences();
        uiUpdate(isScreenOn);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkDrawOverlayPermission(this);
            permissionCheck(permissions);
        }
    }

    private void uiUpdate(boolean flag) {
        screen.setText(flag ? "끌까요?" : "킬까요?");
    }

    private void screenOn() {
        Log.d("?", "screenOn");
        savePreferences(true);
        this.isScreenOn = true;
        uiUpdate(this.isScreenOn);
        NavigationUtil.startScreenController(this);
    }

    private void screenOff() {
        Log.d("?", "screenOff");
        savePreferences(false);
        this.isScreenOn = false;
        uiUpdate(this.isScreenOn);
        NavigationUtil.stopScreenController(this);
    }

    // 값 불러오기
    private boolean getPreferences() {
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        return pref.getBoolean("screenLock", false);
    }

    // 값 저장하기
    private void savePreferences(boolean flag) {
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("screenLock", flag);
        editor.commit();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.screen) {
            if (this.isScreenOn) {
                screenOff();
            } else {
                screenOn();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isScreenOn) {
            NavigationUtil.startScreenController(this);
        }
    }

    private void permissionCheck(String[] permissions) {
        AndroidPermissions.check(this)
                .permissions(permissions)
                .hasPermissions(permissions1 -> {})
                .noPermissions(permissions2 -> ActivityCompat.requestPermissions(SettingActivity.this, permissions2, REQUEST_CODE))
                .check();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, final String[] permissions, int[] grantResults) {
        AndroidPermissions.result()
                .addPermissions(REQUEST_CODE, this.permissions)
                .putActions(REQUEST_CODE, null, (hasPermissions, noPermissions) -> {
                    if (noPermissions != null) {
                        for (String permission : hasPermissions) {
                            Log.d("test", "획득성공 : " + permission);
                        }
                        for (String permission : noPermissions) {
                            Log.d("test", "획득실패 : " + permission);
                        }
                        finish();
                    }
                })
                .result(requestCode, permissions, grantResults);
    }

    public void checkDrawOverlayPermission(Context mContext) {
        /** check if we already  have permission to draw over other apps */
        if (!Settings.canDrawOverlays(mContext)) {
            /** if not construct intent to request permission */
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            /** request permission via start activity for result */
            startActivityForResult(intent, REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /** check if received result code
         is equal our requested code for draw permission  */
        if (requestCode == REQUEST_CODE) {
            /** if so check once again if we have permission */
            if (Settings.canDrawOverlays(this)) {
                // continue here - permission was granted
            } else {
                Toast.makeText(this, "권한획득에 실패하여 어플리케이션을 종료합니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}
