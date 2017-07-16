package com.phoenix.xlblog.entities;

//评论
public class Comment {
    public String created_at;
    public long id;
    public String text;
    public String source;
    public User user;
    public String mid;
    public String idStr;
    public Comment reply_comment;
}
