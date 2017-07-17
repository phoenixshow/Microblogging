package com.phoenix.xlblog.views.mvpviews;

import com.phoenix.xlblog.entities.Comment;

import java.util.List;

/**
 * Created by flashing on 2017/7/17.
 */

public interface ArticleCommentView extends BaseView {
    void onSuccess(List<Comment> list);
}
