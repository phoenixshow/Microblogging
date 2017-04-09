package com.phoenix.xlblog.activities;

import android.support.annotation.IdRes;
import android.support.v4.app.FragmentTabHost;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;

import com.phoenix.xlblog.R;
import com.phoenix.xlblog.fragments.HomeFragment;
import com.phoenix.xlblog.fragments.MessageFragment;
import com.phoenix.xlblog.fragments.ProfileFragment;

public class HomeActivity extends BaseActivity {
    private FrameLayout containerFl;
    private FragmentTabHost tabHost;
    private RadioGroup tabRg;
    private RadioButton homeRb;
    private RadioButton messageRb;
    private RadioButton profileRb;
    private Class[] fragment;
    private int menu_id = R.menu.menu_home;

    private void assignViews() {
        containerFl = (FrameLayout) findViewById(R.id.container_fl);
        tabHost = (FragmentTabHost) findViewById(R.id.tabHost);
        tabRg = (RadioGroup) findViewById(R.id.tab_rg);
        homeRb = (RadioButton) findViewById(R.id.home_rb);
        messageRb = (RadioButton) findViewById(R.id.message_rb);
        profileRb = (RadioButton) findViewById(R.id.profile_rb);

        tabHost.setup(getApplicationContext(), getSupportFragmentManager(), R.id.container_fl);
        for (int i = 0; i < fragment.length; i++) {
            TabHost.TabSpec tabSpec = tabHost.newTabSpec(String.valueOf(i)).setIndicator(String.valueOf(i));
            tabHost.addTab(tabSpec, fragment[i], null);
        }
        tabHost.setCurrentTab(0);
        tabRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.home_rb:
                        tabHost.setCurrentTab(0);
                        menu_id = R.menu.menu_home;
                        break;
                    case R.id.message_rb:
                        tabHost.setCurrentTab(1);
                        menu_id = -1;//菜单不可见
                        break;
                    case R.id.profile_rb:
                        tabHost.setCurrentTab(2);
                        menu_id = -1;
                        break;
                }
                supportInvalidateOptionsMenu();//重新调用一下onCreateOptionsMenu
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getToolbar().setDisplayHomeAsUpEnabled(false).setTitle(R.string.app_name);
        fragment = new Class[]{HomeFragment.class, MessageFragment.class, ProfileFragment.class};
        assignViews();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (menu_id == -1){
            menu.clear();
        } else {
            getMenuInflater().inflate(menu_id, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }
}
