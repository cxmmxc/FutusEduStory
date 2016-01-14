package com.terry.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;

import com.terry.BaseActivity;
import com.terry.R;
import com.terry.view.HeadView;

/**
 * 作者：Terry.Chen on 2015/12/281039.
 * 邮箱：herewinner@163.com
 * 描述：头像界面
 */
public class HeadActivity extends BaseActivity {
    private HeadView head_view;
    public static Bitmap mHeadBitmap;
    private ImageView user_img_head;
    @Override
    protected void initView() {
//        HeadView headView = new HeadView(this);
//        setContentView(headView);
        setContentView(R.layout.head_view_layout);
        head_view = (HeadView) findViewById(R.id.head_view);
        user_img_head = (ImageView) findViewById(R.id.user_img_head);
    }

    @Override
    protected void initData() {
        mHeadBitmap = null;
        head_view.setBitmpData(BitmapFactory.decodeResource(getResources(), R.mipmap.splash1));
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void initToolbar() {

    }

    public void EnterOnclick(View view) {
        Bitmap bitmap = head_view.toRoundBitmap();
        if (bitmap != null) {
            user_img_head.setImageBitmap(bitmap);
            mHeadBitmap = bitmap;
            //存bitmap到本地
        }
//        setResult(RESULT_OK);
//        finish();
    }
}
