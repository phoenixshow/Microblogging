package com.phoenix.xlblog.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import com.phoenix.xlblog.R;
import com.phoenix.xlblog.views.ToolbarX;

/**
 * Created by flashing on 2017/4/4.
 */

public abstract class BaseActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RelativeLayout contentRl;
    private ToolbarX mToolbarX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_baselayout);
        assignViews();
        View v = getLayoutInflater().inflate(getLayoutId(), contentRl, false);
        contentRl.addView(v);
        mToolbarX = new ToolbarX(toolbar, this);
    }

    private void assignViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        contentRl = (RelativeLayout) findViewById(R.id.content_rl);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.anim_in_right_left, R.anim.anim_out_right_left);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.anim_in_left_right, R.anim.anim_out_left_right);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        overridePendingTransition(R.anim.anim_in_left_right, R.anim.anim_out_left_right);
    }

    public ToolbarX getToolbar(){
        if (null == mToolbarX){
            mToolbarX = new ToolbarX(toolbar, this);
        }
        return mToolbarX;
    }

    public abstract int getLayoutId();
}
