package com.xynxs.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baidu.sharesdk.BaiduShareException;
import com.baidu.sharesdk.BaiduSocialShare;
import com.baidu.sharesdk.ShareContent;
import com.baidu.sharesdk.ShareListener;
import com.baidu.sharesdk.Utility;
import com.tencent.stat.common.User;
import com.xynxs.main.adapter.MyTextViewAdapter;
import com.xynxs.main.component.MyViewPager;
import com.xynxs.main.dialog.LoadingDialog;
import com.xynxs.main.task.BaseTask;
import com.xynxs.main.util.Const;
import com.xynxs.main.util.ServerHelper;
import com.xynxs.main.util.StringUtil;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 登录界面
 */
public class LoginActivity extends BaseActivity {

	/**
	 * 登录类型(新浪，QQ，人人)
	 */
	private String login_type;
	private String msg;

	private MyViewPager viewpager = null;
	private View loginView;
	private View waitingView;
	private View provView;
	private View collegeView;
	private View yearView;
	private View intrView;

	private Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_login);
		handler = new Handler();
		viewpager = (MyViewPager) findViewById(R.id.login_tabpager);
		List<View> list = new ArrayList<View>();

		// 登录界面
		loginView = createView(R.layout.login_01);

		// 登录等待界面
		waitingView = createView(R.layout.login_02);

		// 省份选择界面
		provView = createView(R.layout.login_03);

		// 大学选择界面
		collegeView = createView(R.layout.login_04);

		// 入学年份选择界面
		yearView = createView(R.layout.login_05);

		// 兴趣界面
		intrView = createView(R.layout.login_06);

		list.add(loginView);
		list.add(waitingView);
		list.add(provView);
		list.add(collegeView);
		list.add(yearView);
		list.add(intrView);

		viewpager.setAdapter(new LoginViewPagerAdapter(list));
		viewpager.setOnPageChangeListener(new LoginViewPagerPageChangeListener());

		viewpager.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});

		loginView.findViewById(R.id.sina_login_btn).setOnClickListener(new LoginBtnClickListener(Utility.SHARE_TYPE_SINA_WEIBO));
		loginView.findViewById(R.id.qzone_login_btn).setOnClickListener(new LoginBtnClickListener(Utility.SHARE_TYPE_QZONE));
		loginView.findViewById(R.id.renren_login_btn).setOnClickListener(new LoginBtnClickListener(Utility.SHARE_TYPE_RENREN));

		super.onCreate(savedInstanceState);
	}

	private class LoginBtnClickListener implements OnClickListener {
		private String t;

		public LoginBtnClickListener(String type) {
			t = type;
		}

		@Override
		public void onClick(View v) {
			LoginActivity.this.login_type = t;
			showWaitingView();
		}
	}

	/**
	 * 显示等待界面（用于判断用户是否是第一次登录）
	 */
	private void showWaitingView() {
		viewpager.setCurrentItem(1);

		// 点击返回按钮。返回登录界面
		waitingView.findViewById(R.id.title_bar_left_btn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				viewpager.setCurrentItem(0);
			}
		});
		final BaiduSocialShare share = getSocailShare();

		// 当前share不为空，则要进行清空，（保证能看到登录界面，必须是未登录状态）
		if (share != null) {
			share.cleanAllAccessToken();
			share.cleanAccessToken(login_type);
		}
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if (share != null) {
					if (share.isAccessTokenValid(login_type)) {
						share.getUserInfoWithShareType(getApplicationContext(), login_type, new UserInfoListener());
					} else {
						share.authorize(LoginActivity.this, login_type, new UserInfoListener());
					}
				} else {
					toast("无法初始化分享组件");
				}
			}
		}, 150);
	}

	private void showProvView() {
		final EditText provEt = (EditText) provView.findViewById(R.id.prov_search_et);
		ListView plistView = (ListView) provView.findViewById(R.id.prov_listview);
		provView.findViewById(R.id.title_bar_left_btn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				viewpager.setCurrentItem(0);
			}
		});
		final MyTextViewAdapter adapter = new MyTextViewAdapter(this, R.layout.textview, initProvList());
		plistView.setAdapter(adapter);
		viewpager.setCurrentItem(2);

		provEt.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				String content = provEt.getText().toString();
				adapter.filter(content);
			}
		});
		plistView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String select_prov = view.getTag().toString();
				showCollegeView(select_prov);
			}
		});
	}

	private void showCollegeView(final String prov) {
		final EditText colEt = (EditText) collegeView.findViewById(R.id.college_search_et);
		collegeView.findViewById(R.id.title_bar_left_btn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				viewpager.setCurrentItem(2);
			}
		});
		ListView listView = (ListView) collegeView.findViewById(R.id.college_listview);
		final MyTextViewAdapter adapter = new MyTextViewAdapter(this, R.layout.textview, initCollegeList(prov));
		listView.setAdapter(adapter);
		viewpager.setCurrentItem(3);

		colEt.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				String content = colEt.getText().toString();
				adapter.filter(content);
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String select_college = view.getTag().toString();
				showYearView(select_college, prov);
			}
		});
	}

	private void showYearView(final String college, final String prov) {
		ListView listView = (ListView) yearView.findViewById(R.id.year_listview);
		yearView.findViewById(R.id.title_bar_left_btn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				viewpager.setCurrentItem(3);
			}
		});
		List<String> yearList = new ArrayList<String>();
		int newestYear = StringUtil.getCurrentYear() + 3;
		for (int i = newestYear; i > (newestYear - 20); i--) {
			yearList.add(i + "");
		}

		final MyTextViewAdapter adapter = new MyTextViewAdapter(this, R.layout.textview, yearList);
		listView.setAdapter(adapter);
		viewpager.setCurrentItem(4);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String y = view.getTag().toString();
				showIntrView(college, y, prov);
			}
		});
	}

	private void showIntrView(final String college, final String year, final String prov) {
		intrView.findViewById(R.id.title_bar_left_btn).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (registerTask != null) {
					registerTask.stopTask();
				}
				viewpager.setCurrentItem(4);
			}
		});
		viewpager.setCurrentItem(5);
		OnClickListener finisher = new OnClickListener() {

			@Override
			public void onClick(View v) {
				String intr = getInterest();
				register(college, prov, year, intr, msg);

			}
		};
		intrView.findViewById(R.id.title_bar_right_btn).setOnClickListener(finisher);
		intrView.findViewById(R.id.interest_finish).setOnClickListener(finisher);
	}

	private RegisterTask registerTask;

	private void register(String college, String prov, String year, String intr, String msg) {
		intrView.findViewById(R.id.title_bar_right_btn).setClickable(false);
		intrView.findViewById(R.id.interest_finish).setClickable(false);
		if (registerTask != null) {
			registerTask.stopTask();
		}
		registerTask = new RegisterTask(this, college, prov, msg, year, intr);
		loadingDialog = new LoadingDialog(this, registerTask, "正在登录");
		loadingDialog.show();
	}

	private class RegisterTask extends BaseTask {

		private String college;
		private String year;
		private String city;
		private String msg;
		private String label;

		private String result;

		@Override
		public void cancelTask() {
			getActivity().toast("取消登录");
			super.cancelTask();
		}

		public RegisterTask(BaseActivity activity, String college, String city, String msg, String year, String label) {
			super(activity);
			this.college = college;
			this.year = year;
			this.city = city;
			this.msg = msg;
			this.label = label;
		}

		@Override
		protected void doInBackground() {
			result = (String) getHelper().register(college, city, msg, Integer.parseInt(year), label);
		}

		@Override
		protected void onPostExecute() {
			loadingDialog.dismiss();
			if (result == null) {
				// 网络不给力
				toastNetworkBusy();
				intrView.findViewById(R.id.title_bar_right_btn).setClickable(true);
				intrView.findViewById(R.id.interest_finish).setClickable(true);
			} else if (result.equals("")) {
				toastNetworkBusy();
				intrView.findViewById(R.id.title_bar_right_btn).setClickable(true);
				intrView.findViewById(R.id.interest_finish).setClickable(true);
			} else {
				if (result.startsWith("FAIL")) {
					toastNetworkBusy();
					intrView.findViewById(R.id.title_bar_right_btn).setClickable(true);
					intrView.findViewById(R.id.interest_finish).setClickable(true);
				} else {
					// 注册成功,保存信息,跳到主页
					saveUser(result);
					// 分享注册信息到其它平台
					if (((CheckBox) intrView.findViewById(R.id.interest_cb)).isChecked()) {
						new ShareRegisterTask(LoginActivity.this, login_type);
					}
					Intent intent = new Intent(getActivity(), MainActivity.class);
					getActivity().finish();
					getActivity().startActivity(intent);
				}
			}

		}

	}

	/**
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @author Administrator
	 * 
	 */
	private class LoginViewPagerAdapter extends PagerAdapter {

		private List<View> list = null;

		public LoginViewPagerAdapter(List<View> list) {
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

	private class LoginViewPagerPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int state) {
		}

		@Override
		public void onPageScrolled(int page, float positionOffset, int positionOffsetPixels) {
		}

		@Override
		public void onPageSelected(int page) {
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			int i = viewpager.getCurrentItem();
			if (i > 0) {
				if (i == 2) {
					viewpager.setCurrentItem(0);
				} else {
					viewpager.setCurrentItem(i - 1);
					if (registerTask != null) {
						registerTask.stopTask();
					}
				}
			} else {
				toQuitTheApp();
			}
		}
		return false;
	}

	private String getInterest() {
		EditText intr1 = (EditText) intrView.findViewById(R.id.interest_1);
		EditText intr2 = (EditText) intrView.findViewById(R.id.interest_2);
		EditText intr3 = (EditText) intrView.findViewById(R.id.interest_3);
		EditText intr4 = (EditText) intrView.findViewById(R.id.interest_4);
		EditText intr5 = (EditText) intrView.findViewById(R.id.interest_5);
		String i1 = intr1.getText().toString().trim();
		String i2 = intr2.getText().toString().trim();
		String i3 = intr3.getText().toString().trim();
		String i4 = intr4.getText().toString().trim();
		String i5 = intr5.getText().toString().trim();

		String interest = "";

		if (!interest.contains(i1) && !i1.equals("")) {
			if (interest.equals("")) {
				interest = i1;
			} else {
				interest = interest + "[/-/]" + i1;
			}
		}
		if (!interest.contains(i2) && !i2.equals("")) {
			if (interest.equals("")) {
				interest = i2;
			} else {
				interest = interest + "[/-/]" + i2;
			}
		}

		if (!interest.contains(i3) && !i3.equals("")) {
			if (interest.equals("")) {
				interest = i3;
			} else {
				interest = interest + "[/-/]" + i3;
			}
		}

		if (!interest.contains(i4) && !i4.equals("")) {
			if (interest.equals("")) {
				interest = i4;
			} else {
				interest = interest + "[/-/]" + i4;
			}
		}

		if (!interest.contains(i5) && !i5.equals("")) {
			if (interest.equals("")) {
				interest = i5;
			} else {
				interest = interest + "[/-/]" + i5;
			}
		}

		return interest;
	}


	private GetUserByMsgTask msgTask;

	private LoadingDialog loadingDialog;

	private class UserInfoListener implements ShareListener {

		final Handler handler = new Handler(Looper.getMainLooper());

		@Override
		public void onAuthComplete(Bundle values) {
			getSocailShare().getUserInfoWithShareType(LoginActivity.this, login_type, new UserInfoListener());
		}

		/**
		 * Auth成功后返回的结果
		 */
		@Override
		public void onApiComplete(String responses) {

			final String responseStr = responses;
			handler.post(new Runnable() {
				@Override
				public void run() {
					msg = StringUtil.decodeUnicode(responseStr);
					if (msgTask != null) {
						msgTask.stopTask();
					}
					// 获取用户信息
					msgTask = new GetUserByMsgTask(LoginActivity.this, msg);
					// 得到消息后，要判断用户是否已经注册了
					loadingDialog = new LoadingDialog(LoginActivity.this, msgTask, "正在登录");
					loadingDialog.show();
				}
			});
		}

		@Override
		public void onError(BaiduShareException e) {
			final String error = e.toString();
			handler.post(new Runnable() {
				@Override
				public void run() {
					toast(error);
					viewpager.setCurrentItem(0);
				}
			});
		}

	}

	private class GetUserByMsgTask extends BaseTask {
		private String msg;
		private Object result = null;

		public GetUserByMsgTask(BaseActivity activity, String msg) {
			super(activity);
			this.msg = msg;
		}

		@Override
		protected void onCancelled() {
			getActivity().toast("取消登录");
			super.onCancelled();
		}

		@Override
		protected void doInBackground() {
			result = getHelper().getUserByMsg(msg);
		}

		@Override
		protected void onPostExecute() {
			loadingDialog.dismiss();
			if (result == null || result.equals("")) {
				getActivity().toastNetworkBusy();
			} else {
				if (result.toString().startsWith("FAIL")) {
					getActivity().toast("错误：" + result);
				} else {
					if (result.toString().startsWith("OK")) {
						// 跳转到完善资料的界面
						showProvView();
					} else {
						
						final String userStr = result.toString();
						saveUser(userStr);
						// 已有账号, 直接登录
						handler.postDelayed(new Runnable() {
							@Override
							public void run() {
								if (hasLogin()) {
									Intent intent = new Intent(LoginActivity.this, MainActivity.class);
									finish();
									startActivity(intent);
								} else {
									toast("用户错误");
									viewpager.setCurrentItem(0, false);
								}
							}
						},150);
					}
				}
			}
		}
	}

	private class ShareRegisterTask extends BaseTask {

		private String target;

		public ShareRegisterTask(BaseActivity activity, String target) {
			super(activity);
			this.target = target;
			startTask();
		}

		@Override
		protected void doInBackground() {
			String share_type = "page";
			String shareContent = "我刚刚在#校园那些事#上注册了账号，校园生活从这里开始，亲们快来看看吧，挺不错哦";
			String shareUrl = ServerHelper.getS();
			String imgUrl = shareUrl + "imgs/favicon.png";
			String title = "";
			share(share_type, shareContent, shareUrl, imgUrl, target, title);
		}

		private void share(String share_type, String shareContent, String shareUrl, String imgUrl, String target, String title) {
			// 构造分享实体
			ShareContent content = new ShareContent();
			content.setContent(shareContent);
			content.setImageUrl(imgUrl);
			content.setTitle(title);
			content.setUrl(shareUrl);
			// content.addImageByRemoteUrl("");
			getActivity().getSocailShare().share(getActivity(), target, content, new ShareContentListener());

		}

		class ShareContentListener implements ShareListener {
			final Handler handler = new Handler(Looper.getMainLooper());

			@Override
			public void onAuthComplete(Bundle values) {
			}

			@Override
			public void onApiComplete(String responses) {
				handler.post(new Runnable() {
					@Override
					public void run() {
						System.out.println("分享成功");
					}
				});
			}

			@Override
			public void onError(BaiduShareException e) {
				final String responseStr = e.toString();
				handler.post(new Runnable() {
					@Override
					public void run() {
						System.out.println("错误提示:" + responseStr);
					}
				});
			}

		}

		@Override
		protected void onPostExecute() {

		}
	}
}
