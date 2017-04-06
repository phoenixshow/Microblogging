package com.phoenix.xlblog.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.phoenix.xlblog.R;
import com.phoenix.xlblog.views.ToolbarX;

/**
 * Created by flashing on 2017/4/4.
 */

public class FirstActivity extends BaseActivity {
    private ToolbarX mToolbarX;
    private RadioGroup custom_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();
        getToolbar().setDisplayHomeAsUpEnabled(true)
                .setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .setCustomView(custom_view)
                .setTitle("ToolbarX")
                .setSubTitle("XSub");
    }

    private void initialize() {
        custom_view = (RadioGroup) getLayoutInflater().inflate(R.layout.view_custom, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.e("TAG", "setOnMenuItemClickListener"+item.getItemId());
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_first;
    }

    public void startActivity(View view) {
        startActivity(new Intent(FirstActivity.this, SecondActivity.class));
    }
}
