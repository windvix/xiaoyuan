package com.xynxs.main;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.xynxs.main.bean.User;
import com.xynxs.main.component.MyViewPager;
import com.xynxs.main.dialog.MustUpdateDialog;
import com.xynxs.main.dialog.ReLoginDialog;
import com.xynxs.main.dialog.UpdateDialog;
import com.xynxs.main.util.Const;
import com.xynxs.main.util.LevelUtil;
import com.xynxs.main.util.ServerHelper;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * 主界面
 */
public class MainActivity extends BaseActivity {

	private MyViewPager viewpager = null;
	private List<View> list = null;

	private Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		handler = new Handler();
		setContentView(R.layout.activity_main);

		viewpager = (MyViewPager) findViewById(R.id.tabpager);
		list = new ArrayList<View>();

		View view01 = createView(R.layout.main_tab_01);
		View view02 = createView(R.layout.main_tab_02);
		View view03 = createView(R.layout.main_tab_03);
		View view04 = createView(R.layout.main_tab_04);


		list.add(view01);
		list.add(view02);
		list.add(view03);
		list.add(view04);

		viewpager.setAdapter(new MainViewPagerAdapter(list));
		viewpager.setOnPageChangeListener(new MainViewPagerPageChangeListener());

		findViewById(R.id.show1).setOnClickListener(new MyOnClickListener(0));
		findViewById(R.id.show2).setOnClickListener(new MyOnClickListener(1));
		findViewById(R.id.show3).setOnClickListener(new MyOnClickListener(2));
		findViewById(R.id.show4).setOnClickListener(new MyOnClickListener(3));

		findViewById(R.id.img_frd).setOnClickListener(new MyOnClickListener(0));
		findViewById(R.id.img_msg).setOnClickListener(new MyOnClickListener(1));
		findViewById(R.id.img_info).setOnClickListener(new MyOnClickListener(2));
		findViewById(R.id.img_settings).setOnClickListener(new MyOnClickListener(3));

