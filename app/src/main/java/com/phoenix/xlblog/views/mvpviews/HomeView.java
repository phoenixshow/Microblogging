package com.phoenix.xlblog.views.mvpviews;

import android.app.Activity;

import com.phoenix.xlblog.entities.Status;

import java.util.List;

/**
 * Created by flashing on 2017/7/15.
 */

public interface HomeView {
    Activity getActivity();
//    void onSuccess();
    void onSuccess(List<Status> list);
    void onError(String error);
}
