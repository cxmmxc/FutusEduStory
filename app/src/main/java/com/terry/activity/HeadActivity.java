package com.terry.activity;

import com.terry.BaseActivity;
import com.terry.view.HeadView;

/**
 * 作者：Terry.Chen on 2015/12/281039.
 * 邮箱：herewinner@163.com
 * 描述：头像界面
 */
public class HeadActivity extends BaseActivity{
    @Override
    protected void initView() {
        HeadView headView = new HeadView(this);
        setContentView(headView);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void initToolbar() {

    }
}
