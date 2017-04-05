package com.phoenix.xlblog.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.phoenix.xlblog.R;

/**
 * Created by flashing on 2017/4/4.
 */

//public class FirstActivity extends BaseActivity {
public class FirstActivity extends AppCompatActivity {
    private Toolbar toolbar;

    private void assignViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        assignViews();

        toolbar.setTitle(R.string.title_first);
        toolbar.setSubtitle(R.string.title_sub_first);
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TAG", "setNavigationOnClickListener");
            }
        });
//        toolbar.inflateMenu(R.menu.menu_main);
//        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                Log.e("TAG", "setOnMenuItemClickListener"+item.getItemId());
//                return true;
//            }
//        });
        setSupportActionBar(toolbar);//将Toolbar设置为ActionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);//隐藏导航
//        getSupportActionBar().setDisplayShowTitleEnabled(false);//隐藏标题、子标题
        getSupportActionBar().setTitle("SetTitleAfterSupportActionBar");//在setSupportActionBar之后可以设置标题
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

    //    @Override
//    public int getLayoutId() {
//        return R.layout.activity_first;
//    }

    public void startActivity(View view) {
        startActivity(new Intent(FirstActivity.this, SecondActivity.class));
    }
}
