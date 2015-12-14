package com.terry.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.terry.BaseActivity;
import com.terry.R;
import com.terry.fragment.MusicFragment;
import com.terry.fragment.StoryFragment;
import com.terry.util.FragmentChangeManager;

/**
 * Created by jl02 on 2015/11/25.
 */
public class IndexActivity extends BaseActivity implements View.OnClickListener{

    private DrawerLayout mDrawlayout;

    private ActionBarDrawerToggle mToggle;

    private Toolbar toolbar;

    private LinearLayout left_list;
//
//    private LeftListAdapter mLfetAdapter;
//    private int mCurrentPosition;

    private FragmentChangeManager mFragmentManager;

    public final static String STORY_FRAG = "story_frag";
    public final static String MUSIC_FRAG = "music_frag";

//    private View mHeaderView;

    private RelativeLayout head_layout;
    private SimpleDraweeView head_img;
    private TextView name_text, story_text, music_text, uploadfile_text;

    private final static int LOGIN_CODE = 13;
    private final static int USER_CODE = 14;
    @Override
    protected void initView() {
        setContentView(R.layout.index_layout);
        mDrawlayout = (DrawerLayout) findViewById(R.id.mDrawlayout);
//        left_list = (ListView) findViewById(R.id.left_list);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        head_layout = (RelativeLayout) findViewById(R.id.head_layout);
        head_img = (SimpleDraweeView) findViewById(R.id.head_img);
        name_text = (TextView) findViewById(R.id.name_text);
        story_text = (TextView) findViewById(R.id.story_text);
        music_text = (TextView) findViewById(R.id.music_text);
        uploadfile_text = (TextView) findViewById(R.id.uploadfile_text);
        left_list = (LinearLayout) findViewById(R.id.left_list);
    }

    @Override
    protected void initData() {

        if (TextUtils.isEmpty(spUtil.getPersonName())) {
            name_text.setText("点击登录");
        }else {
            name_text.setText(spUtil.getPersonName());
        }

        if (TextUtils.isEmpty(spUtil.getPersonHead())) {
            head_img.setImageURI(Uri.parse("res:///"+R.mipmap.setting_head_icon));
        }else {
            head_img.setImageURI(Uri.parse(spUtil.getPersonHead()));
        }

//        mHeaderView = LayoutInflater.from(mContext).inflate(R.layout.head_layout, null);
//        left_list.addHeaderView(mHeaderView);
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

//        mLfetAdapter = new LeftListAdapter(mContext);
//        left_list.setAdapter(mLfetAdapter);
//        mLfetAdapter.setData(Arrays.asList(getResources().getStringArray(R.array.drawble_left_list)));
//        left_list.setItemChecked(0, true);
//        mCurrentPosition = 1;

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
//        left_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (position == 0) {
//                    //Head模式
//
//
//                } else {
//                    if (mCurrentPosition == position) {
//                        return;
//                    }
//                    openOrCloseDrawer();
//                    mCurrentPosition = position;
//                    left_list.setItemChecked(position, true);
//                    EneterFragment(position);
//                }
//            }
//        });

        head_layout.setOnClickListener(this);
        story_text.setOnClickListener(this);
        music_text.setOnClickListener(this);
        uploadfile_text.setOnClickListener(this);

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
            case 1:
                //进入MainFragment
                openOrCloseDrawer();
                mFragmentManager.onFragmentChanged(STORY_FRAG);
                toolbar.setTitle(getResources().getString(R.string.futus_story));
                break;
            case 2:
                //进入分类Fragment
                openOrCloseDrawer();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_layout:
                //未登录,进入登录页面
                if (TextUtils.isEmpty(spUtil.getPersonObjid())) {
                    Intent itent = new Intent(mContext, LoginActivity.class);
                    startActivityForResult(itent, LOGIN_CODE);
                } else {
                    //进入用户详情页面
                    Intent intent = new Intent(mContext, UseActivity.class);
                    startActivityForResult(intent, USER_CODE);
                }
                break;
            case R.id.story_text:
                EneterFragment(1);
                break;
            case R.id.music_text:
                EneterFragment(2);
                break;
            case R.id.uploadfile_text:
                //进入上传文件页面，仅作者可见
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
//            if (requestCode == LOGIN_CODE) {
//                name_text.setText(spUtil.getPersonName());
//                if (TextUtils.isEmpty(spUtil.getPersonHead())) {
//                    head_img.setImageURI(Uri.parse("res:///"+R.mipmap.setting_head_icon));
//                }else {
//                    head_img.setImageURI(Uri.parse(spUtil.getPersonHead()));
//                }
//            }else if (requestCode == USER_CODE) {
//                //判断是否有修改，有修改，则更新头像以及name信息
//                if (TextUtils.isEmpty(spUtil.getPersonName())) {
//                    name_text.setText("点击登录");
//                }else {
//                    name_text.setText(spUtil.getPersonName());
//                }
//                if (TextUtils.isEmpty(spUtil.getPersonHead())) {
//                    head_img.setImageURI(Uri.parse("res:///" + R.mipmap.setting_head_icon));
//                } else {
//                    head_img.setImageURI(Uri.parse(spUtil.getPersonHead()));
//                }
//            }else {
                super.onActivityResult(requestCode, resultCode, data);
//            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //判断是否有修改，有修改，则更新头像以及name信息
        if (TextUtils.isEmpty(spUtil.getPersonName())) {
            name_text.setText("点击登录");
        }else {
            name_text.setText(spUtil.getPersonName());
        }
        if (TextUtils.isEmpty(spUtil.getPersonHead())) {
            head_img.setImageURI(Uri.parse("res:///" + R.mipmap.setting_head_icon));
        } else {
            head_img.setImageURI(Uri.parse(spUtil.getPersonHead()));
        }
    }
}
