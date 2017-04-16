package com.phoenix.xlblog.entities;

import java.io.Serializable;
import java.util.List;

/**
 * Created by flashing on 2017/4/15.
 */

public class Status implements Serializable {
    public String created_at;//创建时间
    public long id;
    public String mid;
    public String idstr;
    public String text;//微博内容
    public int textLength;
    public int source_allowclick;
    public int source_type;
    public String source;//从哪里来From
    public boolean favorited;
    public boolean truncated;
    public String in_reply_to_status_id;
    public String in_reply_to_user_id;
    public String in_reply_to_screen_name;
    public List<PicUrls> pic_urls;
    public String thumbnail_pic;
    public String bmiddle_pic;
    public String original_pic;
    public Object geo;
    public User user;//用户信息
    public Status retweeted_status;//被转发的微博
    public int reposts_count;
    public int comments_count;
    public int attitudes_count;

    public int deleted;
}
