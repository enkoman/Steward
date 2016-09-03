package aka.heyden.memorizeapp.view;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.WindowManager;

import aka.heyden.memorizeapp.R;
import aka.heyden.memorizeapp.presenter.ScreenController;
import aka.heyden.memorizeapp.util.CustomIntent;
import aka.heyden.memorizeapp.util.L;

import static aka.heyden.memorizeapp.model.MessageHandler.BINDING_COMPLETE;
import static aka.heyden.memorizeapp.model.MessageHandler.FINISH_ACTIVITY;


/**
 * Created by Han In-Gyu on 2016-07-29.<br><br>
 * 기본잠금화면을 해제하기위한 액티비티 클래스.<br>
 * 요구사항의 변동 가능성이 거의 전무하므로 패턴을 적용할 필요없이 클래스내에 로직을 포함시킨다.
 */
public class FakeActivity extends Activity {
    /**
     * Messenger for communicating with the service.
     */
    private Messenger sendMessenger = null;
    private final Messenger receiveMessenger = new Messenger(new IncomingHandler());

    /**
     * Flag indicating whether we have called bind on the service.
     */
    boolean mBound = false;

    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BINDING_COMPLETE:
                    L.d("activity : binding service success");
                    break;
                case FINISH_ACTIVITY:
                    L.d("activity : get message to stop myself");
                    finish();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        L.d("activity : onCreate");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fake);
        initBind();
    }

    /**
     * Class for interacting with the main interface of the service.
     */
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            if (className.getClassName().equals("aka.heyden.memorizeapp.presenter.ScreenController")) {
                sendMessenger = new Messenger(service);

                L.d("activity : start to bind a service");

                Message msg = Message.obtain(null, BINDING_COMPLETE);
                try {
                    msg.replyTo = receiveMessenger;
                    sendMessenger.send(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mBound = true;
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            L.d("activity : onServiceDisconnected");
            sendMessenger = null;
            mBound = false;
        }
    };

    /**
     * Bind to the service
     */
    private void initBind() {
        L.d("activity : initBind");
        CustomIntent bindIntent = new CustomIntent(this, ScreenController.class);
        bindService(bindIntent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStart() {
        L.d("activity : onStart complete");
        super.onStart();
    }

    @Override
    protected void onResume() {
        L.d("activity : onResume complete");
        super.onResume();
    }

    @Override
    protected void onStop() {
        L.d("activity : onStop complete");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        L.d("activity : onDestroy complete");
        try {
            Message msg = Message.obtain(null, FINISH_ACTIVITY);
            sendMessenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
        super.onDestroy();
    }
}
