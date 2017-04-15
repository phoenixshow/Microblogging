package com.phoenix.xlblog.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.phoenix.xlblog.R;
import com.phoenix.xlblog.constant.Constants;
import com.phoenix.xlblog.entities.Status;
import com.phoenix.xlblog.utils.SPUtils;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.AsyncWeiboRunner;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.WeiboParameters;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

public class HomeFragment extends BaseFragment {
    private String url = "https://api.weibo.com/2/statuses/public_timeline.json";
    private AsyncWeiboRunner mAsyncWeiboRunner;
    private WeiboParameters mParameters;
    //(“GET”, “POST”, “DELETE”)
    private String httpMethod;
    private SPUtils mSPUtils;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAsyncWeiboRunner = new AsyncWeiboRunner(getActivity());
        mParameters = new WeiboParameters(Constants.APP_KEY);
        httpMethod = "GET";
        mSPUtils = SPUtils.getInstance(getActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mParameters.put(WBConstants.AUTH_ACCESS_TOKEN, mSPUtils.getToken().getToken());
        mAsyncWeiboRunner.requestAsync(url, mParameters, httpMethod, new RequestListener() {
            @Override
            public void onComplete(String s) {
                Log.e("TAG", "onComplete------>s="+s);
                JsonParser parser = new JsonParser();
                JsonObject object = parser.parse(s).getAsJsonObject();
                JsonArray array = object.get("statuses").getAsJsonArray();
                List<Status> list = new ArrayList<Status>();
                Type type = new TypeToken<ArrayList<Status>>(){}.getType();
                list = new Gson().fromJson(array, type);
                Log.e("TAG", "---------->"+list.size());
            }

            @Override
            public void onWeiboException(WeiboException e) {

            }
        });
        return view;
    }
}
