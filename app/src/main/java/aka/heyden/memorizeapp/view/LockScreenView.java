package aka.heyden.memorizeapp.view;

import android.animation.Animator;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import aka.heyden.memorizeapp.R;
import aka.heyden.memorizeapp.data.ScreenData;
import aka.heyden.memorizeapp.databinding.ActivityLockBinding;
import aka.heyden.memorizeapp.model.ScreenController;
import aka.heyden.memorizeapp.model.ViewCallBack;
import aka.heyden.memorizeapp.util.Constant;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

import static android.content.Context.WINDOW_SERVICE;

/**
 * Created by N4047 on 2016-07-29.
 */

public class LockScreenView implements View.OnClickListener, View.OnTouchListener, ViewCallBack {
    private View screen;
    private WindowManager windowManager;
    private ActivityLockBinding binding;
    private ScreenData mData;
    private PublishSubject<String> timerQue;
    private PublishSubject<String> quit;
    private ViewCallBack.controller controller;

    /**
     * 1.View를 초기화하고 해당 레이아웃 데이터를 바인딩한다.
     * 2.바인딩된 데이터를 이용해 이벤트를 등록한다.
     * 3.실시간 현재시간 업데이트를 위한 RX 옵저버와 서브젝트를 초기화한다.
     * @param mContext
     */
    public LockScreenView(Context mContext) {
        init(mContext);
    }

    private void init(Context mContext){
        initView(mContext);
        initEvent();
        initReactive();
    }


    private void initView(Context mContext){
        windowManager = (WindowManager) mContext.getSystemService(WINDOW_SERVICE);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        screen = inflater.inflate(R.layout.activity_lock, null);
        binding = DataBindingUtil.bind(screen);
        setController((ScreenController) mContext);
    }

    private void initEvent(){
        binding.getRoot().setOnTouchListener(this);
        binding.confirm.setOnClickListener(this);
    }

    private void initReactive(){
        quit = PublishSubject.create();
        timerQue = PublishSubject.create();
        timerQue.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(t -> binding.datetime.setText(t), Throwable::printStackTrace);
        rx.Observable.interval(1, TimeUnit.SECONDS).map(t -> getDataTime()).takeUntil(quit).subscribe(timerQue);
    }

    // 매우 좋지 않은 부분..
    // 화면의 뷰가 존재하는지 검사하는 논리가 필요
    public void showScreen(){
        try {
            windowManager.addView(screen, Constant.lockScreen);
        }catch (Exception e){
        }
    }

    public void changeData(ScreenData mData){
        this.mData = mData;
        binding.word.setText(this.mData.getWord());
        binding.diction.setText(this.mData.getDiction());
        binding.datetime.setText(getDataTime());
    }

    private String getDataTime() {
        return Constant.mSimpleDateFormat.format(new Date());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.confirm) {
            finish();
        }
    }

    public void initScreen(Context mContext) {
        init(mContext);

        // else의 경우 screen 객체의 이벤트가 먹히지앖음
        // 우선 임시로 모든 경우에 새로 초기화하게 코드 작성
        /*if(screenIsNull()){
            init(mContext);
        }else{
            binding.getRoot().setX(0);
            initReactive();
        }*/
    }


    private boolean screenIsNull() {
        return screen==null?true:false;
    }


    private void finish() {
        quit.onNext(null);
        timerQue.onCompleted();
        windowManager.removeViewImmediate(screen);
        this.controller.finish();
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
                        .x(8000)
                        .setDuration(250)
                        .setInterpolator(t->0.4f)
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

    @Override
    public void setController(ViewCallBack.controller controller) {
        this.controller = controller;
    }
}
