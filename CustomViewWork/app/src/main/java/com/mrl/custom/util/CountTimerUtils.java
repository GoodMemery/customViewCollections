package com.mrl.custom.util;

import android.os.CountDownTimer;
import android.view.View;

/**
 * 时间延时工具
 *
 * @author 李立
 * @date 2019/2/23
 */
public class CountTimerUtils {

    private CountDownTimer mTimer;
    private static final int COUNT_DOWN_INTERVAL = 1000;

    /**
     * 延时处理
     */
    public void countTimeViewGone(final View view, int delaySeconds) {

        mTimer = new CountDownTimer(COUNT_DOWN_INTERVAL * delaySeconds, COUNT_DOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                if (view != null) {
                    view.setVisibility(View.GONE);
                }

                if (mTimer != null) {
                    mTimer.cancel();
                }
            }
        };

        mTimer.start();
    }

    /**
     * 延时几秒处理
     */
    public void countTimerDelay(long delayMills, final CountDelayListener listener) {
        mTimer = new CountDownTimer(delayMills, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                if (listener != null) {
                    listener.countDelayCallBack();
                }

                if (mTimer != null) {
                    mTimer.cancel();
                }
            }
        };

        mTimer.start();
    }

    public interface CountDelayListener {
        void countDelayCallBack();
    }
}
