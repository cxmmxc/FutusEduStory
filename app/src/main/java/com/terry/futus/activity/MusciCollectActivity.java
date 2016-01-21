package com.terry.futus.activity;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.terry.futus.BaseActivity;
import com.terry.futus.Constans;
import com.terry.futus.R;
import com.terry.futus.StoryApp;
import com.terry.futus.bean.MpThree;
import com.terry.futus.inter.IOncomplete;
import com.terry.futus.inter.IOnprapared;
import com.terry.futus.util.MusicPlayer;
import com.terry.futus.util.ToastAlone;
import com.terry.futus.view.GifLoadingDialog;

import org.xutils.common.util.LogUtil;
import org.xutils.ex.DbException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 作者：Terry.Chen on 2016/1/141553.
 * 邮箱：herewinner@163.com
 * 描述：音乐收藏
 */
public class MusciCollectActivity extends BaseActivity {
    private PullToRefreshListView music_pulllistview;
    private ListView music_listview;
    private FreshType mFreshType;
    private MusicAdapter mMusicAdapter;
    private ImageView play_pause_img, collect_mp_img, back_img;
    private TextView music_title_text, total_time_text, current_time_text;
    private RelativeLayout music_info_layout;
    private boolean isPlaying;
    private MpThree mMpthree;
    private MusicPlayer mMusicPlayer;
    private int mCurrentItem;

    private int mTotalCount;

    private GifLoadingDialog dialog;
    private ProgressBar progressbar;
    private String mCollectObjId;
    private boolean isCollected;
    private RelativeLayout music_co_title;

    @Override
    protected void initView() {
        setContentView(R.layout.music_frag_layout);

        music_pulllistview = (PullToRefreshListView) findViewById(R.id.music_pulllistview);
        music_listview = music_pulllistview.getRefreshableView();
        play_pause_img = (ImageView) findViewById(R.id.play_pause_img);
        collect_mp_img = (ImageView) findViewById(R.id.collect_mp_img);
        back_img = (ImageView) findViewById(R.id.back_img);
        music_title_text = (TextView) findViewById(R.id.music_title_text);
        total_time_text = (TextView) findViewById(R.id.total_time_text);
        current_time_text = (TextView) findViewById(R.id.current_time_text);
        music_info_layout = (RelativeLayout) findViewById(R.id.music_info_layout);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        music_co_title = (RelativeLayout) findViewById(R.id.music_co_title);
    }

    @Override
    protected void initData() {
        music_co_title.setVisibility(View.VISIBLE);

        dialog = new GifLoadingDialog(mContext, -1);
        dialog.setLoadText("缓冲中...");
        music_pulllistview.setMode(PullToRefreshBase.Mode.BOTH);
        mFreshType = FreshType.Fresh;
        mMusicAdapter = new MusicAdapter();
        music_listview.setAdapter(mMusicAdapter);
        mMusicPlayer = MusicPlayer.getInstance(mContext);
        getTotalCount();
    }

    private void getTotalCount() {
        BmobQuery<MpThree> query = new BmobQuery<MpThree>();
        query.addWhereEqualTo("isCollect", "1");
        query.count(mContext, MpThree.class, new CountListener() {
            @Override
            public void onSuccess(int i) {
                LogUtil.i("count=" + i);
                mTotalCount = i;
                getMusicData(FreshType.Fresh);
                progressbar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(int i, String s) {
                ToastAlone.show("errorcode=" + i + ",errorstr=" + s);
                progressbar.setVisibility(View.GONE);
            }
        });
    }

    Handler mHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    music_pulllistview.onRefreshComplete();
                    ToastAlone.show(R.string.load_all);
                    break;
                case 2:
                    //获取并显示当前播放歌曲的播放时间
                    int currentTime = mMusicPlayer.getCurrentTime();
                    int m_time = currentTime / 60;
                    int s_time = currentTime % 60;
                    String m_time_s = "";
                    String s_time_s = "";
                    if (m_time < 10) {
                        m_time_s = "0" + m_time;
                    } else {
                        m_time_s = m_time + "";
                    }
                    if (s_time < 10) {
                        s_time_s = "0" + s_time;
                    } else {
                        s_time_s = s_time + "";
                    }
                    current_time_text.setText(m_time_s + ":" + s_time_s);
                    mHander.sendEmptyMessageDelayed(2, 500);
                    break;
            }
        }
    };


    @Override
    protected void setListener() {
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mMusicPlayer.setOnpreparedListener(new IOnprapared() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                //准备完成再开始播放
                LogUtil.w("setOnpreparedListener");
                music_info_layout.setVisibility(View.VISIBLE);
                setTotalTime();
                dialog.dismiss();
                //开始播放的时候在设置为已读
                if (mCurrentItem != -1) {
                    View view = music_listview.getChildAt(mCurrentItem + 1 - music_listview.getFirstVisiblePosition());
                    TextView txtView = (TextView) view.findViewById(R.id.music_textview);
                    txtView.setTextColor(Color.GRAY);
                    if (mMpthree != null) {
                        mMpthree.setIsReaded(1);
                        try {
                            StoryApp.mDbManager.saveOrUpdate(mMpthree);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }
                }
                //查看是否已收藏
                getMobBeanId();
            }
        });

        music_pulllistview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getMusicData(FreshType.Fresh);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (mTotalCount != 0 && mMusicAdapter.getCount() >= mTotalCount) {
                    mHander.sendEmptyMessageDelayed(1, 500);
                    return;
                }
                getMusicData(FreshType.LoadMore);
            }
        });

        music_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                isCollected = false;
                //进入播放页面
                mMpthree = mMusicAdapter.getItem(position - 1);
