package com.mrl.custom.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;

/**
 * 波浪线
 *
 * @author 李立
 * @date 2019/2/21
 */
public class SingleWaveView extends View {

    //画波浪
    private Paint mWavePaint;
    //画背景
    private Paint mBackPaint;
    //画波浪的路径
    private Path mWavePath;

    // view宽度
    private int mViewWidth;
    // view高度
    private int mViewHeight;
    // 波浪高低偏移量
    private int mOffset = 100;
    // X轴，view的偏移量
    private int mXOffset = 0;
    // view的Y轴高度
    private int mViewY = 0;
    // 波浪速度
    private int mWaveSpeed = 50;
    //水位高度
    private int mDepth;

    private Bitmap mBitmap;
    private Canvas mCanvas;
    private ValueAnimator mAnimator;

    public SingleWaveView(Context context) {
        super(context);
        init(context);
    }

    public SingleWaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void setDepth(int depth) {
        this.mDepth = depth;
    }

    private void init(Context context) {

        mWavePaint = new Paint();
        mWavePaint.setColor(Color.GREEN);
        mWavePaint.setAntiAlias(true);
        mWavePaint.setStyle(Paint.Style.FILL);
        //使用Xfermode获取重叠部分
        mWavePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        mBackPaint = new Paint();
        mBackPaint.setColor(Color.WHITE);
        mBackPaint.setStyle(Paint.Style.FILL);
        mBackPaint.setAntiAlias(true);
        mBackPaint.setStrokeWidth(5);

        mWavePath = new Path();

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mViewWidth = wm.getDefaultDisplay().getWidth();

        mAnimator = new ValueAnimator();
        mAnimator.setFloatValues(0, mViewWidth);
        mAnimator.setDuration(mWaveSpeed * 20);
        mAnimator.setRepeatCount(-1);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float change = (float) animation.getAnimatedValue();
                mXOffset = (int) change;
                invalidate();
            }
        });
        mAnimator.start();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
        //创建一张空白图片
        mBitmap = Bitmap.createBitmap(mViewWidth, mViewHeight, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    private int measureWidth(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        //设置一个默认值，就是这个View的默认宽度为500，这个看我们自定义View的要求
        int result = 500;
        if (specMode == MeasureSpec.AT_MOST) {//相当于我们设置为wrap_content
            result = specSize;
        } else if (specMode == MeasureSpec.EXACTLY) {//相当于我们设置为match_parent或者为一个具体的值
            result = specSize;
        }
        mViewWidth = result;
        return result;
    }

    private int measureHeight(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        int result = 500;
        if (specMode == MeasureSpec.AT_MOST) {
            result = specSize;
        } else if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        }
        mViewHeight = specSize;
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mWavePath.reset();

        mViewY = mViewHeight / 2;

        mCanvas.drawCircle(mViewWidth / 2, mViewHeight / 2, 300, mBackPaint);


        //1.描点的顺序必须是一条线似的，不能折点
        //2.只能有一个moveto 方法，如果第二个moveto方法的方向是正常的反方向也不能形成上升的水波。
        //3.lineto 方法也是先从最后一个点开始，然后纵坐标和View的高度一样，然后再lineto 到起点的相同高度。
        //4.自定义的canvas 设置setXfermode
        //5.自定义的canvas 画显示的背景
        //6.自定义的canvas 画path.
        //7.系统的canvas 画bitmap

        // 绘制屏幕外的波浪
        mWavePath.moveTo(mXOffset - mViewWidth, mViewY - mDepth);
        mWavePath.quadTo(mViewWidth / 4 + mXOffset - mViewWidth, mViewY - mOffset - mDepth, mViewWidth / 2 + mXOffset - mViewWidth, mViewY - mDepth);
        mWavePath.quadTo(mViewWidth / 4 * 3 + mXOffset - mViewWidth, mViewY + mOffset - mDepth, mViewWidth + mXOffset - mViewWidth, mViewY - mDepth);

        // 绘制屏幕内的波浪
        mWavePath.quadTo(mViewWidth / 4 + mXOffset, mViewY - mOffset - mDepth, mViewWidth / 2 + mXOffset, mViewY - mDepth);
        mWavePath.quadTo(mViewWidth / 4 * 3 + mXOffset, mViewY + mOffset - mDepth, mViewWidth + mXOffset, mViewY - mDepth);

        //画波浪线下边的部分
        mWavePath.lineTo(mViewWidth + mXOffset, mViewHeight);
        mWavePath.lineTo(-mViewWidth + mXOffset, mViewHeight);

        mCanvas.drawPath(mWavePath, mWavePaint);

        canvas.drawBitmap(mBitmap, 0, 0, null);

    }

    /**
     * 设置 波浪的高度
     */
    public void setWaveHeight(int waveHeight) {
        mOffset = waveHeight;
    }

    /**
     * 获取 波浪的高度
     */
    public int getWaveHeight() {
        return mOffset;
    }

    /**
     * 设置 波浪的速度
     */
    public void setmWaveSpeed(int speed) {
        mWaveSpeed = 2000 - speed * 20;
        mAnimator.setDuration(mWaveSpeed);
    }

    /**
     * 获取 波浪的速度
     */
    public int getmWaveSpeed() {
        return mWaveSpeed;
    }
}
