package com.mrl.custom.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.mrl.custom.R;
import com.mrl.custom.view.RoundView;
import com.mrl.custom.view.WaveView;

public class SecondActivity extends Activity {

    private WaveView mWaveView;
    private int currentProgress = 10;
    private int maxProgress = 100;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case 0:
                    mWaveView.start();
                    mWaveView.setCurrentProgress(currentProgress);
                    currentProgress ++;
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);


        float percent = getIntent().getFloatExtra("percent",0);

        RoundView roundView = findViewById(R.id.roundView);
        roundView.setPercent(percent);


        initWaveView();

    }

    private void initWaveView(){
        mWaveView = (WaveView) findViewById(R.id.waveView);
        //设置圆的半径
        mWaveView.setRadius(100);
        //设置进度最大值
        mWaveView.setMaxProgress(maxProgress);
        //设置进度的当前值
        mWaveView.setCurrentProgress(currentProgress);
        //模拟下载。每个
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (currentProgress < maxProgress){
                    try {
                        Thread.sleep(100);
                        handler.sendEmptyMessage(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();
    }
}
