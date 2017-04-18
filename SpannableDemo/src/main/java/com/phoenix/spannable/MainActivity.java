package com.phoenix.spannable;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView tv;
    private int back_color;
    private int text_color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }

    private void initialize() {
        tv = (TextView) findViewById(R.id.tv);
        back_color = ContextCompat.getColor(getApplicationContext(), R.color.colorAccent);
        text_color = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary);

        /*SpannableString spannableString1 = new SpannableString("电视剧");
        //BackgroundColorSpan是背景色，SPAN_EXCLUSIVE_EXCLUSIVE是头尾都不包含
        spannableString1.setSpan(new BackgroundColorSpan(back_color), 0, spannableString1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString spannableString2 = new SpannableString("《西游记》");
        //ForegroundColorSpan是前景色，SPAN_EXCLUSIVE_INCLUSIVE是包含后面但不包含前面
        spannableString2.setSpan(new ForegroundColorSpan(text_color), 0, spannableString2.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        ssb.append(spannableString1).append(spannableString2);

        tv.setText(ssb);*/

        SpannableString spannableString3 = new SpannableString("导演杨洁去世！");
        spannableString3.setSpan(new ForegroundColorSpan(text_color), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString3.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Log.e("TAG", "-------->onClick");
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);//去掉下划线
                ds.setColor(back_color);//设置超链接颜色
            }
        }, 4, spannableString3.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(spannableString3);
        //设置这句，上面的点击事件才会生效
        tv.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
