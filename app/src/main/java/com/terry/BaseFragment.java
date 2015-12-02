package com.terry;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

/**
 * Created by lmz_cxm on 2015/11/28.
 */
public abstract class BaseFragment extends Fragment {
    protected FragmentActivity mActivity;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
        setListener();
    }

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void setListener();
}
