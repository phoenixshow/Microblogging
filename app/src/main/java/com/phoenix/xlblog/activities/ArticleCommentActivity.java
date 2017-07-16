package com.phoenix.xlblog.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.phoenix.xlblog.R;
import com.phoenix.xlblog.adapters.ArticleCommentAdapter;
import com.phoenix.xlblog.constant.Constants;
import com.phoenix.xlblog.entities.Comment;
import com.phoenix.xlblog.entities.HttpResponse;
import com.phoenix.xlblog.entities.Status;
import com.phoenix.xlblog.networks.BaseNetwork;
import com.phoenix.xlblog.networks.Urls;
import com.phoenix.xlblog.utils.LogUtils;
import com.phoenix.xlblog.utils.SPUtils;
import com.phoenix.xlblog.views.PullToRefreshRecyclerView;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.net.WeiboParameters;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by flashing on 2017/7/16.
 */

public class ArticleCommentActivity extends BaseActivity {
    private Status mStatus;
    private PullToRefreshRecyclerView rv;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArticleCommentAdapter mAdapter;
    private SPUtils mSPUtils;
    private List<Comment> mDatas;

    private void assignViews() {
        rv = (PullToRefreshRecyclerView) findViewById(R.id.rv);
        mLayoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(mLayoutManager);
        mAdapter = new ArticleCommentAdapter(this, mStatus, mDatas);
        rv.setAdapter(mAdapter);
        new BaseNetwork(this, Urls.COMMENT_SHOW){
            @Override
            public WeiboParameters onPrepare() {
                WeiboParameters parameters = new WeiboParameters(Constants.APP_KEY);
                parameters.put(Constants.ID, mStatus.id);
                parameters.put(Constants.PAGE, 1);
                parameters.put(Constants.COUNT, 10);
                parameters.put(Constants.AUTH_ACCESS_TOKEN, mSPUtils.getToken().getToken());
                return parameters;
            }

            @Override
            public void onFinish(HttpResponse response, boolean success) {
                if (success){
                    Type type = new TypeToken<ArrayList<Comment>>(){}.getType();
                    JsonParser parser = new JsonParser();
                    JsonElement element = parser.parse(response.response);
                    if (element.isJsonArray()){
                        List<Comment> temp = new Gson().fromJson(element, type);
                        mDatas.clear();
                        mDatas.addAll(temp);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        }.get();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getToolbar().setTitle(R.string.title_detail);
        mStatus = (Status) getIntent().getSerializableExtra(Status.class.getSimpleName());
        mSPUtils = SPUtils.getInstance(getApplicationContext());
        mDatas = new ArrayList<>();
        assignViews();
    }

    @Override
    public int getLayoutId() {
        return R.layout.v_common_recyclerview;
    }
}
