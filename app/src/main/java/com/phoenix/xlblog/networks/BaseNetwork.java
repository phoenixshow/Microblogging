package com.phoenix.xlblog.networks;

import android.content.Context;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.phoenix.xlblog.entities.HttpResponse;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.AsyncWeiboRunner;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.WeiboParameters;

/**
 * Created by flashing on 2017/4/15.
 */

public abstract class BaseNetwork {
    private AsyncWeiboRunner mAsyncWeiboRunner;
    private String url;

    public BaseNetwork(Context context, String url) {
        this.url = url;
        mAsyncWeiboRunner = new AsyncWeiboRunner(context);
    }

    private RequestListener mRequestListener = new RequestListener() {
        @Override
        public void onComplete(String s) {
            boolean success = false;
            HttpResponse response = new HttpResponse();
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(s);//拿到当前元素
            if (element.isJsonObject()){//如果当前元素是一个Json对象
                JsonObject object = element.getAsJsonObject();
                /*错误返回值格式
                {
                    "request" : "/statuses/home_timeline.json",
                        "error_code" : "20502",
                        "error" : "Need you follow uid."
                }*/
                if (object.has("error_code")){
                    response.code = object.get("error_code").getAsInt();
                }
                if (object.has("error")){
                    response.message = object.get("error").getAsString();
                }
                //判断返回的数据分类
                if (object.has("statuses")){//微博类
                    response.response = object.get("statuses").toString();//有时候是数组
                    success = true;
                }else if (object.has("users")){//用户类
                    response.response = object.get("users").toString();
                    success = true;
                }else if (object.has("comments")){//评论类
                    response.response = object.get("comments").toString();
                    success = true;
                }else {//其它
                    response.response = s;
                    success = true;
                }
            }
            onFinish(response, success);
        }

        @Override
        public void onWeiboException(WeiboException e) {
            HttpResponse response = new HttpResponse();
            response.message = e.getMessage();
            onFinish(response, false);
        }
    };

    public void get(){
        mAsyncWeiboRunner.requestAsync(url, onPrepare(), "GET", mRequestListener);
    }

    public void post(){
        mAsyncWeiboRunner.requestAsync(url, onPrepare(), "POST", mRequestListener);
    }

    public void delete(){
        mAsyncWeiboRunner.requestAsync(url, onPrepare(), "DELETE", mRequestListener);
    }

    public abstract WeiboParameters onPrepare();
    public abstract void onFinish(HttpResponse response, boolean success);
}
