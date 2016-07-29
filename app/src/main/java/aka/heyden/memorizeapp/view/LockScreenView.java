package aka.heyden.memorizeapp.view;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.PixelFormat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import aka.heyden.memorizeapp.LockApplication;
import aka.heyden.memorizeapp.R;
import aka.heyden.memorizeapp.data.ScreenData;
import aka.heyden.memorizeapp.databinding.ActivityLockBinding;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

import static android.content.Context.WINDOW_SERVICE;

/**
 * Created by N4047 on 2016-07-29.
 */

public class LockScreenView implements View.OnClickListener, View.OnTouchListener{
    private View screen;
    private WindowManager windowManager;
    private static WindowManager.LayoutParams lockScreen =  new WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSLUCENT);
    private static WindowManager.LayoutParams dismissKeyguard =  new WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT);
    private ActivityLockBinding binding;
    private ScreenData mData;
    private PublishSubject<String> timerQue;
    private PublishSubject<String> quit;
    private static SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일\nHH시 mm분 ss초", Locale.KOREA);

    public LockScreenView(Context mContext) {
        windowManager = (WindowManager) mContext.getSystemService(WINDOW_SERVICE);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        screen = inflater.inflate(R.layout.activity_lock, null);
        binding = DataBindingUtil.bind(screen);

        binding.getRoot().setOnTouchListener(this);
        binding.confirm.setOnClickListener(this);

        quit = PublishSubject.create();
        timerQue = PublishSubject.create();
        timerQue.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(t -> binding.datetime.setText(t), Throwable::printStackTrace);

        rx.Observable.interval(1, TimeUnit.SECONDS).map(t -> getDataTime()).takeUntil(quit).subscribe(timerQue);
    }

    public void dismissKeyguard(){
        windowManager.addView(screen, dismissKeyguard);
    }

    public void showScreen(){
        dismissKeyguard();
        windowManager.updateViewLayout(screen, lockScreen);
    }

    public void changeData(ScreenData mData){
        this.mData = mData;
        binding.word.setText(this.mData.getWord());
        binding.diction.setText(this.mData.getDiction());
        binding.datetime.setText(getDataTime());
        showScreen();
    }
    private String getDataTime() {
        return mSimpleDateFormat.format(new Date());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.confirm) {
            finish();
        }
    }

    private void finish() {
        quit.onNext(null);
        timerQue.onCompleted();
        windowManager.removeViewImmediate(screen);
    }

    float dX;
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dX = screen.getRootView().getX() - event.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                screen.getRootView().animate()
                        .x(event.getRawX() + dX)
                        .setDuration(0)
                        .start();
                break;
            case MotionEvent.ACTION_UP:
                screen.getRootView().animate()
                        .x(screen.getRootView().getWidth())
                        .setDuration(1500)
                        .setInterpolator(t->0.2f)
                        .setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                finish();
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
