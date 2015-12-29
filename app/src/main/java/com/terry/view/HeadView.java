package com.terry.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.terry.R;
import com.terry.util.MeasureTool;

/**
 * 作者：Terry.Chen on 2015/12/281020.
 * 邮箱：herewinner@163.com
 * 描述：自定义头像
 */
public class HeadView extends ImageView {
    private Context mContext;

    private Bitmap mBaseBitmap;

    private Bitmap mBackBitmap;
    private Paint mAplaPaint, mWhitePaint;
    private Canvas mFrontCanvas;
    private int mCircleRadius;

    private final static int MODE_NONE = 0x03;//平移模式
    private final static int MODE_TRANSLATE = 0x01;//平移模式
    private final static int MODE_SCALE = 0x02;//缩放旋转模式

    private int current_mode;

    private Matrix mCurrentMatrix, mSaveMatrix;

    private Bitmap mBitmap;

    private PointF mStartP, mMindP;

    private float mRotate, mSaveRotate, mPremove;

    private int mScW, mScH;

    private boolean isTwoPoint;
    private Bitmap output;//用于承接切割出来的图片内容，也就是说这个就是返回来的圆形bitmap

    private Canvas canvas;//切割圆形bitmap的画布
    private int mAvatorWidth;
    private Paint paint;
    private RectF rectF;
    private Rect dst;
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
//        testBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.splash1);

        int[] screenWH = MeasureTool.getScreenWH((Activity) mContext);
        mScW = screenWH[0];
        mScH = screenWH[1];

        mCircleRadius = mScW * 2 / 5;

        mAvatorWidth = mCircleRadius * 2;

        output = Bitmap.createBitmap(mAvatorWidth,
                mAvatorWidth, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(output);

        current_mode = MODE_NONE;
        mCurrentMatrix = new Matrix();
        mSaveMatrix = new Matrix();
        mStartP = new PointF();
        mMindP = new PointF();

        mBaseBitmap = Bitmap.createBitmap(mScW, mScH, Bitmap.Config.ARGB_8888);

        mAplaPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mAplaPaint.setARGB(180, 0, 0, 0);

        mWhitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mWhitePaint.setColor(Color.WHITE);

        mFrontCanvas = new Canvas(mBaseBitmap);
        mFrontCanvas.drawRect(0, 0, mScW, mScH, mAplaPaint);
        mWhitePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.XOR));
        mFrontCanvas.drawCircle(mScW / 2, mScH / 2, mCircleRadius, mWhitePaint);


        paint = new Paint();
        paint.setAntiAlias(true);

        paint.setColor(0xFFFFFFFF);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        if (testBitmap != null) {
//            canvas.drawBitmap(testBitmap, 0, 0, null);
//        }
        canvas.drawBitmap(mBaseBitmap, 0, 0, null);
    }

    public void setBitmpData(Bitmap bitmap) {
        if (bitmap != null) {
//            mBaseBitmap = bitmap;
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, mScW, mScH, true);
            setImageBitmap(scaledBitmap);
        }
//        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mSaveMatrix.set(mCurrentMatrix);
                mStartP.set(event.getX(), event.getY());
                current_mode = MODE_TRANSLATE;
                isTwoPoint = false;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                //计算两点 的距离
                mPremove = calSpacing(event);
                if (mPremove > 15f) {
                    mSaveMatrix.set(mCurrentMatrix);
                    calMid(mMindP, event);
                    current_mode = MODE_SCALE;
                }
                mSaveRotate = calRotate(event);
                isTwoPoint = true;

            case MotionEvent.ACTION_MOVE:
                if (current_mode == MODE_TRANSLATE) {
                    mCurrentMatrix.set(mSaveMatrix);
                    float dx = event.getX() - mStartP.x;
                    float dy = event.getY() - mStartP.y;
                    mCurrentMatrix.postTranslate(dx, dy);
                } else if (current_mode == MODE_SCALE && event.getPointerCount() == 2) {
                    float cuSpace = calSpacing(event);
                    mCurrentMatrix.set(mSaveMatrix);
                    if(cuSpace > 10f) {
                        //大于10，缩放
                        float scale = cuSpace / mPremove;
                        mCurrentMatrix.postScale(scale, scale, mMindP.x, mMindP.y);
                    }
                    if(isTwoPoint) {
                        mRotate = calRotate(event);
                        float rotate = mRotate - mSaveRotate;
                        mCurrentMatrix.postRotate(rotate, mScW/2, mScH/2);
                    }

                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                isTwoPoint = false;
                current_mode = MODE_NONE;
                break;
        }
        setImageMatrix(mCurrentMatrix);
        return true;
    }

    private float calRotate(MotionEvent event) {
        float du_x = event.getX(0) - event.getX(1);
        float du_y = event.getY(0) - event.getY(1);
        double v = Math.atan2(du_y, du_x);
        return (float) Math.toDegrees(v);
    }

    //计算中点
    private void calMid(PointF mMindP, MotionEvent event) {
        float du_x = (event.getX(0) + event.getX(1)) / 2;
        float du_y = (event.getY(0) + event.getY(1)) / 2;
        mMindP.set(du_x, du_y);
    }

    //计算两点的距离
    private static float calSpacing(MotionEvent event) {
        float du_x = event.getX(0) - event.getX(1);
        float du_y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(du_x * du_x + du_y * du_y);
    }

    /**
     * 转换图片成圆形
     * @param
     * @return
     */
    public Bitmap toRoundBitmap() {

//        canvas.drawRoundRect(rectF, mCircleRadius, mCircleRadius, paint);
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
//        canvas.drawBitmap(mBitmap, src, dst, paint);
//        return output;
        return null;
    }
}
