package com.phoenix.xlblog.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 时间转换工具类
 */
public class TimeFormatUtils {

    public static String  parseToYYMMDD(String time){
        //如果不加Locale.ENGLISH时间会不准
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy",Locale.ENGLISH);
        Date date = null;
        Calendar calendar= Calendar.getInstance();
        try {
            date= sdf.parse(time);
            calendar.setTime(date);
            calendar.setTimeZone(TimeZone.getDefault());
            calendar.getTimeInMillis();
           return converToSimpleStrDate(calendar.getTimeInMillis());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String converToSimpleStrDate(long date) {

        long current = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat;
        long offSet = (current - date) / 1000;
        Date dt = new Date(date);
        String returnData;
        if (offSet < 5 * 60) {//5分钟内
            returnData = "刚刚";
        } else if (offSet >= 5 * 60 && offSet < 60 * 60) {//5-60分钟内
            returnData = offSet / 60 + "分钟前";
        } else if (offSet >= 60 * 60 && offSet < 60 * 60 * 24) {//超过1小时
            returnData = (offSet / 60 / 60) + "小时前";
        } else if (offSet >= 60 * 60 * 24 && offSet < 60 * 60 * 24 * 2) {//超过1天
            returnData = "昨天";
        } else if (offSet >= 60 * 60 * 24 && offSet < 60 * 60 * 24 * 30) {//超过1个月
            simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm");
            returnData = simpleDateFormat.format(dt);
        } else if (offSet >= 60 * 60 * 24 * 30 && offSet < (60 * 60 * 24 * 30 * 365)) {//1年内
            //31天前
            simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm");
            returnData = simpleDateFormat.format(dt);
        } else {//1年前
            //一年前
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            returnData = simpleDateFormat.format(dt);
        }

        return returnData;
    }
}
