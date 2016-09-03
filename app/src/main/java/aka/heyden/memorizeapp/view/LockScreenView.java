package aka.heyden.memorizeapp.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import java.util.concurrent.TimeUnit;

import aka.heyden.memorizeapp.R;
import aka.heyden.memorizeapp.databinding.ActivityLockBinding;
import aka.heyden.memorizeapp.model.ScreenWord;
import aka.heyden.memorizeapp.model.SlideAnimation;
import aka.heyden.memorizeapp.presenter.ScreenController;
import aka.heyden.memorizeapp.presenter.ViewCallBack;
import aka.heyden.memorizeapp.util.Constant;
import aka.heyden.memorizeapp.util.SubscriptionUtils;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;

import static android.content.Context.WINDOW_SERVICE;

/**
 * Created by Han In-Gyu on 2016-07-29.<br><br>
 * 실제 잠금화면으로 사용되는 View 클래스
 */
public class LockScreenView extends SlideAnimation implements ViewCallBack {
    private View screen;
    private ScreenWord mData;
    private ActivityLockBinding binding;
    private WindowManager windowManager;
    private ViewCallBack.presenter presenter;
    private Subscription endItem;
    private PublishSubject<String> timerQue;
    private CompositeSubscription subscriptions = new CompositeSubscription();

    /**
     * 1.View를 초기화하고 해당 레이아웃 데이터를 바인딩한다.<br>
     * 2.바인딩된 데이터를 이용해 이벤트를 등록한다.<br>
     * 3.실시간 현재시간 업데이트를 위한 RX 옵저버와 서브젝트를 초기화한다.<br>
     *
     * @param mContext
     */
    public LockScreenView(Context mContext) {
        init(mContext);
    }

    private void init(Context mContext) {
        initView(mContext);
        initEvent();
        initReactive();
        initAnimation(windowManager, screen);
    }

    // 추후에 리소스가 많이 필요한 부분은 병렬처리로 변환할것
    private void initView(Context mContext) {
        windowManager = (WindowManager) mContext.getSystemService(WINDOW_SERVICE);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        screen = inflater.inflate(R.layout.activity_lock, null);
        binding = DataBindingUtil.bind(screen);
        setPresenter((ScreenController) mContext);
        windowManager.addView(screen, Constant.lockScreen);
        screen.setVisibility(View.GONE);
    }

    private void initEvent() {
        binding.root.setOnTouchListener(this);
        binding.confirm.setOnClickListener(v -> {
            // 슬라이딩 애니메이션중 본래 뷰의 버튼이 눌려 종료가 되는걸 방지한다
            if (!isSliding()) {
                finish();
            }
        });
    }

    private void initReactive() {
        timerQue = PublishSubject.create();
        timerQue.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .lift(SubscriptionUtils.composite(subscriptions))
                .subscribe(t -> binding.datetime.setText(t), Throwable::printStackTrace);
    }

    /**
     * 화면에 실시간 시간갱신을 위한 아이템 발행을 시작한다
     * @param interval 아이템 발행간격 시간
     */
    public void startClock(int interval) {
        endItem = rx.Observable.interval(interval, TimeUnit.MILLISECONDS)
                .map(t -> getDataTime())
                .lift(SubscriptionUtils.composite(subscriptions))
                .subscribe(timerQue);
    }

    public void changeData(ScreenWord mData) {
        this.mData = mData;
        binding.word.setText(this.mData.getWord());
        binding.diction.setText(this.mData.getDiction());
        binding.datetime.setText(getDataTime());
    }

    private String getDataTime() {
        return Constant.mSimpleDateFormat.format(System.currentTimeMillis());
    }

    /**
     * 1. 잠금화면이 존재하지않는다면 새로 생성한다.<br>
     * 2. 잠금화면이 존재한다면<br>x
     * -> 1. Rx이벤트를 구독중인것이 있다면 아이템 발행만 멈춘다<br>
     * -> 2. Rx이벤트를 구독중인것이 없다면 모든 Rx 이벤트를 초기화한다<br>
     * 3. 잠금화면을 띄운다
     *
     * @param mContext
     */
    public void initScreen(Context mContext) {
        if (screenIsNull()) {
            init(mContext);
        } else {
            showScreen();
            if (endItem != null) {
                endItem.unsubscribe();
            }
        }
    }


    public void showScreen() {
        screen.setVisibility(View.VISIBLE);
        screen.setX(0);
    }

    private boolean screenIsNull() {
        return screen == null ? true : false;
    }

    @Override
    protected void finish() {
        if (endItem != null) {
            endItem.unsubscribe();
        }
        endItem = null;
        this.presenter.finish();
        super.finish();
    }

    @Override
    public void destroy() {
        unSubscription();
        this.screen = null;
        this.windowManager = null;
        this.binding = null;
        this.mData = null;
        this.endItem = null;
        this.timerQue = null;
        this.subscriptions = null;
        this.presenter = null;
        super.destroy();
    }

    private void unSubscription() {
        if (subscriptions != null) {
            if (subscriptions.hasSubscriptions()) {
                subscriptions.unsubscribe();
            }
        }
    }

    @Override
    public void setPresenter(ViewCallBack.presenter presenter) {
        this.presenter = presenter;
    }
}
