package com.mrl.custom.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.mrl.custom.R;
import com.mrl.custom.util.LogUtils;

/**
 * 仿qq 消息拖拽红点的效果
 *
 * 具体原理：https://blog.csdn.net/chenupt/article/details/41478303
 *
 * 实现该自定义view 的思路：
 * 1.需要画两个圆，画path ,所以这些操作应该在onDraw方法中操作。
 * 2.要随着手势的滑动来改变半径的大小以及要改变tipImage的显示与隐藏，所以要在onTouchEvent中处理
 * 3.初始化要将tipImage的坐标设置的和（startx,starty）相同，在onLayout方法中已经绘制好可以拿到view的宽高
 * 所以在onLayout中初始化tipImage的坐标。
 * 4.在手势抬起来的时候需要设置tipImage的坐标，在onDraw方法中绘制完之后要刷新tipImage的坐标。
 *
 * @author 李立
 * @date 2019/2/23
 */
public class QQDragRedView extends FrameLayout {

    //画笔
    private Paint mPaint;

    //路径
    private Path mPath;

    //默认半径大小
    private float DEFAULT_RADIUS = 30;

    //两个圆的半径设置位相同，因为效果不会有太大区别
    private float mRadius = DEFAULT_RADIUS;

    //红点圆心坐标
    private float mStartX = 100;
    private float mStartY = 100;

    //手touch 的点
    private float mTouchX = 300;
    private float mTouchY = 300;

    //控制点坐标（控制点坐标的确定：
    // mControllerX = (mStartX + mTouchX )/2,
    // mControllerY = ( mStartY + mTouchY)/2
    private float mControllerX = 200;
    private float mControllerY = 200;

    //是否点到tipImage了。
    private boolean mIsTouchTipView;

    private ImageView mTipImage;

    private boolean mIsAnimStart;


    public QQDragRedView(Context context) {
        this(context, null);
    }

    public QQDragRedView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QQDragRedView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

        //继承自Layout 的自定义view 会导致onDraw 方法不执行，
        //so 添加以下方法
        setWillNotDraw(false);
    }

    private void init() {
        //初始画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(2);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        mPath = new Path();

        //添加提示View ,可以用作判断触摸的点是否在tipview上。
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        mTipImage = new ImageView(getContext());
        mTipImage.setLayoutParams(params);
        mTipImage.setImageResource(R.mipmap.icon_find_pre);

        addView(mTipImage);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //初始化给tipImage设置坐标
        mTipImage.setX(mStartX - mTipImage.getWidth() / 2);
        mTipImage.setY(mStartY - mTipImage.getHeight() / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mIsAnimStart || !mIsTouchTipView) {
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.OVERLAY);
        } else {

            //不断改变红点的半径，半径小于3时，隐藏
            float distance = (float) Math.sqrt(Math.pow(mTouchY - mStartY, 2) + Math.pow(mTouchX - mStartX, 2));
            mRadius = -distance / 15 + DEFAULT_RADIUS;

            if (mRadius < 3) {
                mIsAnimStart = true;
                mTipImage.setVisibility(View.GONE);
            }

            //画两个圆
            canvas.drawCircle(mStartX, mStartY, mRadius, mPaint);
            canvas.drawCircle(mTouchX, mTouchY, mRadius, mPaint);

            //根据三角函数关系，分别近似的算出四个切点的坐标（因为将两个圆的半径设置位相同）
            //以下坐标有正有负，所以当两个圆画在不同的象限时要注意四个坐标的加减问题。
            float offsetX = (float) (mRadius * Math.sin(Math.atan((mTouchY - mStartY) / (mTouchX - mStartX))));
            float offsetY = (float) (mRadius * Math.cos(Math.atan((mTouchY - mStartY) / (mTouchX - mStartX))));

            LogUtils.e("QqDragRedView的onDraw:" + "3" + "offsetX: " + offsetX + "==offsetY:" + offsetY);

            //分别是以 红点圆心左上方的坐标逆时针开始的四个点
            float x1 = mStartX - offsetX;
            float y1 = mStartY + offsetY;

            float x2 = mTouchX - offsetX;
            float y2 = mTouchY + offsetY;

            float x3 = mTouchX + offsetX;
            float y3 = mTouchY - offsetY;

            float x4 = mStartX + offsetX;
            float y4 = mStartY - offsetY;


            //连接四个点，形成区域
            mPath.reset();
            mPath.moveTo(x1, y1);
            mPath.quadTo(mControllerX, mControllerY, x2, y2);
            mPath.lineTo(x3, y3);
            mPath.quadTo(mControllerX, mControllerY, x4, y4);
            mPath.lineTo(x1, y1);

            //更改图标的位置
            //tipImageView 的左上角的坐标
            mTipImage.setX(mTouchX - mTipImage.getWidth() / 2);
            mTipImage.setY(mTouchY - mTipImage.getHeight() / 2);

            canvas.drawPath(mPath, mPaint);
        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //按下操作时判断是否按在tipview上 mIsTouchTipView,根据mIsTouchTipView 的值在决定是否在onDraw
        //方法中画图。
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Rect rect = new Rect();
            int[] location = new int[2];

            mTipImage.getDrawingRect(rect);
            mTipImage.getLocationOnScreen(location);

            rect.left = location[0];
            rect.top = location[1];
            rect.right = rect.right + location[0];
            rect.bottom = rect.bottom + location[1];

            if (rect.contains((int) event.getRawX(), (int) event.getRawY())) {
                mIsTouchTipView = true;
            }

        }
        //当用户抬起手的时候，tipImage 回归原位，mIsTouchTipView = false;
        else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
            mIsTouchTipView = false;

            mTipImage.setX(mStartX);
            mTipImage.setY(mStartY);

        }

        invalidate();

        if (mIsAnimStart) {
            return super.onTouchEvent(event);
        }

        //得到控制点的坐标
        mControllerX = (event.getX() + mStartX) / 2;
        mControllerY = (event.getY() + mStartY) / 2;

        //手所触摸的点
        mTouchX = event.getX();
        mTouchY = event.getY();
        return true;
    }

}
