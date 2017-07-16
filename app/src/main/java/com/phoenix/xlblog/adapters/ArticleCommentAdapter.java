package com.phoenix.xlblog.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.phoenix.xlblog.R;
import com.phoenix.xlblog.activities.PhotoViewActivity;
import com.phoenix.xlblog.activities.RepostActivity;
import com.phoenix.xlblog.constant.Constants;
import com.phoenix.xlblog.entities.Comment;
import com.phoenix.xlblog.entities.PicUrls;
import com.phoenix.xlblog.entities.Status;
import com.phoenix.xlblog.utils.CircleTransform;
import com.phoenix.xlblog.utils.RichTextUtils;
import com.phoenix.xlblog.utils.TimeFormatUtils;

import java.util.List;

/**
 * 文章评论适配器
 */
public class ArticleCommentAdapter extends RecyclerView.Adapter {
    private final static int VIEW_TYPE_HEADER = 0;
    private final static int VIEW_TYPE_ITEM = 1;
    private Context mContext;
    private List<Comment> mList;
    private Status mStatus;

    public ArticleCommentAdapter(Context context, Status status, List<Comment> list) {
        this.mContext = context;
        this.mStatus = status;
        this.mList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_HEADER){
            view = layoutInflater.inflate(R.layout.item_weibo_content, parent, false);
            viewHolder = new HomeViewHolder(view);
        }else {
            view = layoutInflater.inflate(R.layout.item_weibo_comment, parent, false);
            viewHolder = new CommonViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HomeViewHolder) {
            HomeViewHolder homeViewHolder = (HomeViewHolder) holder;
            final Status entity = mStatus;
            homeViewHolder.usernameTv.setText(entity.user.screen_name);
            homeViewHolder.timeTv.setText(TimeFormatUtils.parseToYYMMDD(entity.created_at));
            homeViewHolder.contentTv.setText(RichTextUtils.getRichText(mContext, entity.text));
            homeViewHolder.contentTv.setMovementMethod(LinkMovementMethod.getInstance());//激活链接
            homeViewHolder.commentTv.setText(String.valueOf(entity.comments_count));//评论
            homeViewHolder.likeTv.setText(String.valueOf(entity.attitudes_count));//赞
            homeViewHolder.retweenTv.setText(String.valueOf(entity.reposts_count));//转发
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                homeViewHolder.sourceTv.setText(Html.fromHtml(entity.source, Html.FROM_HTML_MODE_COMPACT).toString());
            } else {
                homeViewHolder.sourceTv.setText(Html.fromHtml(entity.source).toString());
            }