//                ToastAlone.show(mMpthree.getMp3_name());
                mCurrentItem = position - 1;
                music_title_text.setText(mMpthree.getMp3_name());
                //开始播放此歌曲
                try {
                    dialog.show();
                    mMusicPlayer.playUrlMusic(mMpthree.getMp3_file_url());
                    play_pause_img.setImageResource(R.mipmap.pause_icon);
                    isPlaying = true;
                    mHander.sendEmptyMessage(2);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        play_pause_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMpthree == null) {
                    return;
                }
                if (isPlaying) {
                    //播放中，则暂停播放
                    isPlaying = false;
                    play_pause_img.setImageResource(R.mipmap.play_icon);
                    mMusicPlayer.pauseUrlMusic();
                } else {
                    //开始播放
                    isPlaying = true;
                    play_pause_img.setImageResource(R.mipmap.pause_icon);
                    mMusicPlayer.resumeUrlMusic();
                }
            }
        });

        mMusicPlayer.setOncomPleteListener(new IOncomplete() {
            @Override
            public void oncomplete(MediaPlayer mediaPlayer) {
                LogUtil.v("oncomplete");
                //播放下一首
                int count = mMusicAdapter.getCount();
                if (mCurrentItem + 1 > count) {
                    return;
                }
                mCurrentItem += 1;
                MpThree mpThree = mMusicAdapter.getItem(mCurrentItem);
                try {
                    music_title_text.setText(mpThree.getMp3_name());
                    mMusicPlayer.playUrlMusic(mpThree.getMp3_file_url());
//                    setTotalTime();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        collect_mp_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isCollected) {
                    //false,则判断是否登录
                    if (TextUtils.isEmpty(spUtil.getPersonObjid())) {
                        //去登录页面进行登录
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        //已登录，则直接进行收藏更新操作
                        mMpthree.setIsCollect(1);
                        mMpthree.setPersonObjId(spUtil.getPersonObjid());
                        mMpthree.update(mContext, new UpdateListener() {
                            @Override
                            public void onSuccess() {
                                //收藏成功
                                isCollected = true;
                                ToastAlone.show(R.string.collect_success);
                                collect_mp_img.setBackgroundResource(R.mipmap.mp3_collected);
                            }

                            @Override
                            public void onFailure(int i, String s) {
                                LogUtil.v(i + s);
                                isCollected = false;
                                ToastAlone.show(R.string.collect_fail);
                            }
                        });
                    }
                } else {
                    //已收藏，取消收藏
                    mMpthree.setIsCollect(0);
                    mMpthree.update(mContext, new UpdateListener() {
                        @Override
                        public void onSuccess() {
                            //收藏成功
                            isCollected = false;
                            ToastAlone.show(R.string.collect_cancel);
                            collect_mp_img.setBackgroundResource(R.mipmap.mp3_collect);
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            isCollected = true;
                            LogUtil.v(s);
                            ToastAlone.show(R.string.collect_cancel_fail);
                        }
                    });
                }
            }
        });
    }

    private void setTotalTime() {
        int totalTime = mMusicPlayer.getTotalTime();
        int m_time = totalTime / 60;
        int s_time = totalTime % 60;
        String m_time_s = "";
        String s_time_s = "";
        if (m_time < 10) {
            m_time_s = "0" + m_time;
        } else {
            m_time_s = m_time + "";
        }
        if (s_time < 10) {
            s_time_s = "0" + s_time;
        } else {
            s_time_s = s_time + "";
        }
        total_time_text.setText(m_time_s + ":" + s_time_s);
    }

    private void getMusicData(final FreshType type) {
        BmobQuery<MpThree> query = new BmobQuery<MpThree>();
        query.addWhereEqualTo("isCollect", 1);
        query.setLimit(Constans.Limit_Count);
        switch (type) {
            case Fresh:
                query.setSkip(0);
                break;
            case LoadMore:
                query.setSkip(mMusicAdapter.getCount());
                break;
        }
        query.findObjects(mContext, new FindListener<MpThree>() {
            @Override
            public void onSuccess(List<MpThree> list) {
                music_pulllistview.onRefreshComplete();
                switch (type) {
                    case Fresh:
                        mMusicAdapter.setData(list);
                        break;
                    case LoadMore:
                        mMusicAdapter.addData(list);
                        break;
                }
            }

            @Override
            public void onError(int i, String s) {
                music_pulllistview.onRefreshComplete();
                LogUtil.v("code=" + i + ",msg=" + s);
            }
        });
    }


    enum FreshType {
        Fresh, LoadMore
    }

    class MusicAdapter extends BaseAdapter {

        private List<MpThree> mpThreeList;
        private LayoutInflater mInflater;

        public MusicAdapter() {
            mpThreeList = new ArrayList<MpThree>();
            mInflater = LayoutInflater.from(mContext);
        }

        public void setData(List<MpThree> list) {
            if (list != null) {
                mpThreeList.clear();
                mpThreeList.addAll(list);
            }
            notifyDataSetChanged();
        }

        public void addData(List<MpThree> list) {
            if (list != null) {
                mpThreeList.addAll(list);
            }
            notifyDataSetChanged();
        }


        @Override
        public int getCount() {
            return mpThreeList.size();
        }

        @Override
        public MpThree getItem(int position) {
            return mpThreeList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            MpThree mpThree = getItem(position);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.music_item, null);
                viewHolder = new ViewHolder();
                viewHolder.music_read_textview = (TextView) convertView.findViewById(R.id.music_read_textview);
                viewHolder.music_textview = (TextView) convertView.findViewById(R.id.music_textview);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            try {
                MpThree bean = StoryApp.mDbManager.selector(MpThree.class).where("title", "like", "%" + mpThree.getMp3_file_url() + "%").findFirst();
                if (bean != null) {
//                LogUtil.w(bean.toString());
                    mpThree = bean;
                }

            } catch (DbException e) {
                e.printStackTrace();
            }
            viewHolder.music_textview.setText(mpThree.getMp3_name());
            if (mpThree.getIsReaded() == 0) {
                viewHolder.music_textview.setTextColor(Color.BLACK);
            } else if (mpThree.getIsReaded() == 1) {
                viewHolder.music_textview.setTextColor(Color.GRAY);
            }
            return convertView;
        }

        class ViewHolder {
            TextView music_read_textview, music_textview;
        }
    }


    private void getMobBeanId() {
        if (TextUtils.isEmpty(spUtil.getPersonObjid())) {
            return;
        }
        BmobQuery<MpThree> mobQuery = new BmobQuery<MpThree>();
        mobQuery.addWhereEqualTo("mp3_file_url", mMpthree.getMp3_file_url());
        mobQuery.addWhereEqualTo("personObjId", spUtil.getPersonObjid());
        mobQuery.findObjects(mContext, new FindListener<MpThree>() {
            @Override
            public void onSuccess(List<MpThree> list) {
                mCollectObjId = list.get(0).getObjectId();
                //证明已收藏
                collect_mp_img.setBackgroundResource(R.mipmap.mp3_collected);
                isCollected = true;
            }

            @Override
            public void onError(int i, String s) {
                isCollected = false;
                collect_mp_img.setBackgroundResource(R.mipmap.mp3_collect);
            }
        });
    }

    @Override
    protected void initToolbar() {

    }
}
