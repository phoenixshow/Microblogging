package com.phoenix.xlblog.fragments;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.phoenix.xlblog.R;
import com.phoenix.xlblog.adapters.HomeListAdapter;
import com.phoenix.xlblog.constant.Constants;
import com.phoenix.xlblog.entities.HttpResponse;
import com.phoenix.xlblog.entities.Status;
import com.phoenix.xlblog.networks.BaseNetwork;
import com.phoenix.xlblog.networks.Urls;
import com.phoenix.xlblog.utils.DividerItemDecoration;
import com.phoenix.xlblog.utils.LogUtils;
import com.phoenix.xlblog.utils.SPUtils;
import com.phoenix.xlblog.views.PullToRefreshRecyclerView;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.net.AsyncWeiboRunner;
import com.sina.weibo.sdk.net.WeiboParameters;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragment {
    private WeiboParameters mParameters;
    private SPUtils mSPUtils;
    private PullToRefreshRecyclerView rv;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.ItemDecoration mItemDecoration;
    private List<Status> mStatusList;
    private HomeListAdapter mListAdapter;
    private String url = Urls.HOME_TIME_LINE;
    private int page = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

        mParameters = new WeiboParameters(Constants.APP_KEY);
        mSPUtils = SPUtils.getInstance(getActivity().getApplicationContext());
        mStatusList = new ArrayList<>();
        mListAdapter = new HomeListAdapter(getActivity(), mStatusList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rv = (PullToRefreshRecyclerView) inflater.inflate(R.layout.v_common_recyclerview, container, false);
        init();
        loadData(url, false);
        return rv;
    }

    private void loadData(String url, final boolean loadMore) {
        new BaseNetwork(getActivity(), url) {
            @Override
            public WeiboParameters onPrepare() {
                mParameters.put(WBConstants.AUTH_ACCESS_TOKEN, mSPUtils.getToken().getToken());
                mParameters.put(Constants.SOURCE, Constants.APP_KEY);
                mParameters.put(Constants.PAGE, page);
                mParameters.put(Constants.COUNT, 2);//10
                return mParameters;
            }

            @Override
            public void onFinish(HttpResponse response, boolean success) {
                if (success){
                    LogUtils.e(response.response);
                    List<Status> list = new ArrayList<Status>();
                    Type type = new TypeToken<ArrayList<Status>>(){}.getType();
                    list = new Gson().fromJson(response.response, type);
                    if (null != list && list.size() > 0){
                        if (!loadMore) {
                            mStatusList.clear();
                        }
                        mStatusList.addAll(list);
                    }
                    mListAdapter.notifyDataSetChanged();
                    rv.onRefreshComplete();
                }else {
                    LogUtils.e("onFinish---------->Failure："+response.message);
                }
            }
        }.get();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void init() {
        mLayoutManager = new LinearLayoutManager(getActivity());
        rv.getRefreshableView().setLayoutManager(mLayoutManager);
        mItemDecoration = new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL);
        rv.getRefreshableView().addItemDecoration(mItemDecoration);
        rv.getRefreshableView().setAdapter(mListAdapter);
        rv.setMode(PullToRefreshBase.Mode.BOTH);
//        rv.getRefreshableView().setOnScrollChangeListener(new View.OnScrollChangeListener(){
//            @Override
//            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                LogUtils.e(rv.getRefreshableView().getChildCount()+"");
//            }
//        });
        rv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                page = 1;
                loadData(url, false);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                page++;
                loadData(url, true);
            }
        });
        mListAdapter.setOnItemClickListener(new HomeListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                LogUtils.e("position---------->"+position);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Object event) {
        if (event instanceof Integer) {
            int id = (int) event;
            switch (id) {
                case R.id.action_home:
                    url = Urls.HOME_TIME_LINE;
                    break;
                case R.id.action_profile:
                    url = Urls.USER_TIME_LINE;
                    break;
            }
            LogUtils.e("--------->event instanceof Integer");
            loadData(url, false);
        }
        if (event instanceof String){
            LogUtils.e("--------->event instanceof String");
            loadData(url, false);
        }
    }
}