		viewpager.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});

		// 版本检测
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				checkVersion();
			}
		}, 150);

		

		// 获取当前用户
		getServerUserInfo();
		

		new MainActivityTab01(this, view01);
		new MainActivityTab02(this, view02);
		new MainActivityTab03(this, view03);
		new MainActivityTab04(this, view04);
		
		super.onCreate(savedInstanceState);
	}

	/**
	 * 获取服务器上的用户信息
	 */
	protected void getServerUserInfo() {
		new Thread() {

			Object result = null;

			@Override
			public void run() {

				result = new ServerHelper(null).getUser(getUser().getId(), true);
				/**
				 * 如果无法获取得到用户信息，将会不断循环
				 */
				if (result != null && !result.toString().equals("")) {
					getServerUserInfoResult(result.toString());
				} else {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {

					}
					run();
				}
			}
		}.start();
	}

	/**
	 * 获取服务器用户信息的结果
	 */
	protected void getServerUserInfoResult(final String result) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				// 查询用户信息失败
				if (result.startsWith("FAIL")) {
					cleanAllData();
					new ReLoginDialog(MainActivity.this).show();
				}
				// 查询用户成功
				else {
					// 取出已保存用户上次的登录时间
					User newU = (User) convert(result, User.class);
					if (newU != null && newU.getId() != null) {
						if (newU.getScore() > getUser().getScore()) {
							showUserToast("今日登录：+" + (newU.getScore() - getUser().getScore()) + "分");

							String name1 = LevelUtil.getLevelNameByScore(getUser().getScore());
							final String name2 = LevelUtil.getLevelNameByScore(newU.getScore());

							if (!name1.equals(name2)) {
								handler.postDelayed(new Runnable() {
									@Override
									public void run() {
										showUserToast("升级为：" + name2);
									}
								}, 2500);
							}
						}
						//最新用户信息，保存
						saveUser(result);
					} else {
						cleanAllData();
						new ReLoginDialog(MainActivity.this).show();
					}
				}
			}
		});

	}

	/**
	 * 检测新版本
	 */
	protected void checkVersion() {
		new Thread() {
			@Override
			public void run() {
				Object result = null;
				result = new ServerHelper(null).getNewestVersion();
				// 说明连接成功
				if (result != null && !result.equals("")) {
					// 尝试解释
					try {
						JSONArray array = new JSONArray(result.toString());
						JSONObject obj = array.getJSONObject(0);
						int serverVer = obj.getInt("version");
						int minServerVer = obj.getInt("minVersion");
						String serverName = obj.getString("name");
						String info = obj.getString("description");
						String url = obj.getString("url");

						saveDataInt(Const.APP_SERVER_VERSION, serverVer);
						saveDataString(Const.APP_SERVER_NAME, serverName);
						saveDataString(Const.APP_DESCRIPTION, info);
						saveDataString(Const.APP_SERVER_URL, url);
						saveDataInt(Const.APP_SERVER_MIN_VERSION, minServerVer);

						checkVersionResult();
					} catch (Exception e) {
						e.printStackTrace();
						toast(e.getMessage());
					}
				}
			}
		}.start();

	}

	/**
	 * 版本检测结果
	 */
	private void checkVersionResult() {
		handler.post(new Runnable() {
			@Override
			public void run() {
				int version = getDataInt(Const.APP_SERVER_VERSION);
				String versionName = getDataString(Const.APP_SERVER_NAME);
				String versionInfo = getDataString(Const.APP_DESCRIPTION);
				String versionUrl = getDataString(Const.APP_SERVER_URL);
				int minVersion = getDataInt(Const.APP_SERVER_MIN_VERSION);

				int versionCode = version;
				int minVersionCode = minVersion;
				int curVer = getVersionCode();

				// 显示必须升级对话框
				if (curVer < minVersionCode) {
					new MustUpdateDialog(MainActivity.this, versionName, versionUrl).show();
				} else {
					// 显示升级对话框
					if (versionCode > curVer) {
						new UpdateDialog(MainActivity.this, versionName, versionUrl, versionInfo).show();
					}
				}
			}
		});
	}

	private class MainViewPagerAdapter extends PagerAdapter {

		private List<View> list = null;

		public MainViewPagerAdapter(List<View> list) {
			this.list = list;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(list.get(position));
			return list.get(position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(list.get(position));
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

	}

	private class MainViewPagerPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int state) {
		}

		@Override
		public void onPageScrolled(int page, float positionOffset, int positionOffsetPixels) {
		}

		@Override
		public void onPageSelected(int page) {
			((ImageView) findViewById(R.id.img_frd)).setImageResource(R.drawable.tab_find_frd_normal);
			((ImageView) findViewById(R.id.img_msg)).setImageResource(R.drawable.tab_msg_normal);
			((ImageView) findViewById(R.id.img_info)).setImageResource(R.drawable.tab_address_normal);
			((ImageView) findViewById(R.id.img_settings)).setImageResource(R.drawable.tab_settings_normal);
			findViewById(R.id.show1).setBackgroundResource(R.drawable.bottom_tab_default);
			findViewById(R.id.show2).setBackgroundResource(R.drawable.bottom_tab_default);
			findViewById(R.id.show3).setBackgroundResource(R.drawable.bottom_tab_default);
			findViewById(R.id.show4).setBackgroundResource(R.drawable.bottom_tab_default);
			if (page == 0) {
				((ImageView) findViewById(R.id.img_frd)).setImageResource(R.drawable.tab_find_frd_pressed);
				findViewById(R.id.show1).setBackgroundResource(R.drawable.bottom_tab_click);
			}
			if (page == 1) {
				((ImageView) findViewById(R.id.img_msg)).setImageResource(R.drawable.tab_msg_pressed);
				findViewById(R.id.show2).setBackgroundResource(R.drawable.bottom_tab_click);
			}
			if (page == 2) {
				((ImageView) findViewById(R.id.img_info)).setImageResource(R.drawable.tab_address_pressed);
				findViewById(R.id.show3).setBackgroundResource(R.drawable.bottom_tab_click);
			}
			if (page == 3) {
				((ImageView) findViewById(R.id.img_settings)).setImageResource(R.drawable.tab_settings_pressed);
				findViewById(R.id.show4).setBackgroundResource(R.drawable.bottom_tab_click);
			}
		}
	}

	private class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			viewpager.setCurrentItem(index, false);
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			toQuitTheApp();
		}
		return false;
	}

	public void setGenderText(TextView genderView, int gender) {
		if (genderView != null) {
			if (gender >= 2) {
				String text = "<font color='#ef2e71'><b>♀</b></font>";
				genderView.setText(Html.fromHtml(text));
			} else {
				String text = "<font color='#0385e0'><b>♂</b></font>";
				genderView.setText(Html.fromHtml(text));
			}
		}
	}

}
