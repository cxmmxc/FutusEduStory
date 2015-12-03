package com.terry.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.terry.BaseFragment;
import com.terry.R;
import com.terry.util.ToastAlone;

/**
 * 作者：Terry.Chen on 2015/11/271639.
 * 邮箱：herewinner@163.com
 * 描述：小故事的页面
 */
public class StoryFragment extends BaseFragment {

    private View mRootView;
    private RadioGroup radio_group;
    private RadioButton latest_btn, hot_btn;
    private final static String LATEST_FRAG = "latest_frag";
    private final static String HOT_FRAG = "hot_frag";
    private FutusMode mMode;
    private LatestStoryFragment latestStoryFragment;
    private HotStoryFragment hotStoryFragment;
    private FragmentManager mFragmentManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.story_frag_layout, null);
        }
        return mRootView;
    }


    @Override
    protected void initView() {
        latest_btn = (RadioButton) mRootView.findViewById(R.id.latest_btn);
        hot_btn = (RadioButton) mRootView.findViewById(R.id.hot_btn);
        radio_group = (RadioGroup) mRootView.findViewById(R.id.radio_group);
    }

    @Override
    protected void initData() {
        latestStoryFragment = new LatestStoryFragment();
        hotStoryFragment = new HotStoryFragment();

        mFragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.story_content_layout, latestStoryFragment, LATEST_FRAG);
        fragmentTransaction.commit();
        mMode = FutusMode.LatestMode;
    }


    @Override
    protected void setListener() {


        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                switch (checkedId) {
                    case R.id.latest_btn:
                        if (mMode == FutusMode.LatestMode) {
                            return;
                        }
                        resetTextColor();
                        fragmentTransaction.replace(R.id.story_content_layout,
                                latestStoryFragment, LATEST_FRAG);
                        fragmentTransaction.commit();
                        mMode = FutusMode.LatestMode;
                        latest_btn.setTextColor(Color.WHITE);
                        break;
                    case R.id.hot_btn:
                        if (mMode == FutusMode.HotMode) {
                            return;
                        }
                        resetTextColor();
                        fragmentTransaction.replace(R.id.story_content_layout, hotStoryFragment, HOT_FRAG);
                        fragmentTransaction.commit();
                        mMode = FutusMode.HotMode;
                        hot_btn.setTextColor(Color.WHITE);
                        break;
                }
            }
        });
    }

    private void resetTextColor(){
        latest_btn.setTextColor(Color.BLACK);
        hot_btn.setTextColor(Color.BLACK);
    }

    enum FutusMode {
        LatestMode, HotMode;
    }


}
