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
import com.phoenix.xlblog.activities.RepostActivity;
import com.phoenix.xlblog.constant.Constants;
import com.phoenix.xlblog.entities.PicUrls;
import com.phoenix.xlblog.entities.Status;
import com.phoenix.xlblog.utils.CircleTransform;
import com.phoenix.xlblog.utils.RichTextUtils;
import com.phoenix.xlblog.utils.TimeFormatUtils;

import java.util.List;

import static android.R.attr.data;

/**
 * Created by flashing on 2017/4/15.
 */

public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.HomeViewHolder> {
    private Context mContext;
    private List<Status> mData;
    private OnItemClickListener mOnItemClickListener;

    public HomeListAdapter(Context context, List<Status> data) {
        this.mContext = context;
        this.mData = data;
    }

    @Override
    public HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weibo_content, parent, false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HomeViewHolder holder, int position) {
        final Status entity = mData.get(position);
        holder.usernameTv.setText(entity.user.screen_name);
        holder.timeTv.setText(TimeFormatUtils.parseToYYMMDD(entity.created_at));
        holder.contentTv.setText(RichTextUtils.getRichText(mContext, entity.text));
        holder.contentTv.setMovementMethod(LinkMovementMethod.getInstance());//激活链接
        holder.commentTv.setText(String.valueOf(entity.comments_count));//评论
        holder.likeTv.setText(String.valueOf(entity.attitudes_count));//赞
        holder.retweenTv.setText(String.valueOf(entity.reposts_count));//转发
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N){
            // flags
            // FROM_HTML_MODE_COMPACT：html块元素之间使用一个换行符分隔
            // FROM_HTML_MODE_LEGACY：html块元素之间使用两个换行符分隔
            //如果不加.toString()，显示粉字带下划线的主题样式
            holder.sourceTv.setText(Html.fromHtml(entity.source, Html.FROM_HTML_MODE_COMPACT).toString());
        }else {
            holder.sourceTv.setText(Html.fromHtml(entity.source).toString());
        }

        holder.retweenTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, RepostActivity.class);
                intent.putExtra(Constants.ID, entity.id);
                intent.putExtra(Constants.STATUS, entity.text);
                intent.setAction("REPOST");
                mContext.startActivity(intent);
            }
        });
        holder.commentTv.setOnClickListener(new View.OnClickListener() {
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
                .into(holder.headerIv);

        List<PicUrls> pics = entity.pic_urls;
        if (null != pics && pics.size() > 0){
            PicUrls pic = pics.get(0);
            //微博传来的图片默认有三种尺寸，thumbnail_pic是最小的，bmiddle_pic是中等大小，original_pic是原始图片，
            pic.original_pic = pic.thumbnail_pic.replace("thumbnail", "large");
            pic.bmiddle_pic = pic.thumbnail_pic.replace("thumbnail", "bmiddle");
            holder.contentIv.setVisibility(View.VISIBLE);
            //默认的加载方式就是GIF，所以不用再调用.asGif()方法了
            Glide.with(mContext).load(pic.bmiddle_pic).into(holder.contentIv);
        }else {
            holder.contentIv.setVisibility(View.GONE);
        }

        if (null != reStatus){//如果不是转发的，是自己发的微博就会为空
            holder.reLl.setVisibility(View.VISIBLE);
            String reContent = "@"+reStatus.user.screen_name+":"+reStatus.text;
            holder.recontentTv.setText(RichTextUtils.getRichText(mContext, reContent));
            holder.recontentTv.setMovementMethod(LinkMovementMethod.getInstance());//激活链接

            List<PicUrls> rePics = reStatus.pic_urls;
            if (null != rePics && rePics.size() > 0){
                PicUrls pic = rePics.get(0);
                pic.original_pic = pic.thumbnail_pic.replace("thumbnail", "large");
                pic.bmiddle_pic = pic.thumbnail_pic.replace("thumbnail", "bmiddle");
                holder.recontentIv.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(pic.bmiddle_pic).into(holder.recontentIv);
            }else {
                holder.recontentIv.setVisibility(View.GONE);
            }
        }else {
            holder.reLl.setVisibility(View.GONE);//为空时隐藏布局
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mOnItemClickListener = listener;
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
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null){
                        mOnItemClickListener.onItemClick(v, getLayoutPosition());
                    }
                }
            });
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

    public interface OnItemClickListener{
        void onItemClick(View v, int position);
    }
}
