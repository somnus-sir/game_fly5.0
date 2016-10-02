package com.example.game_fly3;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SplashActivity extends Activity implements OnClickListener{
	
	private Button startButton;
	private Button goonButton;
	private LinearLayout linearLayout;
	public Boolean dieBoolean;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_first_start);
		AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
		alphaAnimation.setDuration(1000);
		linearLayout = (LinearLayout) findViewById(R.id.bg);
		linearLayout.setAnimation(alphaAnimation);
		startButton = (Button) findViewById(R.id.startgame);
		
		startButton.getBackground().setAlpha(255);//0~255透明度ֵ
		goonButton = (Button) findViewById(R.id.goongame);
		goonButton.getBackground().setAlpha(255);//ֵ
		
		
		startButton.setOnClickListener(this);
		goonButton.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.startgame:
			My_dialog();
			break;
		case R.id.goongame:
			Intent intent_goon = new Intent(SplashActivity.this,MainActivity.class);
			startActivity(intent_goon);
			break;			
		default:
			break;
		}
		
	}
	
	
	
	public void My_dialog(){
		AlertDialog.Builder dialog = new AlertDialog.Builder(SplashActivity.this);
		dialog.setTitle("开始游戏");
		dialog.setMessage("重新开始游戏");
		dialog.setCancelable(false);
		dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
				editor.putInt("sum", 0);
				editor.putInt("boomsum", 5);
				editor.putBoolean("sumkey100", true);
				editor.putInt("HP", 1);
				editor.putBoolean("dieBoolean", true);
				editor.putInt("sumtrans", 0);
				editor.commit();	
				Intent intent_start = new Intent(SplashActivity.this,MainActivity.class);
				startActivity(intent_start);
				finish();	
			}
		});
		//
		dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				Toast.makeText(SplashActivity.this, "取消", Toast.LENGTH_SHORT).show();
			}
		});
		dialog.show();
	}	
	
	
	
	
	public boolean onKeyDown(int keyCode,KeyEvent event) {

		if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){	
		return true;
		}
		return false;
		}
}
