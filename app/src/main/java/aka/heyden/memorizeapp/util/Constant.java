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
 * Created by Han In-Gyu on 2016-07-30.<br><br>
 * 어플리케이션에 사용되는 상수값들의 클래스
 */

public class Constant {
    /** 잠금화면 windowManager.Layout 설정값*/
    public static final WindowManager.LayoutParams lockScreen = new WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT);

    /** 잠금화면 현재시간 표시를 위한 float -> String SimpleDateFormat 값*/
    public static final SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일\nHH시 mm분 ss초", Locale.KOREA);
    public static final SimpleDateFormat mSimpleDateFormatTest = new SimpleDateFormat("yyyy년 MM월 dd일\nHH시 mm분 ss.SSS초", Locale.KOREA);

    /** 잠금화면 배경 컨텐츠에 사용되는 상수값 */
    public static final int CUSTOM_IMAGE = 0;
    public static final int CUSTOM_COLOR = 1;
}
