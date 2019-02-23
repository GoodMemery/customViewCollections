package com.mrl.custom.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;

import com.mrl.custom.util.CountTimerUtils;
import com.mrl.custom.view.SingleWaveView;

/**
 * 波浪线显示view
 *
 * @author 李立
 * @date 2019/2/21
 */
public class ThirdSingleWaveActivity extends Activity {

    private int mDepth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final SingleWaveView view = new SingleWaveView(this);
        setContentView(view);

        CountDownTimer mTimer = new CountDownTimer( 100000000, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                mDepth = mDepth + 1;
                view.setDepth(mDepth);

                Log.e("SingleWaveView的depth2:",mDepth + "");

            }

            @Override
            public void onFinish() {

            }
        };

        mTimer.start();

    }

}
