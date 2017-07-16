package com.phoenix.xlblog.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.phoenix.xlblog.adapters.HomeListAdapter;
import com.phoenix.xlblog.constant.Constants;
import com.phoenix.xlblog.entities.HttpResponse;
import com.phoenix.xlblog.entities.Status;
import com.phoenix.xlblog.networks.BaseNetwork;
import com.phoenix.xlblog.networks.Urls;
import com.phoenix.xlblog.utils.LogUtils;
import com.phoenix.xlblog.utils.SPUtils;
import com.phoenix.xlblog.views.mvpviews.HomeView;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.net.WeiboParameters;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by flashing on 2017/7/15.
 */

public class HomePresenterImp implements HomePresenter {
    private String url = Urls.HOME_TIME_LINE;
    private int page = 1;
    private SPUtils mSPUtils;
    private List<Status> mStatusList;
    private WeiboParameters mParameters;
    private HomeView mHomeView;
//    private HomeListAdapter mListAdapter;

    public HomePresenterImp(HomeView homeView) {
        this.mHomeView = homeView;
        mStatusList = new ArrayList<>();
        mSPUtils = SPUtils.getInstance(mHomeView.getActivity());
//        mListAdapter = new HomeListAdapter(mHomeView.getActivity(), mStatusList);
        mParameters = new WeiboParameters(Constants.APP_KEY);
    }

    @Override
    public void loadData() {
        page = 1;
        loadData(false);
    }

    @Override
    public void loadMore() {
        page++;
        loadData(true);
    }

    @Override
    public void requestHomeTimeLine() {
        url = Urls.HOME_TIME_LINE;
        loadData();
    }

    @Override
    public void requestUserTimeLine() {
        url = Urls.USER_TIME_LINE;
        loadData();
    }

    /*@Override
    public HomeListAdapter getAdapter() {
        return mListAdapter;
    }*/

    private void loadData(final boolean loadMore) {
        new BaseNetwork(mHomeView.getActivity(), url) {
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
//                mHomeView.onSuccess();
                if (success){
                    LogUtils.e(response.response);
                    List<Status> list = new ArrayList<Status>();
                    Type type = new TypeToken<ArrayList<Status>>(){}.getType();
                    list = new Gson().fromJson(response.response, type);
                    if (!loadMore) {
                        mStatusList.clear();
                    }
                    mStatusList.addAll(list);
//                    mListAdapter.notifyDataSetChanged();
                    mHomeView.onSuccess(mStatusList);
                }else {
                    mHomeView.onError(response.message);
                }
            }
        }.get();
    }
}
