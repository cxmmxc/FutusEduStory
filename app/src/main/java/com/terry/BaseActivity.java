package com.terry;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.terry.util.SpUtil;

/**
 * Created by jl02 on 2015/11/27.
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected Activity mContext;
    protected SpUtil spUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        spUtil = SpUtil.getInstance(this);
        initWindow();
        initView();
        initToolbar();
        initData();
        setListener();
    }

    @TargetApi(19)
    private void initWindow(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintColor(getTheColor(R.color.title_bg));
            tintManager.setStatusBarTintEnabled(true);
        }
    }

    protected void initToolbar(Toolbar toolbar){
        if (toolbar == null)
            return;
        toolbar.setBackgroundColor(getResources().getColor(R.color.title_bg));
        toolbar.setTitle(R.string.futus_story);
        toolbar.setTitleTextColor(getTheColor(R.color.action_bar_title_color));
        toolbar.collapseActionView();
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        }
    }

    protected int getTheColor(int res){
        if (res <= 0)
            throw new IllegalArgumentException("resource id can not be less 0");
        return getResources().getColor(res);
    }

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void setListener();


    protected abstract void initToolbar();
}
