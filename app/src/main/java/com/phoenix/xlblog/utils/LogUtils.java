package com.phoenix.xlblog.utils;

import android.text.TextUtils;
import android.util.Log;

/**
 * Created by flashing on 2017/4/15.
 */

public class LogUtils {
    private static final String TAG = "PHOENIXBLOG";

    public static void e(String message){
        if (!TextUtils.isEmpty(message)){
            Log.e(TAG, message);
        }
    }

    public static void d(String message){
        if (!TextUtils.isEmpty(message)){
            Log.d(TAG, message);
        }
    }
}
