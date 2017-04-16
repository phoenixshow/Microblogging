package com.phoenix.xlblog.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.sina.weibo.sdk.net.WeiboParameters;

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
        mListAdapter = new HomeListAdapter(mStatusList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rv = (RecyclerView) inflater.inflate(R.layout.v_common_recyclerview, container, false);
        init();
        new BaseNetwork(getActivity(), Urls.HOME_TIME_LINE) {
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
        return rv;
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
}
