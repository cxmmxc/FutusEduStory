package com.terry.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.terry.BaseFragment;
import com.terry.R;
import com.terry.util.ToastAlone;

import org.xutils.common.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

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

    private ViewPager story_pager;
    private List<Fragment> fraglist;
    private StoryPagerAdapter mPagerAdapter;

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
        story_pager = (ViewPager) mRootView.findViewById(R.id.story_pager);
    }

    @Override
    protected void initData() {
        latestStoryFragment = new LatestStoryFragment();
        hotStoryFragment = new HotStoryFragment();
        fraglist = new ArrayList<>();
        fraglist.add(latestStoryFragment);
        fraglist.add(hotStoryFragment);
        mPagerAdapter = new StoryPagerAdapter(getFragmentManager(), fraglist);
        story_pager.setAdapter(mPagerAdapter);

//        mFragmentManager = getFragmentManager();
//        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.story_content_layout, latestStoryFragment, LATEST_FRAG);
//        fragmentTransaction.commit();
        mMode = FutusMode.LatestMode;
    }


    @Override
    protected void setListener() {


        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                switch (checkedId) {
                    case R.id.latest_btn:
                        if (mMode == FutusMode.LatestMode) {
                            return;
                        }
                        LogUtil.w("R.id.latest_btn");
                        story_pager.setCurrentItem(0);
//                        resetTextColor();
//                        fragmentTransaction.replace(R.id.story_content_layout,
//                                latestStoryFragment, LATEST_FRAG);
//                        fragmentTransaction.commit();
//                        mMode = FutusMode.LatestMode;
//                        latest_btn.setTextColor(Color.WHITE);
                        break;
                    case R.id.hot_btn:
                        if (mMode == FutusMode.HotMode) {
                            return;
                        }
                        LogUtil.w("R.id.hot_btn");
                        story_pager.setCurrentItem(1);
//                        resetTextColor();
//                        fragmentTransaction.replace(R.id.story_content_layout, hotStoryFragment, HOT_FRAG);
//                        fragmentTransaction.commit();
//                        mMode = FutusMode.HotMode;
//                        hot_btn.setTextColor(Color.WHITE);
                        break;
                }
            }
        });

        story_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        LogUtil.e("onPageSelected -- 0");
                        latest_btn.setChecked(true);
                        resetTextColor();
                        mMode = FutusMode.LatestMode;
                        latest_btn.setTextColor(Color.WHITE);
                        break;
                    case 1:
                        LogUtil.e("onPageSelected -- 1");
                        hot_btn.setChecked(true);
                        resetTextColor();
                        mMode = FutusMode.HotMode;
                        hot_btn.setTextColor(Color.WHITE);
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

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

    class StoryPagerAdapter extends FragmentPagerAdapter{

        private List<Fragment> list;

        public StoryPagerAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            this.list = list;
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }

}
