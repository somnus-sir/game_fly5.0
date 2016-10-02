package com.example.game_fly3;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

public class ActivityLoading extends Activity {
	
	private ImageView ivImageView;
	private ImageView bgImageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change);
		
		//bgImageView = (ImageView) findViewById(R.id.changebg);
	//	bgImageView.setBackgroundResource(R.drawable.bg);
		//bgImageView.setAlpha(0.6f);
		
		
		ivImageView = (ImageView) findViewById(R.id.changeiv);
		final ObjectAnimator oa1 = ObjectAnimator.ofFloat(ivImageView, "rotation", 0.0f,100000f);
		oa1.setDuration(5000);
		oa1.setRepeatMode(ObjectAnimator.RESTART);
		oa1.setRepeatCount(ObjectAnimator.INFINITE);
		oa1.start();
		new Handler().postDelayed(new Runnable() {			
			@Override
			public void run() {
				Intent intent = new Intent(ActivityLoading.this,MainActivity.class);
				startActivity(intent);		
				overridePendingTransition(R.anim.change, R.anim.change);
			}
		}, 5000);
	}
}
