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

        TextView tvclick = findViewById(R.id.tvClick);
        tvclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(MainActivity.this,SecondActivity.class);
                intent.putExtra("percent",getRandom());
                startActivity(intent);
            }
        });

        TextView tvClick2 = findViewById(R.id.tvClick2);
        tvClick2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SecondActivity.class);
                intent.putExtra("percent",getRandom());
                startActivity(intent);
            }
        });

        TextView tvClick3 = findViewById(R.id.tvClick3);
        tvClick3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ThirdSingleWaveActivity.class);
                startActivity(intent);
            }
        });
    }

    private float getRandom(){
        Random ran =new Random(System.currentTimeMillis());
             return ran.nextInt(100);
    }
}
