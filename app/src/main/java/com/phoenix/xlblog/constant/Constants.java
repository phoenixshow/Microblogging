package com.phoenix.xlblog.constant;

/**
 * Created by flashing on 2017/4/14.
 */

public class Constants {
    // 应用的APP_KEY
    public static final String APP_KEY = "911840818";
    // 应用的SECRET_KEY
    public static final String SECRET_KEY = "4829f61dd34dbd35fac66931b08ef82b";
    // 应用的回调页
    public static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";
    // 应用申请的高级权限
    public static final String SCOPE = "email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog," + "invitation_write";
    public static final String PAGE = "page";
    public static final String COUNT = "count";
}
