package com.terry.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;

import com.terry.R;
import com.terry.util.MeasureTool;

/**
 * 作者：Terry.Chen on 2015/12/281020.
 * 邮箱：herewinner@163.com
 * 描述：自定义头像
 */
public class HeadView extends View {
    private Context mContext;

    private Bitmap mBaseBitmap;

    private Bitmap mBackBitmap;
    private int mScWidth, mScHeight;
    private Paint mAplaPaint, mWhitePaint;
    private Canvas mFrontCanvas;
    private int mCircleRadius;

    private Bitmap testBitmap;

    public HeadView(Context context) {
        this(context, null);
    }

    public HeadView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public HeadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initData();
    }

    private void initData() {
        mCircleRadius = 200;

        testBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.splash1);

        int[] screenWH = MeasureTool.getScreenWH((Activity) mContext);
        mScWidth = screenWH[0];
        mScHeight = screenWH[1];

        mBaseBitmap = Bitmap.createBitmap(mScWidth, mScHeight, Bitmap.Config.ARGB_8888);

        mAplaPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mAplaPaint.setARGB(180, 0, 0, 0);

        mWhitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mWhitePaint.setColor(Color.WHITE);

        mFrontCanvas = new Canvas(mBaseBitmap);
        mFrontCanvas.drawRect(0, 0, mScWidth, mScHeight, mAplaPaint);
        mWhitePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.XOR));
        mFrontCanvas.drawCircle(mScWidth / 2, mScHeight / 2, mCircleRadius, mWhitePaint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(testBitmap, 0, 0, null);
        canvas.drawBitmap(mBaseBitmap, new Matrix(), null);
    }

    public void setBitmpData(Bitmap bitmap) {
        if (bitmap != null) {
            mBaseBitmap = bitmap;
        }
        invalidate();
    }
}
