package aka.heyden.memorizeapp.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

import aka.heyden.memorizeapp.LockApplication;
import aka.heyden.memorizeapp.R;
import aka.heyden.memorizeapp.model.ActivityCallBack;


public class FakeActivity extends Activity implements ActivityCallBack.view {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fake);
        setView(LockApplication.getInstance().getController());
    }

    @Override
    public void setView(ActivityCallBack callBack) {
        callBack.setView(this);
    }

    @Override
    public void fakeFinish() {
        finish();
    }
}
