package aka.heyden.memorizeapp.view;

import android.os.Bundle;
import android.os.Parcel;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.Array;

import aka.heyden.memorizeapp.LockApplication;
import aka.heyden.memorizeapp.R;
import aka.heyden.memorizeapp.data.ScreenData;
import aka.heyden.memorizeapp.model.ScreenController;


public class LockActivity extends AppCompatActivity implements View.OnClickListener{
    private Button confirm;
    private ScreenData mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);

        init();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.confirm){
            finishActivity();
        }
    }

    private void init(){
        LockApplication.getInstance().getController().setActivity(this);

        changeData(this.mData = getIntent().getExtras().getParcelable("screenData"));

        confirm = (Button) findViewById(R.id.confirm);
        confirm.setOnClickListener(this);
    }

    public void showScreen(){
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_FULLSCREEN|
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
    }

    public void changeData(ScreenData mData){
        this.mData = mData;
        ((TextView) findViewById(R.id.word)).setText(this.mData.getWord());
        showScreen();
    }

    private void finishActivity(){
        LockApplication.getInstance().getController().setActivity(null);
        finish();
    }
}