            homeViewHolder.retweenTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, RepostActivity.class);
                    intent.putExtra(Constants.ID, entity.id);
                    intent.putExtra(Constants.STATUS, entity.text);
                    intent.setAction("REPOST");
                    mContext.startActivity(intent);
                }
            });
            homeViewHolder.commentTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, RepostActivity.class);
                    intent.putExtra(Constants.ID, entity.id);
                    intent.setAction("COMMENT");
                    mContext.startActivity(intent);
                }
            });

            Status reStatus = entity.retweeted_status;
            Glide.with(mContext).load(entity.user.profile_image_url)
                    .transform(new CircleTransform(mContext))//圆形头像
                    .error(R.mipmap.ic_default_header)//加载失败时的默认头像
                    .placeholder(R.mipmap.ic_launcher)//加载中的占位头像
                    .into(homeViewHolder.headerIv);

            List<PicUrls> pics = entity.pic_urls;
            if (null != pics && pics.size() > 0) {
                final PicUrls pic = pics.get(0);
                //微博传来的图片默认有三种尺寸，thumbnail_pic是最小的，bmiddle_pic是中等大小，original_pic是原始图片，
                pic.original_pic = pic.thumbnail_pic.replace("thumbnail", "large");
                pic.bmiddle_pic = pic.thumbnail_pic.replace("thumbnail", "bmiddle");
                homeViewHolder.contentIv.setVisibility(View.VISIBLE);
                //默认的加载方式就是GIF，所以不用再调用.asGif()方法了
                Glide.with(mContext).load(pic.bmiddle_pic).into(homeViewHolder.contentIv);
                homeViewHolder.contentIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PhotoViewActivity.class);
                        intent.putExtra(PicUrls.class.getSimpleName(), pic);
                        mContext.startActivity(intent);
                    }
                });
            } else {
                homeViewHolder.contentIv.setVisibility(View.GONE);
            }

            if (null != reStatus) {//如果不是转发的，是自己发的微博就会为空
                homeViewHolder.reLl.setVisibility(View.VISIBLE);
                String reContent = "@" + reStatus.user.screen_name + ":" + reStatus.text;
                homeViewHolder.recontentTv.setText(RichTextUtils.getRichText(mContext, reContent));
                homeViewHolder.recontentTv.setMovementMethod(LinkMovementMethod.getInstance());//激活链接

                List<PicUrls> rePics = reStatus.pic_urls;
                if (null != rePics && rePics.size() > 0) {
                    final PicUrls pic = rePics.get(0);
                    pic.original_pic = pic.thumbnail_pic.replace("thumbnail", "large");
                    pic.bmiddle_pic = pic.thumbnail_pic.replace("thumbnail", "bmiddle");
                    homeViewHolder.recontentIv.setVisibility(View.VISIBLE);
                    Glide.with(mContext).load(pic.bmiddle_pic).into(homeViewHolder.recontentIv);
                    homeViewHolder.recontentIv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, PhotoViewActivity.class);
                            intent.putExtra(PicUrls.class.getSimpleName(), pic);
                            mContext.startActivity(intent);
                        }
                    });
                } else {
                    homeViewHolder.recontentIv.setVisibility(View.GONE);
                }
            } else {
                homeViewHolder.reLl.setVisibility(View.GONE);//为空时隐藏布局
            }
        }
        if (holder instanceof CommonViewHolder){
            CommonViewHolder commonViewHolder = (CommonViewHolder) holder;
            Comment comment = mList.get(position - 1);
            Glide.with(mContext).load(comment.user.profile_image_url).into(commonViewHolder.headerIv);
            commonViewHolder.commentTv.setText(comment.text);
            commonViewHolder.usernameTv.setText(comment.user.screen_name);
            commonViewHolder.timeTv.setText(TimeFormatUtils.parseToYYMMDD(comment.created_at));
        }
    }

    @Override
    public int getItemCount() {
        return mList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return isHeader(position) ? VIEW_TYPE_HEADER : VIEW_TYPE_ITEM;
    }

    private boolean isHeader(int position){
        return position == 0;
    }

    class HomeViewHolder extends RecyclerView.ViewHolder{
        private ImageView headerIv;
        private TextView usernameTv;
        private TextView timeTv;
        private TextView sourceTv;
        private TextView contentTv;
        private ImageView contentIv;
        private LinearLayout reLl;
        private TextView recontentTv;
        private ImageView recontentIv;
        private TextView commentTv;
        private TextView likeTv;
        private TextView retweenTv;

        public HomeViewHolder(View itemView) {
            super(itemView);
            assignViews(itemView);
        }

        private void assignViews(View v) {
            headerIv = (ImageView) v.findViewById(R.id.header_iv);
            usernameTv = (TextView) v.findViewById(R.id.username_tv);
            timeTv = (TextView) v.findViewById(R.id.time_tv);
            sourceTv = (TextView) v.findViewById(R.id.source_tv);
            contentTv = (TextView) v.findViewById(R.id.content_tv);
            contentIv = (ImageView) v.findViewById(R.id.content_iv);
            reLl = (LinearLayout) v.findViewById(R.id.re_ll);
            recontentTv = (TextView) v.findViewById(R.id.recontent_tv);
            recontentIv = (ImageView) v.findViewById(R.id.recontent_iv);
            commentTv = (TextView) v.findViewById(R.id.comment_tv);
            likeTv = (TextView) v.findViewById(R.id.like_tv);
            retweenTv = (TextView) v.findViewById(R.id.retweet_tv);
        }
    }

    class CommonViewHolder extends RecyclerView.ViewHolder{
        private ImageView headerIv;
        private TextView usernameTv;
        private TextView timeTv;
        private TextView commentTv;

        public CommonViewHolder(View itemView) {
            super(itemView);
            initialize(itemView);
        }

        private void initialize(View view) {
            headerIv = (ImageView) view.findViewById(R.id.header_iv);
            usernameTv = (TextView) view.findViewById(R.id.username_tv);
            timeTv = (TextView) view.findViewById(R.id.time_tv);
            commentTv = (TextView) view.findViewById(R.id.comment_tv);
        }
    }
}
