package com.phoenix.mvcdemo.presenter;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.phoenix.mvcdemo.model.UserEntity;
import com.phoenix.mvcdemo.view.LoginView;

public class LoginPresenterImp implements LoginPresenter {
    private LoginView mLoginView;

    public LoginPresenterImp(LoginView loginView) {
        mLoginView = loginView;
    }

    public void login() {
        String userName = mLoginView.getUerName();
        String pwd = mLoginView.getPwd();
        if (TextUtils.isEmpty(userName)) {
            mLoginView.onUserNameError();
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            mLoginView.onPwdError();
            return;
        }
        if (pwd.equals("abc")) {
            String result = "{\n" +
                    "    \"nickName\": \"phoenix\",\n" +
                    "    \"userID\": \"1\",\n" +
                    "    \"address\": \"吉林吉林\"\n" +
                    "}";
            UserEntity userEntity = new Gson().fromJson(result, UserEntity.class);
//            mLoginView.onSuccess(userEntity);
            mLoginView.onSuccess(userEntity.address);
        } else {
            mLoginView.onError();
        }
    }
}
