package com.phoenix.xlblog.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.view.View;

import com.phoenix.xlblog.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则匹配工具类
 */
public class RichTextUtils {
    //匹配http超链接的
    private static String regex = "\\(?\\b(http://|www[.])[-A-Za-z0-9+&@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&@#/%=~_()|]";
    private static final Pattern WEB_PATTERN = Pattern.compile(regex);

    //匹配@xxx
    private static final Pattern MENTION_PATTERN = Pattern.compile("@[\\u4e00-\\u9fa5a-zA-Z0-9_-]+");

    public static SpannableString getRichText(final Context context, String text){
        SpannableString string = new SpannableString(text);
        if(!TextUtils.isEmpty(text)){
            final int link_color = ContextCompat.getColor(context, R.color.blue);
            final int mention_color = ContextCompat.getColor(context, R.color.blue);

            Matcher matcher= WEB_PATTERN.matcher(text);
            while (matcher.find()){//如果找到匹配的内容
                final String url  = matcher.group();
                string.setSpan(new ClickableSpan() {
                    public void onClick(View widget) {
                        LogUtils.d("匹配超链接--------->"+url);
                        //启用系统浏览器打开网址
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        context.startActivity(intent);
                    }

                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setUnderlineText(false);//去掉下划线
                        ds.setColor(link_color);//设置超链接文字颜色
                    }
                }, matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            Matcher matcher_two= MENTION_PATTERN.matcher(text);
            while (matcher_two.find()){
                final String url  = matcher_two.group();
                string.setSpan(new ClickableSpan() {
                    public void onClick(View widget) {
                        LogUtils.d("匹配转发来源--------->"+url);
                    }

                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setUnderlineText(false);
                        ds.setColor(mention_color);
                    }
                }, matcher_two.start(), matcher_two.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        return  string;
    }
}
