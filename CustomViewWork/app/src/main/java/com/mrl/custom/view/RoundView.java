package com.mrl.custom.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.mrl.custom.R;
import com.mrl.custom.util.TDevice;

/**
 * 圆形View
 *
 * @author 李立
 * @date 2019/2/16
 */
public class RoundView extends View {

    private Context mContext;

    private int mRoundDefaultColor;
    private float mRoundRadius;
    private float mStrokeWidth;
    private float mRoundViewWidth;
    private int mArcColor;
    private String mTextString;
    private float mTextNumSize;
    private float mTextPercentSize;
    private float mCenterX;
    private float mCenterY;
    private int mEndAngle;
    private float mPercent;

    private Paint mArcPaint;
    private Paint mTextNumPaint;
    private Paint mTextPercentPaint;
    private Paint mCirclePaint;
    private RectF mRectF;

    public RoundView(Context context) {
        this(context, null);
    }

    public RoundView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initAttr(context, attrs, defStyleAttr);
    }

    private void initAttr(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundView);
        if (a != null) {

            mRoundDefaultColor = a.getColor(R.styleable.RoundView_roundViewDefaultColor, 0);
            mRoundRadius = a.getDimension(R.styleable.RoundView_roundViewRadius, 0);

            mStrokeWidth = a.getDimension(R.styleable.RoundView_roundViewStrokeWidth, 0);

            mTextString = a.getString(R.styleable.RoundView_roundViewText);
            mTextNumSize = a.getDimension(R.styleable.RoundView_roundViewTextNumSize, 30);

            mTextPercentSize = a.getDimension(R.styleable.RoundView_roundViewTextPercentSize, 15);

            mArcColor = a.getColor(R.styleable.RoundView_roundViewArcColor, 0);

            mPercent = a.getFloat(R.styleable.RoundView_roundViewPercent, 0);

            a.recycle();
        }

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setWidthHeight(widthMeasureSpec, heightMeasureSpec);
        initMeasure();
    }

    /**
     * 控制宽高，解决自定义wrap_content 还是match_parent 效果的问题
     */
    private void setWidthHeight(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredWidth = (int) (mRoundRadius + mStrokeWidth) * 2;
        int desiredHeight = (int) (mRoundRadius + mStrokeWidth) * 2;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            width = Math.min(desiredWidth, widthSize);
        } else {
            //Be whatever you want
            width = desiredWidth;
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            height = Math.min(desiredHeight, heightSize);
        } else {
            //Be whatever you want
            height = desiredHeight;
        }

        //MUST CALL THIS
        setMeasuredDimension(width, height);
    }

    /**
     * 设置百分比
     */
    public void setPercent(float percent) {
        mPercent = percent;
        mEndAngle = (int) (360 * percent/(100 * 1.0f));
        mTextString = percent  + "";
        invalidate();
    }

    private void initMeasure() {

        //圆形
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setColor(mRoundDefaultColor);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(mStrokeWidth);

        //数字
        mTextNumPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextNumPaint.setTextSize(mTextNumSize);
        mTextNumPaint.setColor(mContext.getResources().getColor(R.color.colorPrimary));
        mTextNumPaint.setTextAlign(Paint.Align.CENTER);

        //百分比
        mTextPercentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPercentPaint.setTextSize(mTextPercentSize);
        mTextPercentPaint.setColor(mContext.getResources().getColor(R.color.orange));
        mTextPercentPaint.setTextAlign(Paint.Align.CENTER);

        //矩形
        mArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArcPaint.setColor(mArcColor);
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setStrokeWidth(mStrokeWidth);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawRound(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    private void drawRound(Canvas canvas) {
        mRoundViewWidth = getWidth();

        mCenterX = mRoundViewWidth / (2 * 1.0f);
        mCenterY = mRoundRadius + mStrokeWidth;

        mRectF = new RectF(
                mCenterX - mRoundRadius,
                mStrokeWidth,
                mCenterX + mRoundRadius,
                mCenterY + mRoundRadius);

        setPercent(mPercent);

        //画圆
        canvas.drawCircle(mCenterX, mCenterY, mRoundRadius, mCirclePaint);

        //画矩形
        canvas.drawArc(mRectF, -90, mEndAngle, false, mArcPaint);


        //画数字
        canvas.drawText(
                mTextString,
                0,
                mTextString.length(),
                mCenterX,
                mCenterY + mTextNumSize / 4,
                mTextNumPaint);

        //数字的宽度
        int textWidth = getTextWidth(mTextNumPaint, mTextString);

        //画百分号
        canvas.drawText(
                "%",
                0,
                "%".length(),
                mCenterX + textWidth/2 + TDevice.dip2px(5),
                mCenterY + mTextPercentSize / 2,
                mTextPercentPaint);



    }

    /**
     * 精确计算文字宽度
     */
    public static int getTextWidth(Paint paint, String str) {
        int iRet = 0;
        if (str != null && str.length() > 0) {
            int len = str.length();
            float[] widths = new float[len];
            paint.getTextWidths(str, widths);
            for (int j = 0; j < len; j++) {
                iRet += (int) Math.ceil(widths[j]);
            }
        }
        return iRet;
    }
}
