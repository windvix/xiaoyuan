package com.xynxs.main;

import java.io.InputStream;

import org.apache.http.util.EncodingUtils;

import com.baidu.sharesdk.BaiduSocialShare;
import com.baidu.sharesdk.SocialShareLogger;
import com.baidu.sharesdk.Utility;
import com.google.gson.Gson;
import com.xynxs.main.bean.User;
import com.xynxs.main.util.SocialShareConfig;
import com.xynxs.main.util.ToastUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;

@SuppressLint("CommitPrefEdits")
public class BaseActivity extends Activity {

	private boolean isExit = false;

	private static BaiduSocialShare socialShare;

	// 封装ToQuitTheApp方法
	public void toQuitTheApp() {
		if (isExit) {
			// ACTION_MAIN with category CATEGORY_HOME 启动主屏幕
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			startActivity(intent);
			finish();
			System.exit(0);// 使虚拟机停止运行并退出程序

		} else {
			isExit = true;
			toast("再按一次退出");
			mHandler.sendEmptyMessageDelayed(0, 3000);// 3秒后发送消息
		}
	}

	// 从assets 文件夹中获取文件并读取数据
	protected String getFromAssets(String fileName) {
		String result = "";
		try {
			InputStream in = getResources().getAssets().open(fileName);
			// 获取文件的字节数
			int lenght = in.available();
			// 创建byte数组
			byte[] buffer = new byte[lenght];
			// 将文件中的数据读到byte数组中
			in.read(buffer);
			result = EncodingUtils.getString(buffer, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	protected String getHeadImgDir(){
		String dir = getFilesDir().getAbsolutePath()+"/head/";
		try{
			
		}catch(Exception e){
			
		}
		return dir;
	}
	
	// 创建Handler对象，用来处理消息
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {// 处理消息
			super.handleMessage(msg);
			isExit = false;
		}
	};

	/**
	 * 显示一个类toast消息
	 */
	public void toast(String msg) {
		ToastUtil.showMessage(getApplicationContext(), msg);
	}

	/**
	 * 获取屏幕高度
	 */
	@SuppressWarnings("deprecation")
	public int getScreenHeight() {
		return getWindowManager().getDefaultDisplay().getHeight();
	}

	/**
	 * 获取屏幕宽度
	 */
	@SuppressWarnings("deprecation")
	public int getScreenWidth() {
		return getWindowManager().getDefaultDisplay().getWidth();
	}

	public void toastNetworkBusy() {
		toast("网络不给力，请稍后再试");
	}

	public BaiduSocialShare getSocailShare() {
		if (socialShare == null) {
			socialShare = BaiduSocialShare.getInstance(this, SocialShareConfig.mbApiKey);
			SocialShareLogger.on();
		}
		return socialShare;
	}

	/**
	 * 获取inflater
	 */
	private LayoutInflater getInflater() {
		return (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	/**
	 * 根据资源ID，生成对应的view
	 */
	public View createView(int resourceId) {
		return getInflater().inflate(resourceId, null);
	}

	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
	}

	// 判断是否已经登录
	protected boolean hasLogin() {
		boolean hasLogin = false;
		User user = getUser();
		if (user != null) {
			String id = user.getId();
			if (id != null) {
				hasLogin = true;
			}
		}
		return hasLogin;
	}

	/**
	 * 获取版本号
	 */
	public int getVersionCode() {
		int version = 0;
		PackageManager pm = getPackageManager();
		PackageInfo pi;
		try {
			pi = pm.getPackageInfo(getPackageName(), 0);
			version = pi.versionCode;
		} catch (NameNotFoundException e) {
		}
		return version;
	}

	/**
	 * 以下是关于数据的读取也保存
	 */
	private static final String DATA_ROOT_KEY = "DATA_ROOT";
	public static final String USER_KEY = "USER_INFO_KEY";

	public void cleanAllData() {
		// 清除所有数据
		getPreference().edit().clear().commit();
		BaiduSocialShare share = getSocailShare();
		share.cleanAllAccessToken();
		share.cleanAccessToken(Utility.SHARE_TYPE_SINA_WEIBO);
		share.cleanAccessToken(Utility.SHARE_TYPE_QZONE);
		share.cleanAccessToken(Utility.SHARE_TYPE_RENREN);
		socialShare = null;
	}

	private SharedPreferences getPreference() {
		return getSharedPreferences(DATA_ROOT_KEY, 0);
	}

	public boolean saveDataString(String key, String val) {
		return getPreference().edit().putString(key, val).commit();
	}

	public String getDataString(String key) {
		return getPreference().getString(key, "");
	}

	public boolean saveDataInt(String key, int val) {
		return getPreference().edit().putInt(key, val).commit();
	}

	public int getDataInt(String key) {
		return getPreference().getInt(key, -1);
	}

	public boolean saveDataLong(String key, long val) {
		return getPreference().edit().putLong(key, val).commit();
	}

	public long getDataLong(String key) {
		return getPreference().getLong(key, -1L);
	}

	public boolean saveDataBoolean(String key, boolean val) {
		return getPreference().edit().putBoolean(key, val).commit();
	}

	public boolean getDataBoolean(String key) {
		return getPreference().getBoolean(key, false);
	}

	public User getUser() {
		String val = getDataString(USER_KEY);
		return (User) convert(val, User.class);
	}

	public void saveUser(String userStr) {
		saveDataString(USER_KEY, userStr);
	}

	/**
	 * 数据转换成对象
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object convert(Object data, Class target) {
		return new Gson().fromJson(data.toString(), target);
	}
}
