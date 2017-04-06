package com.phoenix.xlblog.views;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import com.phoenix.xlblog.R;

/**
 * Created by flashing on 2017/4/5.
 */

public class ToolbarX {
    private Toolbar mToolbar;
    private AppCompatActivity mActivity;
    private ActionBar mActionBar;
    private RelativeLayout mCustomRl;

    public ToolbarX(Toolbar toolbar, AppCompatActivity activity) {
        this.mToolbar = toolbar;
        this.mActivity = activity;
        mCustomRl = (RelativeLayout) mToolbar.findViewById(R.id.custom_rl);
        mActivity.setSupportActionBar(mToolbar);
        mActionBar = mActivity.getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);//显示导航
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.finish();
            }
        });
    }

    public ToolbarX setTitle(String text){
        mActionBar.setTitle(text);
        return this;
    }

    public ToolbarX setTitle(int resId){
        mActionBar.setTitle(resId);
//        mActionBar.hide();
        return this;
    }

    public ToolbarX setSubTitle(String text){
        mActionBar.setSubtitle(text);
        return this;
    }

    public ToolbarX setSubTitle(int resId){
        mActionBar.setSubtitle(resId);
        return this;
    }

    public ToolbarX setNavigationOnClickListener(View.OnClickListener listener){
        mToolbar.setNavigationOnClickListener(listener);
        return this;
    }

    public ToolbarX setNavigationIcon(int resId){
        mToolbar.setNavigationIcon(resId);
        return this;
    }

    public ToolbarX setDisplayHomeAsUpEnabled(boolean show){
        mActionBar.setDisplayHomeAsUpEnabled(show);
        return this;
    }

    public ToolbarX setCustomView(View view){
        mCustomRl.removeAllViews();//防止重复添加，先移除所有
        mCustomRl.addView(view);
        return this;
    }
}
