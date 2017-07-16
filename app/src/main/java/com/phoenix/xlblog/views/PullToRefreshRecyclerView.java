package com.phoenix.xlblog.views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.phoenix.xlblog.R;
import com.phoenix.xlblog.utils.LogUtils;

/**
 * Created by flashing on 2017/7/13.
 */

public class PullToRefreshRecyclerView extends PullToRefreshBase<RecyclerView> {
    public PullToRefreshRecyclerView(Context context) {
        super(context);
    }

    public PullToRefreshRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullToRefreshRecyclerView(Context context, Mode mode) {
        super(context, mode);
    }

    public PullToRefreshRecyclerView(Context context, Mode mode, AnimationStyle animStyle) {
        super(context, mode, animStyle);
    }

    //滚动方向
    @Override
    public Orientation getPullToRefreshScrollDirection() {
        return Orientation.VERTICAL;
    }

    //创建一个可刷新的View
    @Override
    protected RecyclerView createRefreshableView(Context context, AttributeSet attrs) {
        RecyclerView recyclerView = new RecyclerView(context, attrs);
        recyclerView.setId(com.handmark.pulltorefresh.library.R.id.recyclerview);
        return recyclerView;
    }

    @Override
    protected boolean isReadyForPullEnd() {
        if (mRefreshableView.getChildCount() == 0){
            return true;
        }
        int count = mRefreshableView.getChildCount();
        View view = mRefreshableView.getChildAt(count - 1);
        int position = mRefreshableView.getChildLayoutPosition(view);
        if (position >= mRefreshableView.getAdapter().getItemCount()-1){
            LogUtils.e("view.getBottom()--------------->"+view.getBottom());
            LogUtils.e("mRefreshableView.getBottom()--------------->"+mRefreshableView.getBottom());
            return view.getBottom() <= mRefreshableView.getBottom();
        }
        return false;
    }

    @Override
    protected boolean isReadyForPullStart() {
        //RecyclerView其实是继承的ViewGroup，如果它没有子项，就可以刷新了
        if (mRefreshableView.getChildCount() == 0){
            return true;
        }
        View view = mRefreshableView.getChildAt(0);
        int position = mRefreshableView.getChildLayoutPosition(view);
        //判断当前可见的Item是否是列表的第0个Item
        if (position == 0){
            //返回第0个Item是否已到顶，到顶即相等，就返回true
            return view.getTop() == mRefreshableView.getTop();
        }
        return false;
    }

    public void setLayoutManager(RecyclerView.LayoutManager manager){
        mRefreshableView.setLayoutManager(manager);
    }

    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration){
        mRefreshableView.addItemDecoration(itemDecoration);
    }

    public void setAdapter(RecyclerView.Adapter adapter){
        mRefreshableView.setAdapter(adapter);
    }
}
