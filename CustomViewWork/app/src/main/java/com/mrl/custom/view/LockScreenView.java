package com.mrl.custom.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 锁屏View
 */
public class LockScreenView extends View {

    //画线的画笔
    private Paint mLinePaint;
    //正常情况的画笔
    private Paint mNormalPaint;
    //错误情况的画笔
    private Paint mErrotPaint;
    //按压情况的画笔
    private Paint mPressedPaint;
    //箭头画笔
    private Paint mArrowPaint;

    //内部小圈颜色
    private int mInnerNoramlColor;
    private int mInnerPressedColor;
    private int mInnerErrorColor;

    //外部大圈颜色
    private int mOuterNormalColor;
    private int mOuterPressedColor;
    private int mOuterErrorColor;

    //9宫格一共9个点
    private LockPatternView.Point[][] mPoints = new LockPatternView.Point[3][3];

    //外圆的半径
    private float mOuterDotRadius = 0;

    public LockScreenView(Context context) {
        this(context, null);
    }

    public LockScreenView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LockScreenView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
        initPoint();
    }

    private void initPaint() {

        //画线的画笔
        mLinePaint = new Paint();
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(mOuterDotRadius / 9);
        mLinePaint.setColor(mInnerPressedColor);
        mLinePaint.setAntiAlias(true);

        //显示错误的画笔
        mErrotPaint = new Paint();
        mErrotPaint.setAntiAlias(true);
        mErrotPaint.setStrokeWidth(mOuterDotRadius / 9);
        mErrotPaint.setColor(mOuterErrorColor);
        mErrotPaint.setStyle(Paint.Style.STROKE);

        //正常显示的画笔
        mNormalPaint = new Paint();
        mNormalPaint.setStyle(Paint.Style.STROKE);
        mNormalPaint.setAntiAlias(true);
        mNormalPaint.setStrokeWidth(mOuterDotRadius / 9);
        mNormalPaint.setColor(mOuterNormalColor);

        //按压的画笔
        mPressedPaint = new Paint();
        mPressedPaint.setAntiAlias(true);
        mPressedPaint.setStrokeWidth(mOuterDotRadius / 9);
        mPressedPaint.setColor(mOuterPressedColor);
        mPressedPaint.setStyle(Paint.Style.STROKE);

        //箭头的画笔
        mArrowPaint = new Paint();
        mArrowPaint.setStyle(Paint.Style.FILL);
        mArrowPaint.setColor(mInnerPressedColor);
        mArrowPaint.setAntiAlias(true);

    }

    private void initPoint() {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public class SelfPoint {

        public static final int STATUS_NORMAL = 1;
        public static final int STATUS_PRESSED = 2;
        public static final int STATUS_ERROR = 3;

        //当前的点的状态
        private int pointStatus;
        private int centerX;
        private int centerY;
        private int index;

        public SelfPoint(int centerX, int centerY, int index) {
            this.pointStatus = pointStatus;
        }

        public int getCenterX() {
            return centerX;
        }

        public void setCenterX(int centerX) {
            this.centerX = centerX;
        }

        public int getCenterY() {
            return centerY;
        }

        public void setCenterY(int centerY) {
            this.centerY = centerY;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public void setPointNormal() {
        }

        public void setPointPressed() {
        }

        public void setPointError() {
        }

        public boolean isStatusNormal() {
            return pointStatus == STATUS_NORMAL;
        }

        public boolean isStatusPressed() {
            return pointStatus == STATUS_PRESSED;
        }

        public boolean isStatusError() {
            return pointStatus == STATUS_ERROR;
        }


    }
}
