package com.terry.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.terry.BaseFragment;
import com.terry.R;

/**
 * Created by lmz_cxm on 2015/12/2.
 */
public class HotStoryFragment extends BaseFragment {

    private View mRootView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.same_story_layout, null);
        }
        return mRootView;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {

    }
}
