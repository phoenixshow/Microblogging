package com.phoenix.xlblog.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
    private RecyclerView rv;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.ItemDecoration mItemDecoration;
    private List<Status> mStatusList;
    private HomeListAdapter mListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mParameters = new WeiboParameters(Constants.APP_KEY);
        mSPUtils = SPUtils.getInstance(getActivity().getApplicationContext());
        mStatusList = new ArrayList<>();
        mListAdapter = new HomeListAdapter(getActivity(), mStatusList);
//        //注册广播接收者接收Activity传来的消息
//        IntentFilter filter = new IntentFilter();
//        filter.addAction("change");
//        LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).registerReceiver(new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                LogUtils.e("--------->onReceive");
//                loadData(Urls.USER_TIME_LINE);
//            }
//        }, filter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rv = (RecyclerView) inflater.inflate(R.layout.v_common_recyclerview, container, false);
        init();
        loadData(Urls.HOME_TIME_LINE);
        return rv;
    }

    private void loadData(String url) {
        new BaseNetwork(getActivity(), url) {
            @Override
            public WeiboParameters onPrepare() {
                mParameters.put(WBConstants.AUTH_ACCESS_TOKEN, mSPUtils.getToken().getToken());
                mParameters.put(Constants.PAGE, 1);
                mParameters.put(Constants.COUNT, 10);
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
                        mStatusList.clear();
                        mStatusList.addAll(list);
                    }
                    mListAdapter.notifyDataSetChanged();
                }else {
                    LogUtils.e("onFinish---------->Failure："+response.message);
                }
            }
        }.get();
    }

    private void init() {
        mLayoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(mLayoutManager);
        mItemDecoration = new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL);
        rv.addItemDecoration(mItemDecoration);
        rv.setAdapter(mListAdapter);
        mListAdapter.setOnItemClickListener(new HomeListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                LogUtils.e("position---------->"+position);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    /*@Subscribe(threadMode = ThreadMode.POSTING)
    public void onMessageEvent(String string) {
        if ("change".equals(string)){
            loadData(Urls.USER_TIME_LINE);
        }
    }*/
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Integer event) {
        switch (event){
            case R.id.action_home:
                loadData(Urls.HOME_TIME_LINE);
                break;
            case R.id.action_profile:
                loadData(Urls.USER_TIME_LINE);
                break;
        }
    }
/*//    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onMessageEvent(String string) {
        if ("change".equals(string)){
            mParameters.put(WBConstants.AUTH_ACCESS_TOKEN, mSPUtils.getToken().getToken());
            mParameters.put(Constants.PAGE, 1);
            mParameters.put(Constants.COUNT, 5);

            //request方法是一个同步操作，不能放在主线程，所以放在独立线程中执行
            String str = new AsyncWeiboRunner(getActivity()).request(Urls.USER_TIME_LINE, mParameters, "GET");
//            LogUtils.e("ThreadMode.BACKGROUND--------->"+str);
            LogUtils.e("ThreadMode.ASYNC--------->"+str);
        }
    }*/
    /*//试验BACKGROUND与ASYNC的区别
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMessageEventBackground(Integer string) {
        try {
            Thread.sleep(5000);
            LogUtils.e("ThreadMode.BACKGROUND--------->"+string);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onMessageEventAsync(Integer string) {
        try {
            Thread.sleep(5000);
            LogUtils.e("ThreadMode.ASYNC--------->"+string);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }*/
}
