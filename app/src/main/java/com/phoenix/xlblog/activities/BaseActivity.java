package com.phoenix.xlblog.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.phoenix.xlblog.R;

/**
 * Created by flashing on 2017/4/4.
 */

public abstract class BaseActivity extends AppCompatActivity {
    private TextView titleTv;
    private RelativeLayout contentRl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_baselayout);
        assignViews();
        View v = getLayoutInflater().inflate(getLayoutId(), contentRl, false);
        contentRl.addView(v);
    }

    private void assignViews() {
        titleTv = (TextView) findViewById(R.id.title_tv);
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

    public abstract int getLayoutId();
}