package com.terry.fragment;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.terry.BaseFragment;
import com.terry.Constans;
import com.terry.R;
import com.terry.bean.MpThree;
import com.terry.inter.IOncomplete;
import com.terry.inter.IOnprapared;
import com.terry.util.MusicPlayer;
import com.terry.util.ToastAlone;

import org.xutils.common.util.LogUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;

/**
 * 从服务器获取MusicBean的数据信息
 */
public class MusicFragment extends BaseFragment{

    private View mRootView;
    private PullToRefreshListView music_pulllistview;
    private ListView music_listview;
    private FreshType mFreshType;
    private MusicAdapter mMusicAdapter;
    private ImageView play_pause_img;
    private TextView music_title_text, total_time_text, current_time_text;
    private boolean isPlaying;
    private MpThree mMpthree;
    private MusicPlayer mMusicPlayer;
    private int mCurrentItem;

    private int mTotalCount;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.music_frag_layout, null);
        }
        return mRootView;
    }

    @Override
    protected void initView() {
        music_pulllistview = (PullToRefreshListView) mRootView.findViewById(R.id.music_pulllistview);
        music_listview = music_pulllistview.getRefreshableView();
        play_pause_img = (ImageView) mRootView.findViewById(R.id.play_pause_img);
        music_title_text = (TextView) mRootView.findViewById(R.id.music_title_text);
        total_time_text = (TextView) mRootView.findViewById(R.id.total_time_text);
        current_time_text = (TextView) mRootView.findViewById(R.id.current_time_text);

    }

    @Override
    protected void initData() {
        music_pulllistview.setMode(PullToRefreshBase.Mode.BOTH);
        mFreshType = FreshType.Fresh;
        mMusicAdapter = new MusicAdapter();
        music_listview.setAdapter(mMusicAdapter);
        mMusicPlayer = MusicPlayer.getInstance(mActivity);
        getTotalCount();
    }

    private void getTotalCount() {
        BmobQuery<MpThree> query = new BmobQuery<MpThree>();
        query.count(mActivity, MpThree.class, new CountListener() {
            @Override
            public void onSuccess(int i) {
                LogUtil.i("count=" + i);
                mTotalCount = i;
                getMusicData(FreshType.Fresh);
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }

    Handler mHander = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    music_pulllistview.onRefreshComplete();
                    ToastAlone.show(R.string.load_all);
                    break;
                case 2:
                    int currentTime = mMusicPlayer.getCurrentTime();
                    int m_time = currentTime / 60;
                    int s_time = currentTime % 60;
                    String m_time_s = "";
                    String s_time_s = "";
                    if (m_time < 10) {
                        m_time_s = "0" + m_time;
                    } else {
                        m_time_s = m_time+"";
                    }
                    if (s_time < 10) {
                        s_time_s = "0" + s_time;
                    } else {
                        s_time_s = s_time+"";
                    }
                    current_time_text.setText(m_time_s + ":" + s_time_s);
                    mHander.sendEmptyMessageDelayed(2, 500);
                    break;
            }
        }
    };

    @Override
    protected void setListener() {
        mMusicPlayer.setOnpreparedListener(new IOnprapared() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                //准备完成再开始播放
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
                //进入播放页面
                mMpthree = mMusicAdapter.getItem(position - 1);
                mCurrentItem = position - 1;
                music_title_text.setText(mMpthree.getMp3_name());
                //开始播放此歌曲
                try {
                    mMusicPlayer.playUrlMusic(mMpthree.getMp3_file_url());
                    setTotalTime();
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
                LogUtil.v("setOncomPleteListener");
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
                    setTotalTime();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setTotalTime(){
        int totalTime = mMusicPlayer.getTotalTime();
        int m_time = totalTime / 60;
        int s_time = totalTime % 60;
        String m_time_s = "";
        String s_time_s = "";
        if (m_time < 10) {
            m_time_s = "0" + m_time;
        } else {
            m_time_s = m_time+"";
        }
        if (s_time < 10) {
            s_time_s = "0" + s_time;
        } else {
            s_time_s = s_time+"";
        }
        total_time_text.setText(m_time_s+":"+s_time_s);
    }

    private void getMusicData(final FreshType type){
        BmobQuery<MpThree> query = new BmobQuery<MpThree>();
        query.setLimit(Constans.Limit_Count);
        switch (type) {
            case Fresh:
                query.setSkip(0);
                break;
            case LoadMore:
                query.setSkip(mMusicAdapter.getCount());
                break;
        }
        query.findObjects(mActivity, new FindListener<MpThree>() {
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
                LogUtil.v("code="+i+",msg="+s);
            }
        });
    }


    enum FreshType{
        Fresh,LoadMore
    }

    class MusicAdapter extends BaseAdapter {

        private List<MpThree> mpThreeList;
        private LayoutInflater mInflater;
        public MusicAdapter() {
            mpThreeList = new ArrayList<MpThree>();
            mInflater = LayoutInflater.from(mActivity);
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
            viewHolder.music_textview.setText(mpThree.getMp3_name());

            return convertView;
        }

        class ViewHolder{
            TextView music_read_textview, music_textview;
        }
    }
}
