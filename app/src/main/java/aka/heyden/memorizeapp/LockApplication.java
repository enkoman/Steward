package aka.heyden.memorizeapp;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Han In-Gyu on 2016-07-25.<br><br>
 */

public class LockApplication extends Application {
    private static LockApplication appInstance;
    private static Realm realm;

    @Override
    public void onCreate() {
        super.onCreate();
        appInstance = this;

        initReam();
    }

    private void initReam(){
        Realm.setDefaultConfiguration(new RealmConfiguration.Builder(this).build());
        realm = Realm.getDefaultInstance();
    }

    public static Realm getRealm(){
        return realm;
    }

    public static LockApplication getInstance() {
        return appInstance;
    }
}
