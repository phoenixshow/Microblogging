package com.phoenix.xlblog.views.mvpviews;

import android.app.Activity;

/**
 * Created by flashing on 2017/7/17.
 */

public interface BaseView {//BaseView<T>
    Activity getActivity();
    void onError(String error);
//    void onSuccess(T t);
}
