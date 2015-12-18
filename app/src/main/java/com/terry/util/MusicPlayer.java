package com.terry.util;

/**
 * Created by Terry.Chen on 2015/5/19 15:16.
 * Description:背景音乐的播放
 * Email:cxm_lmz@163.com
 */

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import com.terry.inter.IOnSeekComplete;
import com.terry.inter.IOncomplete;
import com.terry.inter.IOnprapared;

import org.xutils.common.util.LogUtil;

import java.io.IOException;

/**
 *
 * This class is used for controlling background music
 *
 */
public class MusicPlayer implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnPreparedListener{
    private static MusicPlayer backgroundMusic = null;
    private static final String TAG = "Bg_Music";
    private float mLeftVolume;
    private float mRightVolume;
    private Context mContext;
    private MediaPlayer mBackgroundMediaPlayer;
    private boolean mIsPaused;
    private String mCurrentPath;
    private static int mMusicCount = 3;

    private MusicPlayer(Context context) {
        this.mContext = context;
        initData();
    }

    public static MusicPlayer getInstance(Context context) {
        if (backgroundMusic == null) {
            backgroundMusic = new MusicPlayer(context);
        }
        return backgroundMusic;
    }

    // 初始化一些数据
    private void initData() {
        mLeftVolume = 0.5f;
        mRightVolume = 0.5f;
        mBackgroundMediaPlayer = new MediaPlayer();
        mBackgroundMediaPlayer.setLooping(false);
        mIsPaused = false;
        mCurrentPath = null;
        mBackgroundMediaPlayer.setOnCompletionListener(this);
    }

    /**
     *
     * @param url
     */
    public void playUrlMusic(String url) throws IOException {
        mBackgroundMediaPlayer.reset();
        mBackgroundMediaPlayer.setDataSource(mContext, Uri.parse(url));
        mBackgroundMediaPlayer.prepare();
        mBackgroundMediaPlayer.start();
    }

    public void seekToMusic(int time_duration) {
        mBackgroundMediaPlayer.seekTo(time_duration);
        mBackgroundMediaPlayer.setOnSeekCompleteListener(this);
    }

    public int getTotalTime() {
        int duration = mBackgroundMediaPlayer.getDuration();
        if (duration != -1) {
            duration = duration / 1000;
        }
        return duration;
    }

    public int getCurrentTime() {
        int currentPosition = mBackgroundMediaPlayer.getCurrentPosition();
        return currentPosition / 1000;
    }


    /**
     * 停止播放背景音乐
     */
    public void stopUrlMusic() {
        if (mBackgroundMediaPlayer != null) {
            mBackgroundMediaPlayer.stop();
            // should set the state, if not , the following sequence will be
            // error
            // play -> pause -> stop -> resume
            this.mIsPaused = false;
        }
    }

    /**
     * 暂停播放背景音乐
     */
    public void pauseUrlMusic() {
        if (mBackgroundMediaPlayer != null
                && mBackgroundMediaPlayer.isPlaying()) {
            mBackgroundMediaPlayer.pause();
            this.mIsPaused = true;
        }
    }

    /**
     * 继续播放背景音乐
     */
    public void resumeUrlMusic() {
        if (mBackgroundMediaPlayer != null && this.mIsPaused) {
            mBackgroundMediaPlayer.start();
            this.mIsPaused = false;
        }
    }

    /**
     * 重新播放背景音乐
     * 相当于重复播放的功能
     */
    public void rewindUrlMusic() {
        if (mBackgroundMediaPlayer != null) {
            mBackgroundMediaPlayer.stop();
            try {
                mBackgroundMediaPlayer.prepare();
                mBackgroundMediaPlayer.seekTo(0);
                mBackgroundMediaPlayer.start();
                this.mIsPaused = false;
            } catch (Exception e) {
                Log.e(TAG, "rewindBackgroundMusic: error state");
            }
        }
    }

    /**
     * 判断背景音乐是否正在播放
     *
     * @return：返回的boolean值代表是否正在播放
     */
    public boolean isUrlMusicPlaying() {
        boolean ret = false;
        if (mBackgroundMediaPlayer == null) {
            ret = false;
        } else {
            ret = mBackgroundMediaPlayer.isPlaying();
        }
        return ret;
    }

    /**
     * 结束背景音乐，并释放资源
     */
    public void end() {
        if (mBackgroundMediaPlayer != null) {
            mBackgroundMediaPlayer.release();
        }
        // 重新“初始化数据”
        initData();
    }

    /**
     * 得到背景音乐的“音量”
     *
     * @return
     */
    public float getUrlMusicVolume() {
        if (this.mBackgroundMediaPlayer != null) {
            return (this.mLeftVolume + this.mRightVolume) / 2;
        } else {
            return 0.0f;
        }
    }

    /**
     * 设置背景音乐的音量
     *
     * @param volume
     *            ：设置播放的音量，float类型
     */
    public void setUrlMusicVolume(float volume) {
        this.mLeftVolume = this.mRightVolume = volume;
        if (this.mBackgroundMediaPlayer != null) {
            this.mBackgroundMediaPlayer.setVolume(this.mLeftVolume,
                    this.mRightVolume);
        }
    }

    private IOncomplete mOncomplete;

    public void setOncomPleteListener(IOncomplete comlister) {
        mOncomplete = comlister;
    }

    private IOnSeekComplete mOnSeekComplete;
    public void setOnSeekOnCompleteListener(IOnSeekComplete onListener){
        mOnSeekComplete = onListener;
    }

    private IOnprapared mOnPrepared;

    public void setOnpreparedListener(IOnprapared prepare) {
        mOnPrepared = prepare;
    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        LogUtil.v("onComplete");
        //接着播放下一首
        if (mOncomplete != null) {
            mOncomplete.oncomplete(mp);
        }
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
        if (mOnSeekComplete != null) {
            mOnSeekComplete.seekOncomplete(mp);
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        LogUtil.v("onPrepared");
        mOnPrepared.onPrepared(mp);
    }
}

