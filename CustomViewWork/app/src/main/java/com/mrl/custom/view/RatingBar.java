package com.mrl.custom.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.mrl.custom.R;

public class RatingBar extends View {
    private Bitmap mStarNormalBitmap, mStarFocusBitmap;
    private int mGradeNumber = 5;
    private int mCurrentGrade = 0;

    public RatingBar(Context context) {
        this(context, null);
    }

    public RatingBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RatingBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RatingBar);
        int starNormalId = ta.getResourceId(R.styleable.RatingBar_starNormal, 0);
        if (starNormalId == 0) {
            throw new RuntimeException("请设置属性 starNormal ");
        }
        mStarNormalBitmap = BitmapFactory.decodeResource(getResources(), starNormalId);
        int starFocusId = ta.getResourceId(R.styleable.RatingBar_starFocus, 0);
        if (starFocusId == 0) {
            throw new RuntimeException("请设置属性 starFocus ");
        }
        mStarFocusBitmap = BitmapFactory.decodeResource(getResources(), starFocusId);

        mGradeNumber = ta.getInt(R.styleable.RatingBar_gradeNumber, mGradeNumber);
        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = mStarFocusBitmap.getHeight();
        int width = (mStarFocusBitmap.getWidth() + getPaddingLeft()) * mGradeNumber;
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < mGradeNumber; i++) {

            //拿到每一个star 的左上角的坐标，画bitmap
            int x = i * (mStarFocusBitmap.getWidth() + getPaddingLeft());
            if (mCurrentGrade > i) {
                canvas.drawBitmap(mStarFocusBitmap, x, 0, null);
            } else {
                canvas.drawBitmap(mStarNormalBitmap, x, 0, null);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            //case MotionEvent.ACTION_DOWN:// 按下 尽量减少onDraw()的调用
            case MotionEvent.ACTION_MOVE:
                //case MotionEvent.ACTION_UP:// 抬起 尽量减少onDraw()的调用
                //相对自身左上角的坐标
                float x = event.getX();

                //相对屏幕的左上角的坐标
                float rawX = event.getRawX();

                //获取当前的评分
                int currentGrade = (int) (x / (mStarFocusBitmap.getWidth() + getPaddingLeft()) + 1);//当前评分
                if (currentGrade < 0) {
                    currentGrade = 0;
                }
                if (currentGrade > mGradeNumber) {
                    currentGrade = mGradeNumber;
                }
                if (currentGrade == mCurrentGrade) {
                    return true;//消费当前事件。减少invalidate
                }
                // 再去刷新显示
                mCurrentGrade = currentGrade;
                invalidate();
                break;
        }
        return true;//消费当前事件
    }
}
