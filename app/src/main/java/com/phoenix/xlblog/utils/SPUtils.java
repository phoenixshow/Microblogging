package com.phoenix.xlblog.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

/**
 * Created by flashing on 2017/4/14.
 */

public class SPUtils {
    private static SPUtils instance;
    private static SharedPreferences mSharedPreferences;
    private static SharedPreferences.Editor mEditor;
    private static final String SP_NAME = "PHOENIXWB";
    private static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    private static final String IS_LOGIN = "IS_LOGIN";

    private SPUtils() {

    }

    public static SPUtils getInstance(Context context) {
        if (instance == null) {
            synchronized (SPUtils.class) {
                instance = new SPUtils();
                mSharedPreferences = context.getSharedPreferences(SP_NAME, Activity.MODE_PRIVATE);
                mEditor = mSharedPreferences.edit();
            }
        }
        return instance;
    }

    public void saveToken(Oauth2AccessToken accessToken){
        mEditor.putString(ACCESS_TOKEN, new Gson().toJson(accessToken)).commit();
        mEditor.putBoolean(IS_LOGIN, true).commit();
    }

    public boolean isLogin(){
        return mSharedPreferences.getBoolean(IS_LOGIN, false);
    }
}
