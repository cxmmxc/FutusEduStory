package com.terry.activity;

import android.view.View;
import android.widget.ImageView;

import com.terry.BaseActivity;
import com.terry.R;

/**
 * 作者：Terry.Chen on 2015/12/81448.
 * 邮箱：herewinner@163.com
 * 描述：登录界面
 */
public class LoginActivity extends BaseActivity {
    private ImageView back_img;
    @Override
    protected void initView() {
        setContentView(R.layout.login_layout);

        back_img = (ImageView) findViewById(R.id.back_img);
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void setListener() {
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initToolbar() {
    }
}
