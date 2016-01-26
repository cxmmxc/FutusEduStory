package com.terry.futus.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.terry.futus.R;
import com.terry.futus.StoryApp;
import com.terry.futus.fragment.MineFragment;
import com.terry.futus.fragment.MusicFragment;
import com.terry.futus.fragment.StoryFragment;
import com.terry.futus.util.FragmentChangeManager;
import com.terry.futus.util.SpUtil;
import com.terry.futus.util.ToastAlone;

import org.xutils.common.util.LogUtil;

/**
 * 作者：Terry.Chen on 2015/12/241137.
 * 邮箱：herewinner@163.com
 * 描述：Tab形式的首页
 */
public class IndexTabActivity extends AppCompatActivity {

    private RelativeLayout tab_home_layout, tab_music_layout, tab_mine_layout;
    private ImageView img_home, img_music, img_mine;
    private TextView home_text, music_text, mine_text;
    private FrameLayout content_tab_layout;

    private FragmentChangeManager mFragmentManager;
    public final static String STORY_FRAG = "story_frag";
    public final static String MUSIC_FRAG = "music_frag";
    public final static String MINE_FRAG = "mine_frag";

    private SelectMode mSelectMode;

    private SpUtil spUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index_tab_layout);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        tab_home_layout = (RelativeLayout) findViewById(R.id.tab_home_layout);
        tab_music_layout = (RelativeLayout) findViewById(R.id.tab_music_layout);
        tab_mine_layout = (RelativeLayout) findViewById(R.id.tab_mine_layout);

        img_home = (ImageView) findViewById(R.id.img_home);
        img_music = (ImageView) findViewById(R.id.img_music);
        img_mine = (ImageView) findViewById(R.id.img_mine);

        home_text = (TextView) findViewById(R.id.home_text);
        music_text = (TextView) findViewById(R.id.music_text);
        mine_text = (TextView) findViewById(R.id.mine_text);

        content_tab_layout = (FrameLayout) findViewById(R.id.content_tab_layout);
    }

    private void initData() {

        String pkName = this.getPackageName();

        LogUtil.w("---------"+pkName);

        spUtil = SpUtil.getInstance(this);

        mFragmentManager = new FragmentChangeManager(this, R.id.content_tab_layout);
        mFragmentManager.addFragment(STORY_FRAG, StoryFragment.class, null);
        mFragmentManager.addFragment(MUSIC_FRAG, MusicFragment.class, null);
        mFragmentManager.addFragment(MINE_FRAG, MineFragment.class, null);
        mFragmentManager.onFragmentChanged(STORY_FRAG);
        mSelectMode = SelectMode.Home;
    }

    private String getAppInfo() {
        try {
            String pkName = this.getPackageName();
            String versionName = this.getPackageManager().getPackageInfo(
                    pkName, 0).versionName;
            int versionCode = this.getPackageManager()
                    .getPackageInfo(pkName, 0).versionCode;
            return pkName + "   " + versionName + "  " + versionCode;
        } catch (Exception e) {
        }
        return null;
    }

    private void setListener() {
        tab_home_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectMode == SelectMode.Home) {
                    return;
                }
                resetMode(mSelectMode);
                mSelectMode = SelectMode.Home;
                img_home.setImageResource(R.mipmap.sailfish_home_sel);
                home_text.setTextColor(getResources().getColor(R.color.tab_text_select_color));
                EnterFragment(1);
            }
        });

        tab_music_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectMode == SelectMode.Music) {
                    return;
                }
                resetMode(mSelectMode);
                mSelectMode = SelectMode.Music;
                img_music.setImageResource(R.mipmap.sailfish_comm_sel);
                music_text.setTextColor(getResources().getColor(R.color.tab_text_select_color));
                EnterFragment(2);
            }
        });

        tab_mine_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectMode == SelectMode.Mine) {
                    return;
                }
                resetMode(mSelectMode);
                mSelectMode = SelectMode.Mine;
                img_music.setImageResource(R.mipmap.sailfish_user_sel);
                music_text.setTextColor(getResources().getColor(R.color.tab_text_select_color));
                EnterFragment(3);


                //判断是否已经登录，未登录则进入登录页面
//                if (TextUtils.isEmpty(spUtil.getPersonObjid())) {
//                    Intent itent = new Intent(IndexTabActivity.this, LoginActivity.class);
//                    startActivity(itent);
//                }else {
//                    EnterFragment(3);
//                    resetMode(mSelectMode);
//                    mSelectMode = SelectMode.Mine;
//                    img_mine.setImageResource(R.mipmap.sailfish_user_sel);
//                    mine_text.setTextColor(getResources().getColor(R.color.tab_text_select_color));
//                }
            }
        });
    }


    /**
     *
     * @param mode
     * 重置mode
     */
    private void resetMode(SelectMode mode) {
        switch (mode) {
            case Home:
                img_home.setImageResource(R.mipmap.sailfish_home_nor);
                home_text.setTextColor(getResources().getColor(R.color.tab_text_color));
                break;
            case Music:
                img_music.setImageResource(R.mipmap.sailfish_comm_nor);
                music_text.setTextColor(getResources().getColor(R.color.tab_text_color));
                break;
            case Mine:
                img_mine.setImageResource(R.mipmap.sailfish_user_nor);
                mine_text.setTextColor(getResources().getColor(R.color.tab_text_color));
                break;
        }
    }

    private void EnterFragment(int position) {
        switch (position) {
            case 1:
                //进入MainFragment
                mFragmentManager.onFragmentChanged(STORY_FRAG);
                break;
            case 2:
                //进入分类Fragment
                mFragmentManager.onFragmentChanged(MUSIC_FRAG);
                break;
            case 3:
                //进入分类Fragment
                mFragmentManager.onFragmentChanged(MINE_FRAG);
                break;
        }
    }

    enum SelectMode{
        Home,Music,Mine
    }

    private int mClickCount = 0;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mClickCount = 0;
        }
    };

    @Override
    public void onBackPressed() {
        mClickCount += 1;
        ToastAlone.show(R.string.double_click_out_welcome_again);
        mHandler.sendEmptyMessageDelayed(1, 1500);
        if (mClickCount >= 2) {
            StoryApp.exit(IndexTabActivity.this);
        }
    }
}
