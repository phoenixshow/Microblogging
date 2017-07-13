package com.phoenix.xlblog.activities;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.phoenix.xlblog.R;
import com.phoenix.xlblog.entities.PicUrls;

/**
 * Created by flashing on 2017/4/19.
 */

public class PhotoViewActivity extends BaseActivity {
    private PhotoView photoview;
    private PicUrls mPicUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getToolbar().hide();
        mPicUrls = (PicUrls) getIntent().getSerializableExtra(PicUrls.class.getSimpleName());
        assignViews();
    }

    private void assignViews() {
        photoview = (PhotoView) findViewById(R.id.photoview);
        //因为PhotoView就是继承ImageView，所以可以使用Glide加载图片
        Glide.with(this).load(mPicUrls.original_pic).asBitmap().fitCenter().into(photoview);
    }

    @Override
    public int getLayoutId() {
        return R.layout.ac_photoview;
    }
}
