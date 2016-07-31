/*
 * Copyright (c) 2016.  In-Kyu Heyden Han
 * 문의 : dlsrb0412@naver.com
 */

package aka.heyden.memorizeapp.model;

/**
 * Created by IVE on 2016-07-31.
 *
 * Application Class가 있는데 굳이 쓸 필요가 있을까?
 * 고민
 */

public interface ActivityCallBack {
    void setView(ActivityCallBack.view view);

    interface view{
        void setView(ActivityCallBack callBack);
        void fakeFinish();
    };
}
