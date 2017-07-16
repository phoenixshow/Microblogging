package com.phoenix.xlblog.presenter;

import com.phoenix.xlblog.adapters.HomeListAdapter;

/**
 * Created by flashing on 2017/7/15.
 */

public interface HomePresenter {
    void loadData();//加载数据
    void loadMore();//加载更多
//    void setURL(String url);//设置地址//但MVP尽量不在View中对数据做处理，所以把它改成以下两个方法
    void requestHomeTimeLine();//请求主页数据
    void requestUserTimeLine();//请求我的数据
    /*HomeListAdapter getAdapter();//获取适配器*/
}
