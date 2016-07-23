package aka.heyden.memorizeapp.model;


import android.content.Context;
import android.content.Intent;

import aka.heyden.memorizeapp.view.LockActivity;


/**
 * Created by N4047 on 2016-07-22.
 * using singleton pattern(DCL), State pattern
 */

public class ScreenController {
    private volatile static ScreenController mController = null;
    private Context mContext;

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

    public void ShowScreen(){
        mController.mContext.startActivity(new Intent(mController.mContext, LockActivity.class));
    }

    public void shuffleData(){

    }
}
