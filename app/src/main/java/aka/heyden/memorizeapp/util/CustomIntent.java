package aka.heyden.memorizeapp.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by N4047 on 2016-07-22.
 */

public class CustomIntent extends Intent {
    public CustomIntent(Context packageContext, Class<?> cls) {
        super(packageContext, cls);
        Log.d("CustomIntent", packageContext.getClass().getSimpleName() + " -> " + cls.getSimpleName());
    }
}
