package com.mrl.custom.ui;

import android.app.Activity;
import android.os.Bundle;

import com.mrl.custom.R;
import com.mrl.custom.view.RoundView;

/**
 * 圆环百分比
 *
 * @author 李立
 * @date 2019/2/23
 */
public class RoundViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        initRoundView();
    }

    private void initRoundView() {
        float percent = getIntent().getFloatExtra("percent", 0);

        RoundView roundView = findViewById(R.id.roundView);
        roundView.setPercent(percent);
    }
}
