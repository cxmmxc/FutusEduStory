package com.terry.futus.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.terry.futus.BaseFragment;
import com.terry.futus.R;
import com.terry.futus.activity.CollectActivity;
import com.terry.futus.activity.LoginActivity;
import com.terry.futus.activity.MusciCollectActivity;
import com.terry.futus.activity.RecentReadActivity;

/**
 * 作者：Terry.Chen on 2016/1/261048.
 * 邮箱：herewinner@163.com
 * 描述：新版我的页面
 */
public class Mine_NewFragment extends BaseFragment {
    private View mRootView;
    private TextView user_name, collect_text, music_collect_text, recent_look_text,
            feedback_text, update_text;
    private SimpleDraweeView user_img;
    private RelativeLayout header_layout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.mine_new_layout, null);
        }
        return mRootView;
    }

    @Override
    protected void initView() {
        user_name = (TextView) mRootView.findViewById(R.id.user_name);
        user_img = (SimpleDraweeView) mRootView.findViewById(R.id.user_img);
        header_layout = (RelativeLayout) mRootView.findViewById(R.id.header_layout);

        collect_text = (TextView) mRootView.findViewById(R.id.collect_text);
        music_collect_text = (TextView) mRootView.findViewById(R.id.music_collect_text);
        recent_look_text = (TextView) mRootView.findViewById(R.id.recent_look_text);
        feedback_text = (TextView) mRootView.findViewById(R.id.feedback_text);
        update_text = (TextView) mRootView.findViewById(R.id.update_text);
    }

    @Override
    protected void initData() {
        if (TextUtils.isEmpty(spUtil.getPersonObjid())) {
            user_name.setText("未登录");
            user_img.setImageURI(Uri.parse("res:///" + R.mipmap.setting_head_icon));
        } else {
            user_name.setText(spUtil.getPersonName());
            user_img.setImageURI(Uri.parse(spUtil.getPersonHead()));
        }

    }

    @Override
    protected void setListener() {
        collect_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(spUtil.getPersonObjid())) {
                    Intent intent = new Intent(mActivity, LoginActivity.class);
                    startActivity(intent);
                } else {
                    //进入我的收藏页面
                    Intent intent = new Intent(mActivity, CollectActivity.class);
                    startActivity(intent);
                }
            }
        });

        music_collect_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(spUtil.getPersonObjid())) {
                    Intent intent = new Intent(mActivity, LoginActivity.class);
                    startActivity(intent);
                } else {
                    //进入我的收藏页面
                    Intent intent = new Intent(mActivity, MusciCollectActivity.class);
                    startActivity(intent);
                }
            }
        });

        recent_look_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进入最近浏览
                Intent intent = new Intent(mActivity, RecentReadActivity.class);
                startActivity(intent);
            }
        });

        feedback_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //意见反馈
            }
        });
    }
}
