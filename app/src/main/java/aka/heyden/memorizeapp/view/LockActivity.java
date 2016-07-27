package aka.heyden.memorizeapp.view;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.app.Activity;
import android.app.ActivityManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.trello.rxlifecycle.ActivityEvent;
import com.trello.rxlifecycle.RxLifecycle;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Observable;
import java.util.concurrent.TimeUnit;

import aka.heyden.memorizeapp.LockApplication;
import aka.heyden.memorizeapp.databinding.ActivityLockBinding;
import aka.heyden.memorizeapp.util.NavigationUtil;
import aka.heyden.memorizeapp.R;
import aka.heyden.memorizeapp.data.ScreenData;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;


public class LockActivity extends Activity implements View.OnClickListener, View.OnTouchListener{
    private ScreenData mData;
    private ActivityLockBinding binding;
    private PublishSubject<String> timerQue;
    private PublishSubject<String> quit;
    private static  SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat ("yyyy년 MM월 dd일\nHH시 mm분 ss초", Locale.KOREA );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.confirm){
            finishActivity();
        }
    }

    private void init() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_lock);
        binding.confirm.setOnClickListener(this);
        binding.root.setOnTouchListener(this);

        changeData(this.mData = getIntent().getExtras().getParcelable("screenData"));
        LockApplication.getInstance().getController().setActivity(this);

        quit = PublishSubject.create();
        timerQue = PublishSubject.create();
        timerQue.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(t -> binding.datetime.setText(t), Throwable::printStackTrace);

        rx.Observable.interval(1, TimeUnit.SECONDS).map(t -> getDataTime()).takeUntil(quit).subscribe(timerQue);
    }

    public void showScreen(){
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
    }

    public void changeData(ScreenData mData){
        this.mData = mData;
        binding.word.setText(this.mData.getWord());
        binding.diction.setText(this.mData.getDiction());
        binding.datetime.setText(getDataTime());
        showScreen();
    }

    private void finishActivity(){
        LockApplication.getInstance().getController().setActivity(null);
        quit.onNext(null);
        timerQue.onCompleted();
        finish();
    }

    private String getDataTime(){
        return mSimpleDateFormat.format(new Date());
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recovery Service if it's dead
        if(!isServiceRunningCheck()){
            NavigationUtil.startScreenController(this);
        }
    }

    public boolean isServiceRunningCheck() {
        ActivityManager manager = (ActivityManager) this.getSystemService(Activity.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("ScreenController".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    float dX;
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dX = binding.root.getX() - event.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                binding.root.animate()
                        .x(event.getRawX() + dX)
                        .setDuration(0)
                        .start();
                break;
            case MotionEvent.ACTION_UP:
                binding.root.animate()
                        .x(binding.root.getWidth())
                        .setDuration(1500)
                        .setInterpolator(t->0.2f)
                        .setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                finishActivity();
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        }).start();
                break;
            default:
                return false;
        }
        return true;
    }
}
