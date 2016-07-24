package aka.heyden.memorizeapp.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import aka.heyden.memorizeapp.R;
import aka.heyden.memorizeapp.data.ScreenData;
import aka.heyden.memorizeapp.model.ScreenController;


public class LockActivity extends AppCompatActivity implements View.OnClickListener{
    private Button confirm;
    private ScreenController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showScreen();

        setContentView(R.layout.activity_lock);

        controller = ScreenController.getInstance();
        controller.setActivity(this);

        confirm = (Button) findViewById(R.id.confirm);
        confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.confirm){
            finish();
        }
    }

    private void showScreen(){
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_FULLSCREEN|
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
    }
    public void changeData(ScreenData mData){
        ((TextView) findViewById(R.id.word)).setText(mData.getWord());
        showScreen();
    }
}
