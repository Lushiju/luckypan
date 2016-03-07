package com.example.lucypan;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class MainActivity extends Activity {
    private LuckyPan mLuckyPan;
    private ImageView mImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLuckyPan = (LuckyPan) findViewById(R.id.luckypan);
        mImageView = (ImageView) findViewById(R.id.iv_start);
        mImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!mLuckyPan.isStart()) {
					mLuckyPan.luckyStart(0);
				}else {
					if (!mLuckyPan.isShouldEnd()) {
						mLuckyPan.luckyEnd();
					}
				}
			}
		});
    }

}
