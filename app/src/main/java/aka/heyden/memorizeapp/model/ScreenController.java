package aka.heyden.memorizeapp.model;


import android.content.Context;
import android.util.Log;

import aka.heyden.memorizeapp.data.ScreenData;
import aka.heyden.memorizeapp.util.CustomIntent;
import aka.heyden.memorizeapp.view.LockActivity;


/**
 * Created by N4047 on 2016-07-22.
 * using singleton pattern(DCL), State pattern
 */

public class ScreenController {
    private volatile static ScreenController mController = null;
    private Context mContext;
    private LockActivity mActivity;
    private ScreenData mData;

    private ScreenController(){
    }

    public static ScreenController getInstance() {
        if (mController == null) {
            synchronized(ScreenController.class){
                if(mController == null){
                    mController = new ScreenController();
                }
            }
        }
        return mController;
    }

    public void setContext(Context mContext){
        mController.mContext = mContext;
    }

    public void setActivity(LockActivity mActivity){
        mController.mActivity = mActivity;
    }

    public void ShowScreen(){
        if(mController.mActivity == null){
            Log.d("??", "mController.mActivity == null");
            mController.mContext.startActivity(new CustomIntent(mController.mContext, LockActivity.class));
        }else{
            Log.d("??", "mController.mActivity != null");
            mController.mActivity.changeData(mData);
        }
    }

    public void shuffleData(){
        mData = new ScreenData();
        mData.setWord("Bread");
    }
}
