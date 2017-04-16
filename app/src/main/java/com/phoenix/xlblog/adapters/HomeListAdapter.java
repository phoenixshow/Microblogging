package com.phoenix.xlblog.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.phoenix.xlblog.R;
import com.phoenix.xlblog.entities.Status;
import com.phoenix.xlblog.utils.TimeFormatUtils;

import java.util.List;

/**
 * Created by flashing on 2017/4/15.
 */

public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.HomeViewHolder> {
    private List<Status> mData;
    private OnItemClickListener mOnItemClickListener;

    public HomeListAdapter(List<Status> data) {
        this.mData = data;
    }

    @Override
    public HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weibo_content, parent, false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HomeViewHolder holder, int position) {
        Status entity = mData.get(position);
        holder.usernameTv.setText(entity.user.screen_name);
        holder.timeTv.setText(TimeFormatUtils.parseToYYMMDD(entity.created_at));
        holder.contentTv.setText(entity.text);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N){
            holder.sourceTv.setText(Html.fromHtml(entity.source, Html.FROM_HTML_MODE_LEGACY).toString());
        }else {
            holder.sourceTv.setText(Html.fromHtml(entity.source).toString());//如果不加.toString()，显示粉字带下划线的主题样式
        }
        Status reStatus = entity.retweeted_status;
        if (null != reStatus){//如果不是转发的，是自己发的微博就会为空
            holder.reLl.setVisibility(View.VISIBLE);
            holder.recontentTv.setText(reStatus.text);
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
        private LinearLayout reLl;
        private TextView recontentTv;

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
            reLl = (LinearLayout) v.findViewById(R.id.re_ll);
            recontentTv = (TextView) v.findViewById(R.id.recontent_tv);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(View v, int position);
    }
}
