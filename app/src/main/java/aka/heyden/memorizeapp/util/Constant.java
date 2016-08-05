/*
 * Copyright (c) 2016.  In-Kyu Heyden Han
 * 문의 : dlsrb0412@naver.com
 */

package aka.heyden.memorizeapp.util;

import android.graphics.PixelFormat;
import android.view.WindowManager;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by IVE on 2016-07-30.
 */

public class Constant {
    public static WindowManager.LayoutParams lockScreen = new WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT);
    public static SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일\nHH시 mm분 ss초", Locale.KOREA);
}
