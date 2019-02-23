package com.mrl.custom.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.mrl.custom.R;

/**
 * @author 李立
 * @date 2019/2/18
 * 圆中的波浪
 */
public class WaveView extends View {

    private Paint mBgCirclePaint;
    private Paint mCirclePaint;
    private Paint mWavePaint;
    private Paint mTextPaint;
    private Canvas mCanvas;
    //贝塞尔曲线
    private Path mPath;

    private int mCircleColor;
    private int mCircleBackColor;
    private float mCircleRadius;
    private int mWaveColor;
    private int mTextColor;
    private float mTextSize;

    private int mWaveWidth, mWaveHeight;
    private Bitmap mBitmap;

    //当前进度
    private int currentProgress = 10;
    //总的进度
    private int maxProgress = 100;
    //属性动画
    ValueAnimator animator = null;
    //水位上升的高度
    private float depth;
    //振幅
    private float waveRipple = 0;

    private boolean mIsLog = false;


    private PointF mStartPoint, mSecondPoint, mThirdPoint, mFourthPoint, mEndPoint;
    private PointF mControllerPoint1, mControllerPoint2, mControllerPoint3, mControllerPoint4;


    public WaveView(Context context) {
        this(context, null);
    }

    public WaveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs, defStyleAttr);
    }

    private void initAttr(Context context, AttributeSet attrs, int defStyleAttr) {

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.WaveView);
        if (array != null) {

            mCircleColor = array.getColor(R.styleable.WaveView_circleColor, 0);
            mCircleBackColor = array.getColor(R.styleable.WaveView_circleBackgroundColor, 0);
            mCircleRadius = array.getDimension(R.styleable.WaveView_circleRadius, 30);
            mWaveColor = array.getColor(R.styleable.WaveView_waveColor, 0);
            mTextColor = array.getColor(R.styleable.WaveView_percentTextColor, 0);
            mTextSize = array.getDimension(R.styleable.WaveView_percentTextSize, 14);
            array.recycle();
        }



        initData();

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //在自己定义的画布上画出背景圆
        mCanvas.drawCircle(mWaveWidth / 2, mWaveHeight / 2, mCircleRadius, mBgCirclePaint);

        mPath.reset();
        mPath.moveTo(mStartPoint.x, mStartPoint.y);
        mPath.quadTo(mControllerPoint1.x, mControllerPoint1.y, mSecondPoint.x, mSecondPoint.y);
        mPath.quadTo(mControllerPoint2.x, mControllerPoint2.y, mThirdPoint.x, mThirdPoint.y);
        mPath.quadTo(mControllerPoint3.x, mControllerPoint3.y, mFourthPoint.x, mFourthPoint.y);
        mPath.quadTo(mControllerPoint4.x, mControllerPoint4.y, mEndPoint.x, mEndPoint.y);

        mPath.lineTo(mEndPoint.x, mWaveHeight);
        mPath.lineTo(-mWaveWidth, mWaveHeight);

        //绘制水波纹
        mCanvas.drawPath(mPath, mWavePaint);

        //将画好的圆绘制再画布上
        canvas.drawBitmap(mBitmap, 0, 0, null);

        //绘制圆
        canvas.drawCircle(mWaveWidth / 2, mWaveHeight / 2, mCircleRadius, mCirclePaint);

        if (currentProgress <= 0) {
            waveRipple = 0;
        } else if (currentProgress > 0 && currentProgress < maxProgress) {
            waveRipple = 15;
        } else if (currentProgress == maxProgress) {
            waveRipple = 0;
        } else if (currentProgress > maxProgress && animator.isRunning()) {
            currentProgress = maxProgress;
            animator.cancel();
        }

        //绘制进度
        String text = currentProgress + "%";
        canvas.drawText(text, (mWaveWidth / 2 - mTextSize), (mWaveHeight / 2 + mTextSize / 4), mTextPaint);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setWidthHeight(widthMeasureSpec, heightMeasureSpec);

    }

    /**
     * 控制宽高，解决自定义wrap_content 还是match_parent 效果的问题
     */
    private void setWidthHeight(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredWidth = (int) (mCircleRadius * 2);
        int desiredHeight = (int) (mCircleRadius * 2);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            mWaveWidth = widthSize;
        }
        else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            mWaveWidth = Math.min(desiredWidth, widthSize);
        }
        else {
            //Be whatever you want
            mWaveWidth = desiredWidth;
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            mWaveHeight = heightSize;
        }
        else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            mWaveHeight = Math.min(desiredHeight, heightSize);
        }
        else {
            //Be whatever you want
            mWaveHeight = desiredHeight;
        }

        //创建一张空白图片
        mBitmap = Bitmap.createBitmap(mWaveWidth, mWaveHeight, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

        reset();

        //MUST CALL THIS
        setMeasuredDimension(mWaveWidth, mWaveHeight);
    }

    /**
     * 初始化点
     */
    private void reset() {
        mStartPoint = new PointF(-mWaveWidth, mWaveHeight);
        mSecondPoint = new PointF(-mWaveWidth / 2, mWaveHeight);
        mThirdPoint = new PointF(0, mWaveHeight);
        mFourthPoint = new PointF(mWaveWidth / 2, mWaveHeight);
        mEndPoint = new PointF(mWaveWidth, mWaveHeight);

        mControllerPoint1 = new PointF(-mWaveWidth / 4, mWaveHeight);
        mControllerPoint2 = new PointF(-mWaveWidth * 3 / 4, mWaveHeight);
        mControllerPoint3 = new PointF(mWaveWidth / 4, mWaveHeight);
        mControllerPoint4 = new PointF(mWaveWidth * 3 / 4, mWaveHeight);
    }

    /**
     * 初始化画笔
     */
    private void initData() {

        //初始化背景圆画笔
        mBgCirclePaint = new Paint();
        //抗锯齿
        mBgCirclePaint.setAntiAlias(true);
        //设置背景圆的背景色
        mBgCirclePaint.setColor(mCircleBackColor);
        //设置充满
        mBgCirclePaint.setStyle(Paint.Style.FILL);

        //外部圆
        mCirclePaint = new Paint();
        mCirclePaint.setColor(mCircleColor);
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStyle(Paint.Style.STROKE);

        mWavePaint = new Paint();
        mWavePaint.setColor(mWaveColor);
        mWavePaint.setAntiAlias(true);
        mWavePaint.setStyle(Paint.Style.FILL);
        //使用Xfermode获取重叠部分
        mWavePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        mTextPaint = new Paint();
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setAntiAlias(true);

        //初始化贝塞尔曲线
        mPath = new Path();

    }

    public void start() {
        if (animator == null) {
            reset();
            //开启动画效果
            startAnimator();
        }
    }

    /**
     * 开始动画
     */
    private void startAnimator() {
        animator = ValueAnimator.ofFloat(mStartPoint.x, 0);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(2000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mStartPoint.x = (Float) animation.getAnimatedValue();
                mStartPoint = new PointF(mStartPoint.x, mWaveHeight - depth);
                mSecondPoint = new PointF(mStartPoint.x + mWaveWidth / 2, mWaveHeight - depth);
                mThirdPoint = new PointF(mSecondPoint.x + mWaveWidth / 2, mWaveHeight - depth);
                mFourthPoint = new PointF(mThirdPoint.x + mWaveWidth / 2, mWaveHeight - depth);
                mEndPoint = new PointF(mFourthPoint.x + mWaveWidth / 2, mWaveHeight - depth);
                mControllerPoint1 = new PointF(mStartPoint.x + mWaveWidth / 4, mWaveHeight - depth + waveRipple);
                mControllerPoint2 = new PointF(mSecondPoint.x + mWaveWidth / 4, mWaveHeight - depth - waveRipple);
                mControllerPoint3 = new PointF(mThirdPoint.x + mWaveWidth / 4, mWaveHeight - depth + waveRipple);
                mControllerPoint4 = new PointF(mFourthPoint.x + mWaveWidth / 4, mWaveHeight - depth - waveRipple);

                if(!mIsLog){
                    Log.e("WaveView的log:","startPoint:"+ mStartPoint.toString()+
                     "secondPoint:"+mSecondPoint.toString()+
                    "thirdPoint:"+ mThirdPoint.toString() +
                    "fourthPoint;" + mFourthPoint.toString()+
                            "endPoint:"+ mEndPoint.toString()
                    );
                    Log.e("WaveView的log:","contrler1:"+mControllerPoint1.toString()+
                    "controller2:"+ mControllerPoint2.toString()+
                    "controller3:" + mControllerPoint3.toString()+
                            "controller4:" + mControllerPoint4.toString()
                    );

                    mIsLog = true;

                }
                invalidate();
            }
        });
        animator.start();
    }


    public float getRadius() {
        return mCircleRadius;
    }

    public void setRadius(float radius) {
        this.mCircleRadius = radius;
    }

    public int getCurrentProgress() {
        return currentProgress;
    }

    public void setCurrentProgress(int currentProgress) {
        this.currentProgress = currentProgress;
        depth = (float) currentProgress / (float) maxProgress * (float) mWaveHeight;
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
