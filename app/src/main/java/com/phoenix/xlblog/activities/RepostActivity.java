package com.phoenix.xlblog.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.phoenix.xlblog.R;
import com.phoenix.xlblog.constant.Constants;
import com.phoenix.xlblog.entities.HttpResponse;
import com.phoenix.xlblog.networks.BaseNetwork;
import com.phoenix.xlblog.networks.Urls;
import com.phoenix.xlblog.utils.LogUtils;
import com.phoenix.xlblog.utils.RichTextUtils;
import com.phoenix.xlblog.utils.SPUtils;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.net.WeiboParameters;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by flashing on 2017/4/19.
 */

public class RepostActivity extends BaseActivity {
    private EditText contentEt;
    private long id;
    private String content;
    private String action;
    private String url;

    private void assignViews() {
        contentEt = (EditText) findViewById(R.id.content_et);
        if (!TextUtils.isEmpty(content)) {
            contentEt.setText(RichTextUtils.getRichText(getApplicationContext(), content));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = getIntent().getLongExtra(Constants.ID, 0);
        action = getIntent().getAction();
        switch (action){
            case "COMMENT":
                getToolbar().setTitle(R.string.comment);
                url = Urls.COMMENT_CREATE;
                break;
            case "REPOST":
                getToolbar().setTitle(R.string.repost);
                content = getIntent().getStringExtra(Constants.STATUS);
                url = Urls.STATUS_REPOST;
                break;
        }
        assignViews();
    }

    @Override
    public int getLayoutId() {
        return R.layout.ac_repost;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_post, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        post(contentEt.getText().toString());
        return true;
    }

    private void post(final String string) {
        new BaseNetwork(getApplicationContext(), url) {
            @Override
            public WeiboParameters onPrepare() {
                WeiboParameters weiboParameters = new WeiboParameters(Constants.APP_KEY);
                weiboParameters.put(WBConstants.AUTH_ACCESS_TOKEN, SPUtils.getInstance(getApplicationContext()).getToken().getToken());
                if ("REPOST".equals(action)) {
                    weiboParameters.put(Constants.STATUS, "//" + string);
                }else {
                    weiboParameters.put(Constants.COMMENT, string);
                }
                weiboParameters.put(Constants.ID, id);
                return weiboParameters;
            }

            @Override
            public void onFinish(HttpResponse response, boolean success) {
                if (success){
                    LogUtils.e("RepostActivity------>"+response.response);
                    EventBus.getDefault().post("onFinish");
                    finish();
                }
            }
        }.post();
    }
}
