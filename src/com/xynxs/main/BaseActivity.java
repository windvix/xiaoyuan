package com.xynxs.main;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.util.EncodingUtils;

import com.baidu.sharesdk.BaiduSocialShare;
import com.baidu.sharesdk.SocialShareLogger;
import com.baidu.sharesdk.Utility;
import com.google.gson.Gson;
import com.xynxs.main.bean.User;
import com.xynxs.main.util.Const;
import com.xynxs.main.util.DBHelper;
import com.xynxs.main.util.SocialShareConfig;
import com.xynxs.main.util.ToastUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

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
			
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					System.exit(0);// 使虚拟机停止运行并退出程序
				}
			}, 1500);
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
		Toast.makeText(getApplicationContext(), msg,Toast.LENGTH_SHORT).show();
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
	 * 获取版本名称：e.g 1.0, 2.1.2
	 */
	protected String getVersionName() {
		String version = "";
		PackageManager pm = getPackageManager();// context为当前Activity上下文
		PackageInfo pi;
		try {
			pi = pm.getPackageInfo(getPackageName(), 0);
			version = pi.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return version;
	}

	/**
	 * 以下是关于数据的读取也保存
	 */
	private static final String DATA_ROOT_KEY = "DATA_ROOT";

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
		DBHelper db = getDbhelper();
		if(val==null){
			val="";
		}
		db.updateKeyVal(key, val);
		return true;
	}

	public String getDataString(String key) {
		DBHelper db = getDbhelper();
		return db.getVal(key);
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

	public User getUser() {
		DBHelper helper = getDbhelper();
		String val = helper.getVal(Const.LOGIN_USER_INFO);
		return (User) convert(val, User.class);
	}

	public void saveUser(String userStr) {
		DBHelper helper = getDbhelper();
		helper.updateKeyVal(Const.LOGIN_USER_INFO, userStr);
	}
	
	private static DBHelper dbHelper;
	
	private DBHelper getDbhelper(){
		if(dbHelper ==null){
			dbHelper = new DBHelper(this);
		}
		return dbHelper;
	}
	
	private static String appDownloadDir;

	/**
	 * 获取系统的下载目录
	 */
	public String getAppDownloadDir() {
		if (appDownloadDir == null) {
			File file = Environment.getExternalStoragePublicDirectory(Const.IMG_DOWNLOAD_FOLDER);

			// 判断目录是否存在, 存在则直接返回
			if (file.exists() && file.isDirectory()) {
				appDownloadDir = file.getAbsolutePath();
			}
			// 不存在，尝试创建目录
			else {
				try {
					file.mkdir();
					// 创建成功, 则返回
					if (file.exists() && file.isDirectory()) {
						appDownloadDir = file.getAbsolutePath();
					}
				} catch (Exception e) {

				}
			}
			// 如果appDownloadDir还是为空,说明上面的创建失败
			if (appDownloadDir == null) {
				File cache = Environment.getDownloadCacheDirectory();
				
				//以缓存目录作为下载目录
				if (cache.exists() && cache.isDirectory()) {
					File cacheFile = new File(cache.getAbsoluteFile(), Const.IMG_DOWNLOAD_FOLDER);
					if (cacheFile.exists() && cacheFile.isDirectory()) {
						appDownloadDir = cacheFile.getAbsolutePath();
					} else {
						try {
							cacheFile.mkdir();
							if (cacheFile.exists() && cacheFile.isDirectory()) {
								appDownloadDir = cacheFile.getAbsolutePath();
							}
						} catch (Exception e) {

						}
					}
				}
				//缓存目录不存在，尝试创建
				else {
					try {
						cache.mkdir();
						if (cache.exists() && cache.isDirectory()) {
							File cacheFile = new File(cache.getAbsoluteFile(), Const.IMG_DOWNLOAD_FOLDER);
							cacheFile.mkdir();
							if (cacheFile.exists() && cacheFile.isDirectory()) {
								appDownloadDir = cacheFile.getAbsolutePath();
							}
						}
					} catch (Exception e) {

					}
				}
			}
		}
		return appDownloadDir + "/";
	}

	
	/**
	 * 数据转换成对象
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object convert(String data, Class target) {
		return new Gson().fromJson(data, target);
	}
	
	/**
	 * 初始化大学列表
	 */
	protected List<String> initCollegeList(String prov) {
		String alls = getFromAssets("colleges.txt");
		@SuppressWarnings("unchecked")
		Map<String, List<String>> map = (HashMap<String, List<String>>) convert(alls, HashMap.class);
		List<String> list = new ArrayList<String>();
		if (map.containsKey(prov)) {
			list = map.get(prov);
			map.clear();
		}
		return list;
	}
	
	/**
	 * 初始化省份列表
	 */
	protected List<String> initProvList() {
		List<String> provList = new ArrayList<String>();
		provList.add("北京");
		provList.add("天津");
		provList.add("河北");
		provList.add("山西");
		provList.add("辽宁");
		provList.add("吉林");
		provList.add("黑龙江");
		provList.add("上海");
		provList.add("江苏");
		provList.add("浙江");
		provList.add("安徽");
		provList.add("福建");
		provList.add("江西");
		provList.add("山东");
		provList.add("河南");
		provList.add("湖北");
		provList.add("湖南");
		provList.add("广东");
		provList.add("内蒙古");
		provList.add("广西");
		provList.add("海南");
		provList.add("重庆");
		provList.add("四川");
		provList.add("贵州");
		provList.add("云南");
		provList.add("西藏");
		provList.add("陕西");
		provList.add("甘肃");
		provList.add("青海");
		provList.add("宁夏");
		provList.add("新疆");
		return provList;
	}
	
	/**
	 * 将绝对路径的图片,以指定的高度和宽度生成bitmap对象
	 */
	public Bitmap readBitmapAutoSize(String filePath, int outWidth, int outHeight) {
		// outWidth和outHeight是目标图片的最大宽度和高度，用作限制
		FileInputStream fs = null;
		BufferedInputStream bs = null;
		try {
			fs = new FileInputStream(filePath);
			bs = new BufferedInputStream(fs);
			BitmapFactory.Options options = setBitmapOption(filePath, outWidth, outHeight);
			return BitmapFactory.decodeStream(bs, null, options);
		} catch (Exception e) {
		} finally {
			try {
				bs.close();
				fs.close();
			} catch (Exception e) {
			}
		}
		return null;
	}

	private BitmapFactory.Options setBitmapOption(String file, int width, int height) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inJustDecodeBounds = true;
		// 设置只是解码图片的边距，此操作目的是度量图片的实际宽度和高度
		BitmapFactory.decodeFile(file, opt);

		int outWidth = opt.outWidth; // 获得图片的实际高和宽
		int outHeight = opt.outHeight;

		opt.inDither = false;
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		// 设置加载图片的颜色数为16bit，默认是RGB_8888，表示24bit颜色和透明通道，但一般用不上
		opt.inSampleSize = 1;
		// 设置缩放比,1表示原比例，2表示原来的四分之一....
		// 计算缩放比
		if (outWidth != 0 && outHeight != 0 && width != 0 && height != 0) {
			int sampleSize = (outWidth / width + outHeight / height) / 2;
			opt.inSampleSize = sampleSize;
		}

		opt.inJustDecodeBounds = false;// 最后把标志复原
		return opt;
	}

	public int getTitleBarHeight() {
		return getResources().getDimensionPixelSize(R.dimen.TITLE_BAR_HEIGHT);
	}
}
