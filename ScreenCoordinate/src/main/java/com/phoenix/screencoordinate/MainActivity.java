package com.phoenix.screencoordinate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity {
    private LinearLayout ll;
    private View vw;

    private void assignViews() {
        ll = (LinearLayout) findViewById(R.id.ll);
        vw = findViewById(R.id.vw);
        EventBus.getDefault().post("lll");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        assignViews();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMessageEvent(String string) {
        if ("lll".equals(string)){
            try {
                Thread.sleep(2000);
                int top = vw.getTop();
                int bottom = vw.getBottom();
                Log.e("TAG", "assignViews--------->:" + top);
                Log.e("TAG", "assignViews--------->:" + bottom);
                Log.e("TAG", "assignViews--------->:" + (top + vw.getHeight()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
