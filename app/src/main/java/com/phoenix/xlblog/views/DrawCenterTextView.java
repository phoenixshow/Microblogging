package com.phoenix.xlblog.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;

/**
 * Created by flashing on 2017/4/16.
 */

public class DrawCenterTextView extends android.support.v7.widget.AppCompatTextView {
    public DrawCenterTextView(Context context) {
        super(context);
    }

    public DrawCenterTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawCenterTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    public DrawCenterTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }


    @Override
    protected void onDraw(Canvas canvas) {
        //获取四个方向上的Drawable
        Drawable[] drawables = getCompoundDrawables();
        //取出左图标，根据setCompoundDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom)方法得知左图标在数组的第0位
        Drawable drawableLeft = drawables[0];
        if (null != drawableLeft){
            //默认就是START在左边，不加这句也可以
            setGravity(Gravity.START);
            //获取图标宽度
            int drawWidth = drawableLeft.getIntrinsicWidth();
            //文字的宽度
            int textWidth = (int) getPaint().measureText(getText().toString());
            //图标与文字的间距
            int drawPadding = getCompoundDrawablePadding();
            //整个内容的宽度
            int bodyWidth = drawWidth + textWidth + drawPadding;
            //在X轴方向上进行偏移
            canvas.translate((getWidth()-bodyWidth)/2, 0);
        }

        Drawable drawableRight = drawables[2];
        if (null != drawableRight){
            //TextView的默认对齐方式是 private int mGravity = Gravity.TOP | Gravity.START; 这里的START表示是在左边，需要设置为END右边，否则文字无法显示
            setGravity(Gravity.END);
            //获取图标宽度
            int drawWidth = drawableRight.getIntrinsicWidth();
            //文字的宽度
            int textWidth = (int) getPaint().measureText(getText().toString());
            //图标与文字的间距
            int drawPadding = getCompoundDrawablePadding();
            //整个内容的宽度
            int bodyWidth = drawWidth + textWidth + drawPadding;
            //在X轴方向上进行偏移，反向偏移取负
            canvas.translate(-(getWidth()-bodyWidth)/2, 0);
        }
        super.onDraw(canvas);
    }
}
