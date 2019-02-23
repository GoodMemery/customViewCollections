package com.mrl.custom.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.mrl.custom.R;
import com.mrl.custom.view.SingleWaveView;

/**
 * 波浪线显示view
 *
 * @author 李立
 * @date 2019/2/21
 */
public class WaveViewActivity extends Activity {

    private CountDownTimer mTimer;

    //水位高度
    private int mDepth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_waveview);

        final SingleWaveView waveView = findViewById(R.id.waveView);

        mTimer = new CountDownTimer( 100000000, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                //更新水位高度
                mDepth = mDepth + 4;
                waveView.setDepth(mDepth);
            }

            @Override
            public void onFinish() {
                mTimer.cancel();
            }
        };

        mTimer.start();

    }

}
