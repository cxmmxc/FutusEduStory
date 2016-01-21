package com.terry.futus.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.terry.futus.R;

/**
 * 作者：Terry.Chen on 2015/12/111542.
 * 邮箱：herewinner@163.com
 * 描述：#TODO
 */
public class GifLoadingDialog extends Dialog implements DialogInterface.OnDismissListener{
    private Context mContext;
    private ImageView load_img;
    private TextView load_text;
    private AnimationDrawable mAnimationDrawable;
    public GifLoadingDialog(Context context, int themeResId) {
        super(context, R.style.style_loadingdialog);
        mContext = context;
        initView();
        initData();
    }

    private void initData() {
        mAnimationDrawable = (AnimationDrawable) mContext.getResources().getDrawable(R.drawable.gif_loading);
        load_img.setBackgroundDrawable(mAnimationDrawable);
        mAnimationDrawable.start();
    }

    private void initView() {
        setContentView(R.layout.gif_loading_layout);
        load_img = (ImageView) findViewById(R.id.load_img);
        load_text = (TextView) findViewById(R.id.load_text);
    }

    public void setLoadText(String txt) {
        load_text.setText(txt);
    }
    public void setLoadText(int restxt) {
        load_text.setText(mContext.getResources().getString(restxt));
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        mAnimationDrawable.stop();
    }
}
