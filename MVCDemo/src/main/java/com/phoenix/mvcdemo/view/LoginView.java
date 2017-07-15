package com.phoenix.mvcdemo.view;

import com.phoenix.mvcdemo.model.UserEntity;

public interface LoginView {
    String getUerName();//获取用户名
    String getPwd();//获取密码
    void onUserNameError();//用户名错误提示
    void onPwdError();//密码错误提示
    void onError();//笼统的错误提示
//    void onSuccess(UserEntity entity);//登录成功的操作
    void onSuccess(String address);//登录成功的操作
}
