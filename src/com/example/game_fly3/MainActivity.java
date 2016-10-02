package com.example.game_fly3;

import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint({ "HandlerLeak", "UseSparseArrays" })
public class MainActivity extends Activity {
	public boolean bossMovekey = true;
	public boolean bossIsLive = true;
	public Boolean sumkey100;// 当sum到达100
	public Boolean dieBoolean;//
	public Boolean espattachkey = true;
	public Boolean lopkey = false;
	public int attach_level = 0;
	public static final int TEXT_A = 1;
	public static final int TEXT_TIME = 2;
	private static final int APP_EXIT = 0;
	private int attach_fly = 100;// 子弹 偏移距离
	private int boomsum;// 炸弹数量-----在startActivity中指定初始化
	private int HP; // 初始化要在Activity_FirstStart 和 死亡重新开始游戏都设置
	private int boss_HP = 100;
	private int attachmove = 14;// 子弹速度
	private int move = 2; // 敌机移动速度
	private int sum;// 杀敌数
	private int key = 0;// 是否touch
	private int sumtrans;
	private int myrandom1 = 1;
	private int myrandom2 = 1;
	private int myrandom3 = 1;
	private int myrandom4 = 1;
	private int myrandom5 = 1;
	private int tableWidth;
	private int tableHight;
	private float volume = (float) 0.03; // 音量
	private TextView boomsumTextView;
	private TextView hpTextView;
	private TextView sumTextView;
	private TextView sumtransTextView;
	private ImageView black_iv;
	private ImageView xingkong1;
	private ImageView xingkong2;
	private ImageView xingkong3;
	private ImageView playerImageView;
	private ImageView attach;
	private ImageView boomiv;
	private ImageView enemy1;
	private ImageView enemy2;
	private ImageView enemy3;
	private ImageView enemy4;
	private ImageView enemy5;
	private ImageView esp_attach;
	private ImageView attach_anim;
	private ImageView trans;// 水晶
	private ImageView boss;

	private ImageView lop_attach;

	private Button boom;
	private LinearLayout linearlayout;//
	private RelativeLayout bgRelativeLayout;//

	float attach_x;
	float attach_y;
	float player_x;
	float player_y;
	float enemy1_x;
	float enemy2_x;
	float enemy3_x;
	float enemy4_x;
	float enemy5_x;
	float enemy1_y;
	float enemy2_y;
	float enemy3_y;
	float enemy4_y;
	float enemy5_y;
	float lop_attach_x;
	float lop_attach_y;
	float trans_x;
	float trans_y;
	float boss_x;
	float boss_y;

	SoundPool soundPool;
	HashMap<Integer, Integer> soundMap = new HashMap<Integer, Integer>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// -----------初始化数据 -----------------
		SharedPreferences prefs = getSharedPreferences("data", MODE_PRIVATE);
		sum = prefs.getInt("sum", 0);
		HP = prefs.getInt("HP", 0);
		sumkey100 = prefs.getBoolean("sumkey100", false);
		dieBoolean = prefs.getBoolean("dieBoolean", true);
		boomsum = prefs.getInt("boomsum", 7);
		sumtrans = prefs.getInt("sumtrans", 10);

		soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);// 最多容纳10个音频，质量为5
		soundMap.put(1, soundPool.load(this, R.raw.kill1, 1));
		soundMap.put(2, soundPool.load(this, R.raw.kill2, 1));
		soundMap.put(3, soundPool.load(this, R.raw.kill2, 1));

		// -----获取窗口管理器-----
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		display.getMetrics(metrics);
		tableWidth = metrics.widthPixels;
		tableHight = metrics.heightPixels;

		bgRelativeLayout = (RelativeLayout) findViewById(R.id.bg);
		hpTextView = (TextView) findViewById(R.id.HP);
		sumTextView = (TextView) findViewById(R.id.sum);
		boomsumTextView = (TextView) findViewById(R.id.boomsum);
		sumtransTextView = (TextView) findViewById(R.id.sum_trans);
		playerImageView = (ImageView) findViewById(R.id.player);// myfly
		black_iv = (ImageView) findViewById(R.id.blackiv);
		xingkong1 = (ImageView) findViewById(R.id.xingkong1);
		xingkong2 = (ImageView) findViewById(R.id.xingkong2);
		xingkong3 = (ImageView) findViewById(R.id.xingkong3);
		esp_attach = (ImageView) findViewById(R.id.esp_attach);
		attach_anim = (ImageView) findViewById(R.id.attach_anim);
		attach = (ImageView) findViewById(R.id.attach);
		boomiv = (ImageView) findViewById(R.id.boom);
		enemy1 = (ImageView) findViewById(R.id.enemy1);
		enemy2 = (ImageView) findViewById(R.id.enemy2);
		enemy3 = (ImageView) findViewById(R.id.enemy3);
		enemy4 = (ImageView) findViewById(R.id.enemy4);
		enemy5 = (ImageView) findViewById(R.id.enemy5);
		boss = (ImageView) findViewById(R.id.boss);

		lop_attach = (ImageView) findViewById(R.id.lop_attach);
		trans = (ImageView) findViewById(R.id.trans);

		boom = (Button) findViewById(R.id.Boom);

		// ------------resource---------
		esp_attach.setBackgroundResource(R.drawable.img_esp_attach);
		esp_attach.setX(-200);
		esp_attach.setY(-200);
		attach_anim.setBackgroundResource(R.drawable.b2);
		attach_anim.setY((float) tableHight);
		attach.setBackgroundResource(R.drawable.attach11);
		lop_attach.setBackgroundResource(R.drawable.upgrade1);
		lop_attach.setY((float) tableHight);
		trans.setBackgroundResource(R.drawable.trans);
		trans.setY((float) tableHight);
		boss.setBackgroundResource(R.drawable.boss1);
		boss.setY((float) tableHight);

		enemy1.setBackgroundResource(R.drawable.enemy_all1);
		enemy2.setBackgroundResource(R.drawable.enemy_all1);
		enemy3.setBackgroundResource(R.drawable.enemy_f3);
		enemy4.setBackgroundResource(R.drawable.enemy_all2);
		enemy5.setBackgroundResource(R.drawable.enemy_all2);

		enemy1.setY((float) -800);
		enemy2.setY((float) -800);
		enemy3.setY((float) -800);
		enemy4.setY((float) -800);
		enemy5.setY((float) -800);

		playerImageView.setBackgroundResource(R.drawable.player_e);
		boomiv.setBackgroundResource(R.drawable.attach11);
		boomiv.setY(tableHight / 4 * 3);
		black_iv.setBackgroundResource(R.drawable.black2);
		black_iv.setX(tableWidth / 3 * 2);
		black_iv.setY(-400);

		xingkong1.setBackgroundResource(R.drawable.image_xk1);
		xingkong2.setBackgroundResource(R.drawable.image_xk1);
		xingkong3.setBackgroundResource(R.drawable.image_xk1);

		// -----button_Boom-----------
		boom.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (boomsum > 0) {
					// boomiv.setY(playerImageView.getY());
					// key_boom = 1;
					boomsum--;
					boomsumTextView.setText(" * " + boomsum);
					// bgRelativeLayout.setBackgroundColor(Color.BLACK);
					playerImageView.setY((float) tableHight / 3 * 2);
					playerImageView.setX((float) tableWidth / 2 - 100);
					// 动画的集合
					AnimatorSet set = new AnimatorSet();
					ObjectAnimator oa = ObjectAnimator.ofFloat(boomiv,
							"rotation", 0.0f, 720.0f, 1440.0f, 2880.0f);
					oa.setDuration(2000);
					ObjectAnimator oa2 = ObjectAnimator.ofFloat(boomiv,
							"translationY", 0.0f, -10f, -20f, -40f, -60f,
							-100f, -200f, -600f);
					oa2.setDuration(2000);
					set.playTogether(oa, oa2);
					set.start();

					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							AnimatorSet set1 = new AnimatorSet();
							ObjectAnimator oa3 = ObjectAnimator.ofFloat(boomiv,
									"rotation", 0.0f, 8000.0f);
							oa3.setDuration(2000);
							ObjectAnimator oa4 = ObjectAnimator.ofFloat(boomiv,
									"scaleY", 1.0f, 2.0f, 4.0f, 8.0f, 16.0f,
									32.0f, 64.0f, 1.0f);
							oa4.setDuration(2000);
							ObjectAnimator oa6 = ObjectAnimator.ofFloat(boomiv,
									"scaleX", 1.0f, 2.0f, 4.0f, 8.0f, 16.0f,
									32.0f, 64.0f, 1.0f);
							oa4.setDuration(2000);
							ObjectAnimator oa5 = ObjectAnimator.ofFloat(boomiv,
									"translationY", -600.0f);
							set1.playTogether(oa5, oa3, oa4, oa6);
							set1.start();
						}
					}, 1900);

					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							// bgRelativeLayout.setBackgroundColor(Color.WHITE);
							enemy1.setY((float) -500);
							enemy2.setY((float) -500);
							enemy3.setY((float) -500);
							enemy4.setY((float) -500);
							enemy5.setY((float) -500);
							boomiv.setY((float) tableHight);
							soundPool.play(soundMap.get(2), 1, 1, 0, 0, 1);
							sum += 5;
						}
					}, 3900);
				} else {
					Toast.makeText(MainActivity.this, "弹药不足",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		// ----UI-------
		task();
		boomsumTextView.setText(" * " + boomsum);

		// ------background---
		ObjectAnimator oax = ObjectAnimator.ofFloat(xingkong1, "translationY",
				-100.0f, 2000f);
		oax.setDuration(30000);
		oax.setRepeatMode(ObjectAnimator.RESTART);
		oax.setRepeatCount(ObjectAnimator.INFINITE);
		// xingkong1.setX((float) xingkong1.getX() +200);
		oax.start();

		ObjectAnimator oax2 = ObjectAnimator.ofFloat(xingkong2, "translationY",
				-2500.0f, 1800.0f);
		oax2.setDuration(80000);
		oax2.setRepeatMode(ObjectAnimator.RESTART);
		oax2.setRepeatCount(ObjectAnimator.INFINITE);
		// xingkong2.setX((float) xingkong2.getX() + 300);
		oax2.start();

		ObjectAnimator oax3 = ObjectAnimator.ofFloat(xingkong3, "translationY",
				-1500.0f, -1000.0f, -500.0f, 0.0f, 500.0f, 1000.0f, 1500.0f,
				2000.0f);
		oax3.setDuration(80000);
		oax3.setRepeatMode(ObjectAnimator.RESTART);
		oax3.setRepeatCount(ObjectAnimator.INFINITE);
		// xingkong3.setX((float) xingkong3.getX() +350);
		oax3.start();

		// ---touch--
		linearlayout = (LinearLayout) findViewById(R.id.linearlayout);
		linearlayout.setOnTouchListener(new OnTouchListener() {
			@TargetApi(Build.VERSION_CODES.HONEYCOMB)
			@SuppressLint("NewApi")
			public boolean onTouch(View v, MotionEvent event) {
				try {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						playerImageView.setX((float) event.getX() - 100);
						playerImageView.setY((float) event.getY());
						key = 1;
						attach.setY((float) playerImageView.getY());
						// change_protected();
						pd();
						break;
					case MotionEvent.ACTION_MOVE:
						playerImageView.setX((float) event.getX() - 100);
						playerImageView.setY((float) event.getY());
						pd();
						break;
					case MotionEvent.ACTION_UP:
						key = 0;
						// change_disprotected();
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return true;
			}
		});
	}

	public void task() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				update();
			}
		}, 0, 4);
	}

	Handler hand = new Handler() {
	};

	public void update() {
		hand.post(new Runnable() {
			@Override
			public void run() {
				updata_enemy();
			}
		});
	}

	// ------判断----touch_down---------
	public void pd() {
		attach_x = attach.getX();
		attach_y = attach.getY();
		player_x = playerImageView.getX();
		player_y = playerImageView.getY();
		enemy1_x = enemy1.getX();
		enemy2_x = enemy2.getX();
		enemy3_x = enemy3.getX();
		enemy4_x = enemy4.getX();
		enemy5_x = enemy5.getX();
		enemy1_y = enemy1.getY();
		enemy2_y = enemy2.getY();
		enemy3_y = enemy3.getY();
		enemy4_y = enemy4.getY();
		enemy5_y = enemy5.getY();
		lop_attach_x = lop_attach.getX();
		lop_attach_y = lop_attach.getY();
		
		boss_x = boss.getX();
		boss_y = boss.getY();
		if (enemy1_x < attach_x && attach_x < enemy1_x + 180
				&& player_y > enemy1_y && enemy1_y > -50) {
			if ((enemy1_y < attach_y + 180 && enemy1_y > attach_y)
					|| attachmove > 30) {// 无视防御
				Toast.makeText(MainActivity.this, "击落!!!", Toast.LENGTH_SHORT)
						.show();
				enemy1.setY((float) tableHight * 8 / 7 - 5);
				enemy1.setX((float) +26);
				if (attach_level < 2) {
					attach.setY((float) playerImageView.getY());
				}// 穿透
				sum += 1;
				attach_anim(enemy1_x, enemy1_y);
				soundPool.play(soundMap.get(1), volume, volume, 0, 0, 1);
			}

		}

		if (enemy2_x < attach_x && attach_x < enemy2_x + 180
				&& player_y > enemy2_y && enemy2_y > -50) {
			if ((enemy2_y < attach_y + 180 && enemy2_y > attach_y)
					|| attachmove > 30) {
				Toast.makeText(MainActivity.this, "击落!!!", Toast.LENGTH_SHORT)
						.show();
				enemy2.setY((float) tableHight * 8 / 7 - 5);
				enemy2.setX((float) +tableWidth / 2);
				if (attach_level < 2) {
					attach.setY((float) playerImageView.getY());
				}
				sum += 1;
				attach_anim(enemy2_x, enemy2_y);
				soundPool.play(soundMap.get(1), volume, volume, 0, 0, 1);
			}

		}

		if (enemy3_x < attach_x && attach_x < enemy3_x + 180
				&& player_y > enemy3_y && enemy3_y > -50) {
			if ((enemy3_y < attach_y + 180 && enemy3_y > attach_y)
					|| attachmove > 30) {
				Toast.makeText(MainActivity.this, "击落!!!", Toast.LENGTH_SHORT)
						.show();
				enemy3.setY((float) tableHight * 8 / 7 - 5);
				enemy3.setX((float) +tableWidth - 50);
				if (attach_level < 2) {
					attach.setY((float) playerImageView.getY());
				}
				sum += 1;
				attach_anim(enemy3_x, enemy3_y);
				soundPool.play(soundMap.get(1), volume, volume, 0, 0, 1);
				my_lop(enemy3_x, enemy3_y);
			}

		}

		if (enemy4_x < attach_x && attach_x < enemy4_x + 180
				&& player_y > enemy4_y && enemy4_y > -50) {
			if ((enemy4_y < attach_y + 180 && enemy4_y > attach_y)
					|| attachmove > 30) {
				Toast.makeText(MainActivity.this, "击落!!!", Toast.LENGTH_SHORT)
						.show();
				enemy4.setY((float) tableHight * 8 / 7 - 5);
				enemy4.setX((float) +tableWidth / 3);
				if (attach_level < 2) {
					attach.setY((float) playerImageView.getY());
				}
				sum += 1;
				attach_anim(enemy4_x, enemy4_y);
				soundPool.play(soundMap.get(1), volume, volume, 0, 0, 1);
			}

		}

		if (enemy5_x < attach_x && attach_x < enemy5_x + 180
				&& player_y > enemy5_y && enemy5_y > -50) {
			if ((enemy5_y < attach_y + 180 && enemy5_y > attach_y)
					|| attachmove > 30) {
				Toast.makeText(MainActivity.this, "击落!!!", Toast.LENGTH_SHORT)
						.show();
				enemy5.setY((float) tableHight * 8 / 7 - 5);
				enemy5.setX((float) +tableWidth / 4 * 3);
				if (attach_level < 2) {
					attach.setY((float) playerImageView.getY());
				}
				sum += 1;
				attach_anim(enemy5_x, enemy5_y);
				soundPool.play(soundMap.get(1), volume, volume, 0, 0, 1);
			}
		}
		
		//boss
		if(boss_x < attach_x && attach_x <boss_x+380&&player_y>boss_y &&boss_y>-50){
			if((boss_y < attach_y + 380 && boss_y>attach_y) || attachmove>30){
				attach_anim(attach_x, boss_y+100);
				soundPool.play(soundMap.get(1), volume, volume, 0, 0, 1);
				bossAttachedAniaml();
				boss_HP--;
			}
		}
		if(boss_HP<=0){
			boss.setY((float) tableHight);
			bossIsLive = false;
		}
	}

	// ------------------------UI
	// Change-------------------------------------------
	public void updata_enemy() {
		attach_x = attach.getX();
		attach_y = attach.getY();
		player_x = playerImageView.getX();
		player_y = playerImageView.getY();
		enemy1_x = enemy1.getX();
		enemy2_x = enemy2.getX();
		enemy3_x = enemy3.getX();
		enemy4_x = enemy4.getX();
		enemy5_x = enemy5.getX();
		enemy1_y = enemy1.getY();
		enemy2_y = enemy2.getY();
		enemy3_y = enemy3.getY();
		enemy4_y = enemy4.getY();
		enemy5_y = enemy5.getY();
		lop_attach_x = lop_attach.getX();
		lop_attach_y = lop_attach.getY();
		trans_x = trans.getX();
		trans_y = trans.getY();
		hpTextView.setText(HP + "");
		sumTextView.setText(sum + "");
		sumtransTextView.setText(sumtrans + "");

		// --------Die---------
		if (sum >= 100) {
			if ((Math.abs(player_x - enemy1_x) < 100)
					&& (Math.abs(player_y - enemy1_y) < 100)
					|| (Math.abs(player_x - enemy2_x) < 100)
					&& (Math.abs(player_y - enemy2_y) < 100)
					|| (Math.abs(player_x - enemy3_x) < 100)
					&& (Math.abs(player_y - enemy3_y) < 100)
					|| (Math.abs(player_x - enemy4_x) < 100)
					&& (Math.abs(player_y - enemy4_y) < 100)
					|| (Math.abs(player_x - enemy5_x) < 100)
					&& (Math.abs(player_y - enemy5_y) < 100)) {
				if (dieBoolean == true) {
					my_die();
					dieBoolean = false;
				}
			}
		}

		// ------------lop --------------
		// 吃掉子弹升级
		if (player_x < lop_attach_x && lop_attach_x < player_x + 200
				&& player_y < lop_attach_y && lop_attach_y < player_y + 200) {
			lop_attach.setY((float) tableHight);
			lopkey = false;
			attach_level++;
			attachmove += 2;
		}
		
		if (attach_level == 1) {
			attach.setBackgroundResource(R.drawable.attach1);
		}
		if (attach_level == 3) {
			attach.setBackgroundResource(R.drawable.attach4);
		}
		if (attachmove == 30) {
			attach.setBackgroundResource(R.drawable.attach6);
		}
		
		
		// 吃掉水晶
		if (player_x < trans_x && trans_x < player_x + 200
				&& player_y < trans_y && trans_y < player_y + 200) {
			trans.setY((float) tableHight);
			sumtrans++;
		}

		if (lop_attach_y > tableHight) {
			lopkey = false;
		}

		if (lopkey == true) {
			lop_attach.setY((float) lop_attach.getY() + 1);
			trans.setY((float) trans.getY() + 1);
		}


		// ---------chage enemy-------------
		if (sumkey100 == true && sum >= 100) {
			ChangeAnimal();
			sumkey100 = false;
		}
		// -----------esp_attach---------------
		if (((10 < sum && sum < 15) || (20 < sum && sum < 30))
				&& (espattachkey == true)) {
			esp_attach();
			espattachkey = false;
		}

		// ----------attach_Aniaml---------
		if (key == 1) {
			attach.setX((float) playerImageView.getX() + 80);
			attach.setY((float) attach.getY() - attachmove);
			if (attach.getY() <= 0) {
				attach.setY((float) playerImageView.getY());
			}
		}
		if (key == 0) {
			attach.setY((float) tableHight);
		}

		// -----fly rule-------
		// enemy1
		if (enemy1.getY() > tableHight * 8 / 7 - 10) {
			Random random = new Random();
			myrandom1 = Math.abs(random.nextInt()) % 3;
		}
		if (myrandom1 == 0) {
			enemy1.setY((float) enemy1.getY() + move);
		}
		if (myrandom1 == 1) {
			enemy1.setX((float) enemy1.getX() + move);
			enemy1.setY((float) enemy1.getY() + move);
		}
		if (myrandom1 == 2) {
			enemy1.setX((float) enemy1.getX() - move);
			enemy1.setY((float) enemy1.getY() + move);
		}

		// enemy2
		if (enemy2.getY() > tableHight * 8 / 7 - 10) {
			Random random = new Random();
			myrandom2 = Math.abs(random.nextInt()) % 3;
		}
		if (myrandom2 == 0) {
			enemy2.setY((float) enemy2.getY() + move);
		}
		if (myrandom2 == 1) {
			enemy2.setX((float) enemy2.getX() + move);
			enemy2.setY((float) enemy2.getY() + move);
		}
		if (myrandom2 == 2) {
			enemy2.setX((float) enemy2.getX() - move);
			enemy2.setY((float) enemy2.getY() + move);
		}

		// enemy3
		if (enemy3.getY() > tableHight * 8 / 7 - 10) {
			Random random = new Random();
			myrandom3 = Math.abs(random.nextInt()) % 3;
		}
		if (myrandom3 == 0) {
			enemy3.setX((float) enemy3.getX() + move);
			enemy3.setY((float) enemy3.getY() + move);
		}
		if (myrandom3 == 1) {
			enemy3.setX((float) enemy3.getX() - move);
			enemy3.setY((float) enemy3.getY() + move);
		}
		if (myrandom3 == 2) {
			enemy3.setY((float) enemy3.getY() + move);
		}

		// enemy4
		if (enemy4.getY() > tableHight * 8 / 7 - 10) {
			Random random = new Random();
			myrandom4 = Math.abs(random.nextInt()) % 3;
		}
		if (myrandom4 == 0) {
			enemy4.setX((float) enemy4.getX() + move);
			enemy4.setY((float) enemy4.getY() + move);
		}
		if (myrandom4 == 1) {
			enemy4.setX((float) enemy4.getX() - move);
			enemy4.setY((float) enemy4.getY() + move);
		}
		if (myrandom4 == 2) {
			enemy4.setY((float) enemy4.getY() + move);
		}

		// enemy5
		if (enemy5.getY() > tableHight * 8 / 7 - 10) {
			Random random = new Random();
			myrandom5 = Math.abs(random.nextInt()) % 3;
		}
		if (myrandom5 == 0) {
			enemy5.setX((float) enemy5.getX() + move);
			enemy5.setY((float) enemy5.getY() + move);
		}
		if (myrandom5 == 1) {
			enemy5.setX((float) enemy5.getX() - move);
			enemy5.setY((float) enemy5.getY() + move);
		}
		if (myrandom5 == 2) {
			enemy5.setY((float) enemy5.getY() + move);
		}

		// boss
		if ( bossIsLive == true && sum >=10) {
			boss.setY((float) 200);
			if (boss.getX() < 100 || bossMovekey==false) {
				bossMovekey=false;
				boss.setX((float) boss.getX() + 1);
			}
			if (boss.getX() > 540 || bossMovekey==true) {
				bossMovekey=true;
				boss.setX((float) boss.getX() - 1);
			}
		}


		// ---------限制飞行------
		if (enemy1.getX() < 0) {
			enemy1.setX((float) tableWidth);
		}
		if (enemy1.getX() > tableWidth) {
			enemy1.setX((float) 0);
		}
		if (enemy1.getY() > tableHight * 8 / 7) {
			enemy1.setY((float) -100);
		}
		if (enemy2.getX() < 0) {
			enemy2.setX((float) tableWidth);
		}
		if (enemy2.getX() > tableWidth) {
			enemy2.setX((float) 0);
		}
		if (enemy2.getY() > tableHight * 8 / 7) {
			enemy2.setY((float) -100);
		}
		if (enemy3.getX() < 0) {
			enemy3.setX((float) tableWidth);
		}
		if (enemy3.getX() > tableWidth) {
			enemy3.setX((float) 0);
		}
		if (enemy3.getY() > tableHight * 8 / 7) {
			enemy3.setY((float) -100);
		}
		if (enemy4.getX() < 0) {
			enemy4.setX((float) tableWidth);
		}
		if (enemy4.getX() > tableWidth) {
			enemy4.setX((float) 0);
		}
		if (enemy4.getY() > tableHight * 8 / 7) {
			enemy4.setY((float) -100);
		}
		if (enemy5.getX() < 0) {
			enemy5.setX((float) tableWidth);
		}
		if (enemy5.getX() > tableWidth) {
			enemy5.setX((float) 0);
		}
		if (enemy5.getY() > tableHight * 8 / 7) {
			enemy5.setY((float) -100);
		}

		if (xingkong1.getX() > tableWidth) {
			xingkong1.setX((float) 50);
		}
		if (xingkong1.getY() > tableHight) {
			xingkong1.setX((float) xingkong1.getX() + 200);
		}
		if (xingkong2.getX() > tableWidth) {
			xingkong2.setX((float) 150);
		}
		if (xingkong2.getY() > tableHight) {
			xingkong2.setX((float) xingkong2.getX() + 300);
		}
		if (xingkong3.getX() > tableWidth) {
			xingkong3.setX((float) 250);
		}
		if (xingkong3.getY() > tableHight) {
			xingkong3.setX((float) xingkong3.getX() + 220);
		}

	}

	// ----关闭保存数据-----
	@SuppressLint("CommitPrefEdits")
	public void onDestroy() {
		super.onDestroy();
		SharedPreferences.Editor editor = getSharedPreferences("data",
				MODE_PRIVATE).edit();
		editor.putInt("sum", sum);
		editor.putInt("boomsum", boomsum);
		editor.putInt("sumtrans", sumtrans);
		editor.putBoolean("sumkey100", sumkey100);
		editor.commit();
	}

	// ----返回存储数据----
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// showDialog(APP_EXIT);
			onDestroy();
			finish();
			return true;
		} else
			return super.onKeyDown(keyCode, event);
	}

	// -----kill_animal-----
	public void attach_anim(float enemy_x, float enemy_y) {
		attach_anim.setX((float) enemy_x);
		attach_anim.setY((float) enemy_y);
		AnimatorSet set = new AnimatorSet();
		ObjectAnimator oa = ObjectAnimator.ofFloat(attach_anim, "alpha", 1.0f,
				1.5f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f);
		oa.setDuration(350);
		ObjectAnimator oa2 = ObjectAnimator.ofFloat(attach_anim,
				"translationY", attach_anim.getY());
		oa2.setDuration(350);
		ObjectAnimator oa3 = ObjectAnimator.ofFloat(attach_anim, "scaleY",
				0.6f, 0.9f, 1.0f);
		oa3.setDuration(350);
		ObjectAnimator oa4 = ObjectAnimator.ofFloat(attach_anim, "scaleX",
				0.6f, 0.9f, 1.0f);
		oa4.setDuration(350);
		set.playTogether(oa, oa2, oa3, oa4);
		set.start();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				attach_anim.setBackgroundResource(R.drawable.b1);
				AnimatorSet set3 = new AnimatorSet();
				ObjectAnimator oa = ObjectAnimator.ofFloat(attach_anim,
						"alpha", 1.0f, 0.0f);
				oa.setDuration(200);
				ObjectAnimator oa2 = ObjectAnimator.ofFloat(attach_anim,
						"translationY", attach_anim.getY());
				oa2.setDuration(350);
				ObjectAnimator oa3 = ObjectAnimator.ofFloat(attach_anim,
						"scaleY", 1.0f, 1.2f);
				oa3.setDuration(200);
				ObjectAnimator oa4 = ObjectAnimator.ofFloat(attach_anim,
						"scaleX", 1.0f, 1.2f);
				oa4.setDuration(200);
				set3.playTogether(oa, oa2, oa3, oa4);
				set3.start();

				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						attach_anim.setBackgroundResource(R.drawable.b2);
					}
				}, 220);
			}
		}, 350);

	}

	// ---------背景音乐--------
	protected void onPause() {
		Intent intent = new Intent(MainActivity.this,
				com.example.game_fly3.MusicService.class);
		stopService(intent);
		super.onPause();
	}

	protected void onStart() {
		Intent intent = new Intent(MainActivity.this, MusicService.class);
		startService(intent);
		super.onStart();
	}

	public void esp_attach() {
		// ---------esp_attach----------
		Animation animation = AnimationUtils.loadAnimation(this,
				R.anim.esp_attach);
		final Animation animation2 = AnimationUtils.loadAnimation(this,
				R.anim.esp_attach2);
		final Animation animation3 = AnimationUtils.loadAnimation(this,
				R.anim.esp_attach3);
		esp_attach.startAnimation(animation);
		new Handler().postDelayed(new Runnable() {
			public void run() {
				esp_attach.startAnimation(animation2);
			}
		}, 900);
		new Handler().postDelayed(new Runnable() {
			public void run() {
				esp_attach.startAnimation(animation3);
				espattachkey = true;
			}
		}, 400);
	}
	
	public void bossAttachedAniaml(){
		AlphaAnimation aa = new AlphaAnimation(1.0f,0.5f);
		aa.setDuration(300);
		boss.startAnimation(aa);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				AlphaAnimation aa = new AlphaAnimation(0.5f,1f);
				aa.setDuration(100);
				boss.startAnimation(aa);
			}
		},300);
		
		
	}

	public void ChangeAnimal() {

		// -------changeenemy------------
		AnimatorSet changeenemy = new AnimatorSet();
		ObjectAnimator oac1 = ObjectAnimator.ofFloat(enemy1, "alpha", 1.0f,
				0.0f, 0.0f, 0.0f);
		oac1.setDuration(3000);
		ObjectAnimator oac2 = ObjectAnimator.ofFloat(enemy2, "alpha", 1.0f,
				0.0f, 0.0f, 0.0f);
		oac1.setDuration(3000);
		ObjectAnimator oac3 = ObjectAnimator.ofFloat(enemy3, "alpha", 1.0f,
				0.0f, 0.0f, 0.0f);
		oac1.setDuration(3000);
		ObjectAnimator oac4 = ObjectAnimator.ofFloat(enemy4, "alpha", 1.0f,
				0.0f, 0.0f, 0.0f);
		oac1.setDuration(3000);
		ObjectAnimator oac5 = ObjectAnimator.ofFloat(enemy5, "alpha", 1.0f,
				0.0f, 0.0f, 0.0f);
		oac1.setDuration(3000);
		changeenemy.playTogether(oac1, oac2, oac3, oac4, oac5);
		changeenemy.start();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				// enemy1.setBackgroundResource(R.drawable.boss1);
				// enemy2.setBackgroundResource(R.drawable.boss1);
				// enemy3.setBackgroundResource(R.drawable.boss1);
				// enemy4.setBackgroundResource(R.drawable.boss1);
				// enemy5.setBackgroundResource(R.drawable.boss1);
				AnimatorSet backeenemy = new AnimatorSet();
				ObjectAnimator oab1 = ObjectAnimator.ofFloat(enemy1, "alpha",
						0.0f, 1.0f);
				oab1.setDuration(2000);
				ObjectAnimator oab2 = ObjectAnimator.ofFloat(enemy2, "alpha",
						0.0f, 1.0f);
				oab2.setDuration(2000);
				ObjectAnimator oab3 = ObjectAnimator.ofFloat(enemy3, "alpha",
						0.0f, 1.0f);
				oab3.setDuration(2000);
				ObjectAnimator oab4 = ObjectAnimator.ofFloat(enemy4, "alpha",
						0.0f, 1.0f);
				oab4.setDuration(2000);
				ObjectAnimator oab5 = ObjectAnimator.ofFloat(enemy5, "alpha",
						0.0f, 1.0f);
				oab5.setDuration(2000);

				ObjectAnimator oab6 = ObjectAnimator.ofFloat(black_iv,
						"translationY", 0.0f, 2000.0f);
				oab6.setDuration(40000);
				ObjectAnimator oab7 = ObjectAnimator.ofFloat(black_iv,
						"rotation", -300.0f, 1800.0f);
				oab7.setDuration(40000);
				ObjectAnimator oab8 = ObjectAnimator.ofFloat(black_iv, "alpha",
						1.0f);
				oab8.setDuration(40000);

				backeenemy.playTogether(oab1, oab2, oab3, oab4, oab5, oab6,
						oab7, oab8);
				backeenemy.start();

			}
		}, 2500);
	}

	public void My_dialog() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
		dialog.setTitle("你已死亡");
		dialog.setMessage("是否重新开始游戏");
		dialog.setCancelable(false);
		dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				sum = 0;
				boomsum = 5;
				sumkey100 = true;
				dieBoolean = true;
				sumtrans = 0;
				Intent intent = new Intent(MainActivity.this,
						MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
		//
		dialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,
						SplashActivity.class);
				startActivity(intent);
				finish();
			}
		});
		dialog.show();
	}

	// ---------死亡动画----------------
	public void my_die() {
		Toast.makeText(MainActivity.this, "dead", Toast.LENGTH_SHORT).show();
		My_dialog();
		soundPool.play(soundMap.get(2), 1, 1, 0, 0, 1);
		attach_anim(playerImageView.getX(), playerImageView.getY());
		playerImageView.setY((float) tableHight);

	}

	public void my_lop(float enemy_x, float enemy_y) {
		if (lopkey == false) {
			lop_attach.setX((float) enemy_x);
			lop_attach.setY((float) enemy_y);
			trans.setX((float) enemy_x + 100);
			trans.setY((float) enemy_y + 50);
			lopkey = true;
		}
	}

}
