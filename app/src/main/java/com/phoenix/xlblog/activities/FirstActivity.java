package com.phoenix.xlblog.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.phoenix.xlblog.R;

/**
 * Created by flashing on 2017/4/4.
 */

public class FirstActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_first;
    }

    public void startActivity(View view) {
        startActivity(new Intent(FirstActivity.this, SecondActivity.class));
    }
}
