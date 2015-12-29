package com.terry.activity;

import android.graphics.BitmapFactory;

import com.terry.BaseActivity;
import com.terry.R;
import com.terry.view.HeadView;

/**
 * 作者：Terry.Chen on 2015/12/281039.
 * 邮箱：herewinner@163.com
 * 描述：头像界面
 */
public class HeadActivity extends BaseActivity {
    private HeadView head_view;
    @Override
    protected void initView() {
//        HeadView headView = new HeadView(this);
//        setContentView(headView);
        setContentView(R.layout.head_view_layout);
        head_view = (HeadView) findViewById(R.id.head_view);
    }

    @Override
    protected void initData() {
        head_view.setBitmpData(BitmapFactory.decodeResource(getResources(), R.mipmap.splash1));
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void initToolbar() {

    }
}
