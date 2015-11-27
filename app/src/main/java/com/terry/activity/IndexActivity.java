package com.terry.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.terry.BaseActivity;
import com.terry.R;
import com.terry.adapter.LeftListAdapter;
import com.terry.fragment.MusicFragment;
import com.terry.fragment.StoryFragment;
import com.terry.util.FragmentChangeManager;

import java.util.Arrays;

/**
 * Created by jl02 on 2015/11/25.
 */
public class IndexActivity extends BaseActivity {

    private DrawerLayout mDrawlayout;

    private ActionBarDrawerToggle mToggle;

    private Toolbar toolbar;

    private ListView left_list;

    private LeftListAdapter mLfetAdapter;
    private int mCurrentPosition;

    private FragmentChangeManager mFragmentManager;

    public final static String STORY_FRAG = "story_frag";
    public final static String MUSIC_FRAG = "music_frag";


    @Override
    protected void initView() {
        setContentView(R.layout.index_layout);
        mDrawlayout = (DrawerLayout) findViewById(R.id.mDrawlayout);
        left_list = (ListView) findViewById(R.id.left_list);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    @Override
    protected void initData() {
        mToggle = new ActionBarDrawerToggle(this, mDrawlayout, 0, 0) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };

        mToggle.setDrawerIndicatorEnabled(true);
        mDrawlayout.setDrawerListener(mToggle);
        mDrawlayout.setScrimColor(getTheColor(R.color.drawer_scrim_color));

        mFragmentManager = new FragmentChangeManager(this, R.id.content_layout);
        mFragmentManager.addFragment(STORY_FRAG, StoryFragment.class, null);
        mFragmentManager.addFragment(MUSIC_FRAG, MusicFragment.class, null);
        mFragmentManager.onFragmentChanged(STORY_FRAG);

        mLfetAdapter = new LeftListAdapter(mContext);
        left_list.setAdapter(mLfetAdapter);
        mLfetAdapter.setData(Arrays.asList(getResources().getStringArray(R.array.drawble_left_list)));
        left_list.setItemChecked(0, true);
        mCurrentPosition = 0;

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mToggle.syncState();
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openOrCloseDrawer();
                }
            });
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mDrawlayout.isDrawerOpen(left_list)) {
            mDrawlayout.closeDrawer(left_list);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void openDrawer() {
        if (!mDrawlayout.isDrawerOpen(left_list)) {
            mDrawlayout.openDrawer(left_list);
        }
    }

    private void closeDrawer() {
        if (mDrawlayout.isDrawerOpen(left_list)) {
            mDrawlayout.closeDrawer(left_list);
        }
    }

    private void openOrCloseDrawer() {
        if (mDrawlayout.isDrawerOpen(left_list)) {
            mDrawlayout.closeDrawer(left_list);
        } else {
            mDrawlayout.openDrawer(left_list);
        }
    }

    @Override
    protected void setListener() {
        left_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mCurrentPosition == position) {
                    return;
                }
                openOrCloseDrawer();
                mCurrentPosition = position;
                left_list.setItemChecked(position, true);
                EneterFragment(position);
            }
        });

        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openOrCloseDrawer();
                }
            });
        }
    }

    private void EneterFragment(int position) {
        switch (position) {
            case 0:
                //进入MainFragment
                mFragmentManager.onFragmentChanged(STORY_FRAG);
                toolbar.setTitle(getResources().getString(R.string.futus_story));
                break;
            case 1:
                //进入分类Fragment
                mFragmentManager.onFragmentChanged(MUSIC_FRAG);
                toolbar.setTitle(getResources().getString(R.string.futus_music));
                break;
        }
    }

    @Override
    protected void initToolbar() {
        super.initToolbar(toolbar);
        toolbar.setTitle(R.string.futus_story);
    }
}
