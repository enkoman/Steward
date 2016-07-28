package aka.heyden.memorizeapp;

import android.app.Application;

import aka.heyden.memorizeapp.model.ScreenController;

/**
 * Created by N4047 on 2016-07-25.
 */

public class LockApplication extends Application {
    private static LockApplication appInstance;
    private ScreenController controller;

    @Override
    public void onCreate() {
        super.onCreate();
        appInstance = this;
    }

    public static LockApplication getInstance() {
        return appInstance;
    }

    public void setController(ScreenController controller){
        this.controller = controller;
    }

    public ScreenController getController(){
        return this.controller;
    }
}
