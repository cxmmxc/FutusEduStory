package com.terry.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.terry.BaseFragment;
import com.terry.R;
import com.terry.activity.CollectActivity;
import com.terry.activity.HeadActivity;
import com.terry.activity.RecentReadActivity;

/**
 * 作者：Terry.Chen on 2015/12/31547.
 * 邮箱：herewinner@163.com
 * 描述：我的个人信息
 */
public class MineFragment extends BaseFragment {
    private View mRootView;

    private RelativeLayout header_layout;
    private SimpleDraweeView user_img;
    private TextView usename_text, email_text, collect_text, recent_look_text
            ,logout_text;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(mRootView == null) {
            mRootView = inflater.inflate(R.layout.user_fragment, null);
        }
        return mRootView;
    }

    @Override
    protected void initView() {
        header_layout = (RelativeLayout) mRootView.findViewById(R.id.header_layout);
        user_img = (SimpleDraweeView) mRootView.findViewById(R.id.user_img);
        usename_text = (TextView) mRootView.findViewById(R.id.usename_text);
        email_text = (TextView) mRootView.findViewById(R.id.email_text);
        collect_text = (TextView) mRootView.findViewById(R.id.collect_text);
        recent_look_text = (TextView) mRootView.findViewById(R.id.recent_look_text);
        logout_text = (TextView) mRootView.findViewById(R.id.logout_text);
    }

    @Override
    protected void initData() {
        usename_text.setText(spUtil.getPersonName());
        if (TextUtils.isEmpty(spUtil.getPersonHead())) {
            user_img.setImageURI(Uri.parse("res:///" + R.mipmap.setting_head_icon));
        }else {
            user_img.setImageURI(Uri.parse(spUtil.getPersonHead()));
        }
        if (TextUtils.isEmpty(spUtil.getPersonEmail())) {
            email_text.setText("未填写");
        }else {
            email_text.setText(spUtil.getPersonEmail());
        }
    }

    @Override
    protected void setListener() {
        header_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进入头像选择页面
                Intent intent = new Intent(mActivity, HeadActivity.class);
                startActivity(intent);
            }
        });

        collect_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进入我的收藏页面
                Intent intent = new Intent(mActivity, CollectActivity.class);
                startActivity(intent);
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

        logout_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //登出
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity, android.support.v7.appcompat.R.style.Base_Theme_AppCompat_Dialog_Alert);
                builder.setMessage("是否确认退出登录")
                        .setNegativeButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                spUtil.setPersonObjId("");
                                spUtil.setPersonHead("");
                                spUtil.setPersonPhone("");
                                spUtil.setPersonName("");
                                spUtil.setPersonEmail("");
//                                setResult(RESULT_OK);
//                                finish();
                            }
                        });
                builder.create().show();
            }
        });
    }
}
