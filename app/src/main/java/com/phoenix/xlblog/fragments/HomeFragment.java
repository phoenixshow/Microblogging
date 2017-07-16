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
import android.widget.Toast;

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
import com.phoenix.xlblog.presenter.HomePresenter;
import com.phoenix.xlblog.presenter.HomePresenterImp;
import com.phoenix.xlblog.utils.DividerItemDecoration;
import com.phoenix.xlblog.utils.LogUtils;
import com.phoenix.xlblog.utils.SPUtils;
import com.phoenix.xlblog.views.PullToRefreshRecyclerView;
import com.phoenix.xlblog.views.mvpviews.HomeView;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.net.AsyncWeiboRunner;
import com.sina.weibo.sdk.net.WeiboParameters;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragment implements HomeView{
    private PullToRefreshRecyclerView rv;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.ItemDecoration mItemDecoration;
    private HomePresenter mPresenter;
    private HomeListAdapter mListAdapter;
    private List<Status> mStatusList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        mPresenter = new HomePresenterImp(this);
        mStatusList = new ArrayList<>();
        mListAdapter = new HomeListAdapter(getActivity(), mStatusList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rv = (PullToRefreshRecyclerView) inflater.inflate(R.layout.v_common_recyclerview, container, false);
        init();
        mPresenter.loadData();
        return rv;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void init() {
        mLayoutManager = new LinearLayoutManager(getActivity());
        rv.getRefreshableView().setLayoutManager(mLayoutManager);
        mItemDecoration = new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL);
        rv.getRefreshableView().addItemDecoration(mItemDecoration);
//        rv.getRefreshableView().setAdapter(mPresenter.getAdapter());
        rv.getRefreshableView().setAdapter(mListAdapter);
        rv.setMode(PullToRefreshBase.Mode.BOTH);
        rv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                mPresenter.loadData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                mPresenter.loadMore();
            }
        });
//        mListAdapter.setOnItemClickListener(new HomeListAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View v, int position) {
//                LogUtils.e("position---------->"+position);
//            }
//        });
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
                    mPresenter.requestHomeTimeLine();
                    break;
                case R.id.action_profile:
                    mPresenter.requestUserTimeLine();
                    break;
            }
        }
    }

    @Override
    public void onSuccess(List<Status> list) {
        rv.onRefreshComplete();
        if (list != null && list.size() > 0) {
            mStatusList.clear();
            mStatusList.addAll(list);
            mListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onError(String error) {
        rv.onRefreshComplete();
        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
    }
}
