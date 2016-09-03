package aka.heyden.memorizeapp.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.nobrain.android.permissions.AndroidPermissions;

import java.io.File;

import aka.heyden.memorizeapp.databinding.ActivitySettingBinding;
import aka.heyden.memorizeapp.model.ScreenBackground;
import aka.heyden.memorizeapp.presenter.SettingPresenter;
import aka.heyden.memorizeapp.presenter.SettingPresenterImpl;
import aka.heyden.memorizeapp.util.Constant;
import aka.heyden.memorizeapp.util.L;
import aka.heyden.memorizeapp.util.NavigationUtil;
import aka.heyden.memorizeapp.R;

/**
 * Created by Han In-Gyu on 2016-07-22.<br><br>
 * 설정화면 view 액티비티 클래스
 */

public class SettingActivity extends AppCompatActivity implements View.OnClickListener, SettingPresenter.view {
    private boolean isScreenOn;
    private ActivitySettingBinding binding;
    private SettingPresenterImpl presenter;
    private static final String[] permissions = {Manifest.permission.DISABLE_KEYGUARD};
    public static final int REQUEST_CODE = 6161;
    public String result = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    /** initialize */
    private void init() {
        initPermission();
        initView();
        initEvent();
    }

    private void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting);

        // presenter 세팅
        presenter = new SettingPresenterImpl();
        presenter.setView(this);
    }

    private void initEvent() {
        binding.main.screen.setOnClickListener(this);
        binding.main.changeImage.setOnClickListener(this);
        binding.main.changeColor.setOnClickListener(this);
        binding.main.preview.setOnClickListener(this);
    }

    private void initPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkDrawOverlayPermission(this);
            permissionCheck(permissions);
        }
    }

    @Override
    public void changeScreenStatus(boolean setScreenOn) {
        if (setScreenOn) {
            activeScreen();
        } else {
            defectiveScreen();
        }
    }

    private void UpdateUi(boolean flag) {
        if(flag){
            activeScreen();
        }else{
            defectiveScreen();
        }

        try {
            ScreenBackground data = presenter.getPreviewThumbnail();
            if(data.getType() == Constant.CUSTOM_COLOR){
                binding.main.preview.setBackgroundColor(Integer.parseInt(data.getResult()));
            }else{
                Glide.with(SettingActivity.this).load(new File(data.getResult())).into(binding.main.preview);
            }
        }catch (Exception e){
        }

    }

    private void activeScreen() {
        binding.main.screen.setText("끌까요?");
        this.isScreenOn = saveAndUpdatePreferences(true);
        NavigationUtil.startScreenController(this);
        L.d("uiUpdate : " + (this.isScreenOn  ? "ON" : "OFF"));
    }

    private void defectiveScreen() {
        binding.main.screen.setText("킬까요?");
        this.isScreenOn = saveAndUpdatePreferences(false);
        NavigationUtil.stopScreenController(this);
        L.d("uiUpdate : " + (this.isScreenOn  ? "ON" : "OFF"));
    }

    // 값 불러오기
    private boolean getPreferences() {
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        return pref.getBoolean("screenLock", false);
    }

    // 값 저장하기
    private boolean saveAndUpdatePreferences(boolean isScreenOn) {
        SharedPreferences.Editor editor = getSharedPreferences("pref", MODE_PRIVATE).edit();
        editor.putBoolean("screenLock", isScreenOn);
        return editor.commit() == isScreenOn;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.screen:
                presenter.controlLockscreen(!isScreenOn);
                break;
            case R.id.change_image:
                presenter.changeImage();
                break;
            case R.id.change_color:
                showColorPicker();
                break;
            case R.id.preview:
                presenter.previewScreen();
                break;
        }
    }

    private void showColorPicker(){
        NavigationUtil.openColorPicker(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 스크린 락 유무 확인
        UpdateUi(isScreenOn = getPreferences());
    }

    private void permissionCheck(String[] permissions) {
        AndroidPermissions.check(this)
                .permissions(permissions)
                .hasPermissions(permissions1 -> { })
                .noPermissions(permissions2 -> ActivityCompat.requestPermissions(SettingActivity.this, permissions2, REQUEST_CODE))
                .check();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull final String[] permissions, @NonNull int[] grantResults) {
        AndroidPermissions.result()
                .addPermissions(REQUEST_CODE, SettingActivity.permissions)
                .putActions(REQUEST_CODE, null, (hasPermissions, noPermissions) -> {
                    if (noPermissions != null) {
                        for (String permission : hasPermissions) {
                            L.d("획득성공 : " + permission);
                        }
                        for (String permission : noPermissions) {
                            L.d("획득실패 : " + permission);
                        }
                        finish();
                    }
                })
                .result(requestCode, permissions, grantResults);
    }

    public void checkDrawOverlayPermission(Context mContext) {
        /** check if we already  have permission to draw over other apps */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(mContext)) {
                /** if not construct intent to request permission */
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                /** request permission via start activity for result */
                startActivityForResult(intent, REQUEST_CODE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /** check if received result code
         is equal our requested code for draw permission  */
        if (requestCode == REQUEST_CODE) {
            /** if so check once again if we have permission */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    Toast.makeText(this, "권한획득에 실패하여 어플리케이션을 종료합니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }
}
