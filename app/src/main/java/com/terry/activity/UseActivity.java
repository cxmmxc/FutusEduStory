package com.terry.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.interfaces.DraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.terry.BaseActivity;
import com.terry.R;

/**
 * 作者：Terry.Chen on 2015/12/111315.
 * 邮箱：herewinner@163.com
 * 描述：用户详情
 */
public class UseActivity extends BaseActivity {
    private ImageView back_img, img_user;
    private RelativeLayout header_layout;
    private SimpleDraweeView user_img;
    private TextView usename_text, email_text, collect_text, recent_look_text
            ,logout_text;

    @Override
    protected void initView() {
        setContentView(R.layout.user_layout);
        back_img = (ImageView) findViewById(R.id.back_img);
        header_layout = (RelativeLayout) findViewById(R.id.header_layout);
        user_img = (SimpleDraweeView) findViewById(R.id.user_img);
        usename_text = (TextView) findViewById(R.id.usename_text);
        email_text = (TextView) findViewById(R.id.email_text);
        collect_text = (TextView) findViewById(R.id.collect_text);
        recent_look_text = (TextView) findViewById(R.id.recent_look_text);
        logout_text = (TextView) findViewById(R.id.logout_text);
        img_user = (ImageView) findViewById(R.id.img_user);
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
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        header_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进入头像选择页面
                startActivityForResult(new Intent(mContext, HeadActivity.class), 110);
            }
        });

        collect_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进入我的收藏页面
                Intent intent = new Intent(mContext, CollectActivity.class);
                startActivity(intent);
            }
        });

        recent_look_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        logout_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //登出
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext, android.support.v7.appcompat.R.style.Base_Theme_AppCompat_Dialog_Alert);
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
                                setResult(RESULT_OK);
                                finish();
                            }
                        });
                builder.create().show();
            }
        });
    }

    @Override
    protected void initToolbar() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 110) {
                if (HeadActivity.mHeadBitmap != null) {
                    img_user.setImageBitmap(HeadActivity.mHeadBitmap);
                }
            }
        }
    }
}
