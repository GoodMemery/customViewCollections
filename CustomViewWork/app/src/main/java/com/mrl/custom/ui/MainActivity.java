package com.mrl.custom.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.mrl.custom.R;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tvClick = findViewById(R.id.tvClick);
        tvClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,RoundViewActivity.class);
                intent.putExtra("percent",getRandom());
                startActivity(intent);
            }
        });

        TextView tvClick3 = findViewById(R.id.tvClick3);
        tvClick3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,WaveViewActivity.class);
                startActivity(intent);
            }
        });

        TextView tvClick5 = findViewById(R.id.tvClick5);
        tvClick5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,RatingBarActivity.class);
                startActivity(intent);
            }
        });
    }

    private float getRandom(){
        Random ran =new Random(System.currentTimeMillis());
             return ran.nextInt(100);
    }
}
