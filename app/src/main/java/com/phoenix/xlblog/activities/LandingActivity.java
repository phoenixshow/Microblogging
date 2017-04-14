package com.phoenix.xlblog.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.phoenix.xlblog.R;
import com.phoenix.xlblog.constant.Constants;
import com.phoenix.xlblog.utils.SPUtils;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

import static com.sina.weibo.sdk.auth.Oauth2AccessToken.parseAccessToken;

public class LandingActivity extends BaseActivity {
    private SsoHandler mSsoHandler;
    private AuthInfo mAuthInfo;
    private SPUtils mSPUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getToolbar().hide();

        mAuthInfo = new AuthInfo(getApplicationContext(), Constants.APP_KEY,
                Constants.REDIRECT_URL, Constants.SCOPE);
        mSsoHandler = new SsoHandler(this, mAuthInfo);
        mSPUtils = SPUtils.getInstance(getApplicationContext());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkLogin();
            }
        }, 500);
    }

    private void checkLogin() {
        if (mSPUtils.isLogin()){
            startActivity(new Intent(LandingActivity.this, HomeActivity.class));
            finish();
        }else {
            //请求认证
            mSsoHandler.authorize(new WeiboAuthListener() {
                @Override
                public void onComplete(Bundle bundle) {
                    Log.e("onComplete", bundle + "");
                    //将Bundle中的数据转换为Oauth2AccessToken授权登录的信息
                    Oauth2AccessToken token = Oauth2AccessToken.parseAccessToken(bundle);
                    mSPUtils.saveToken(token);
                    startActivity(new Intent(LandingActivity.this, HomeActivity.class));
                    finish();
                }

                @Override
                public void onWeiboException(WeiboException e) {

                }

                @Override
                public void onCancel() {

                }
            });
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_landing;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null != mSsoHandler){
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }
}
