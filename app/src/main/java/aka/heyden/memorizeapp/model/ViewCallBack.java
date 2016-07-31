/*
 * Copyright (c) 2016.  In-Kyu Heyden Han
 * 문의 : dlsrb0412@naver.com
 */

package aka.heyden.memorizeapp.model;

/**
 * Created by IVE on 2016-07-31.
 */

public interface ViewCallBack {
    void setController(ViewCallBack.controller controller);

    interface controller{
        void finish();
    };
}
