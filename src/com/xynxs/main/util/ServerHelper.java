package com.xynxs.main.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.baidu.inf.iis.bcs.BaiduBCS;
import com.baidu.inf.iis.bcs.auth.BCSCredentials;
import com.baidu.inf.iis.bcs.model.ObjectMetadata;
import com.baidu.inf.iis.bcs.request.PutObjectRequest;
import com.baidu.inf.iis.bcs.response.BaiduBCSResponse;
import com.google.gson.Gson;
import com.xynxs.main.task.BaseTask;

/**
 * 服务器连接助手
 */
public class ServerHelper {

	private static final String SERVER = "http://windvix.duapp.com/";
	private static final String RM = "POST";

	// ----------------------------------------
	private static final String BH = "bcs.duapp.com";
	private static final String BAK = "AE48a7a79b9cf4db1e04cfc45a29c53d";
	private static final String BSK = "DB7915e53120616423c7bf081d87bda8";
	private static final String bucket = "clouddisk";
	// ----------------------------------------
	private static final String object = "/first-object";
	private static final String SCODE = "ae6828eb5c545a9a2ad7be21d791f53e0b439c62dfc3e9cf7dd1b1";

	private static int versionCode = -1;

	/**
	 * 参数列表
	 */
	private Map<String, String> params = new HashMap<String, String>();

	private BaseTask task;

	public ServerHelper(BaseTask task) {
		this.task = task;
	}

	public static String getS() {
		return SERVER;
	}

	/**
	 * 增加一个参数
	 */
	private void addParam(String key, String val) {
		if (key != null && val != null) {
			if (params == null) {
				params = new HashMap<String, String>();
			}
			if (!params.containsKey(key)) {
				params.put(key, val);
			}
		}
	}

	
	private String initDataVal() {
		String result = new Gson().toJson(params, HashMap.class);
		result = DTCrypt.encode(result);
		return result;
	}

	/**
	 * 设置版本号
	 */
	public static void setVersionCode(int version) {
		versionCode = version;
	}

	public static int getServerHelperVersionCode() {
		return versionCode;
	}

	private boolean isFinish = false;
	private InputStreamReader isr = null;
	private BufferedReader br = null;
	private HttpURLConnection conn = null;
	private String result = "";

	private String p(String path) {
		String pr = "data=";
		try {
			URL url = new URL(path);

			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5 * 1000);
			conn.setRequestMethod(RM);
			conn.setDoOutput(true);
			OutputStream os = conn.getOutputStream();

			if (path.startsWith(SERVER)) {
				addParam("v", versionCode + "");
				addParam("s", SCODE);
				addParam("d", System.currentTimeMillis() + "");
				// 加密数据
				String data = initDataVal();
				pr = pr + data;
			}
			os.write(pr.getBytes());
			os.flush();

			if (conn.getResponseCode() == 200) {
				isr = new InputStreamReader(conn.getInputStream());
				br = new BufferedReader(isr);
				String temp = null;
				
				boolean isCancel = false;
				if(task!=null){
					isCancel = task.isCancelled();
				}
				
				while ((temp = br.readLine()) != null && !isFinish && !isCancel) {
					result = result + temp;
				}
				isr.close();
			}
			isFinish = true;
			conn.disconnect();
		} catch (Exception e) {
			Log.i("Netword", "Network busy");
			isFinish = true;
		}
		if (result != null) {
			result = DTCrypt.decode(result);
			result = result.trim();
		}
		return result;
	}

	public void stopConnection() {
		try {
			if (br != null) {
				br.close();
			}
			if (isr != null) {
				isr.close();
			}
			if (conn != null) {
				conn.disconnect();
			}
		} catch (Exception e) {

		}
		isFinish = true;
		result = "";
	}

	/**
	 * 获取id对应的用户
	 */
	public Object getUser(String id, boolean getOnly) {
		Object result = null;
		String path = SERVER + "a8c3848a54843c34eb70269ed1b532e3";
		addParam("id", id);
		if (getOnly) {
			addParam("ol", "true");
		} else {
			addParam("ol", "");
		}
		result = p(path);
		return result;
	}

	/**
	 * 
	 */
	public Object getUserByMsg(String msg) {
		Object result = null;
		String path = SERVER + "68eb6fb160ff6deef04ea0570704697b";
		addParam("msg", msg);
		result = p(path);
		return result;
	}

	/**
	 * 用户注册
	 */
	public Object register(String college_name, String city_name, String msg, int year, String label) {
		Object result = null;
		String path = SERVER + "689fd1a3440f8d0d74e74708e6084d2c";
		addParam("college", college_name);
		addParam("city", city_name);
		addParam("msg", msg);
		addParam("year", year + "");
		addParam("label", label);
		result = p(path);
		return result;
	}

	/**
	 * 发表
	 */
	public Object publishPost(String userid, String topic, int scope, String content, int isNoname, int hasImg) {
		Object result = null;
		String path = SERVER + "70759ae362ef51584c18cf5673f3ffa0";
		addParam("ui", userid);
		addParam("tp", topic);
		addParam("sc", scope + "");
		addParam("ct", content);
		addParam("nn", isNoname + "");
		addParam("hi", hasImg + "");
		result = p(path);

		return result;
	}

	/**
	 * post列表
	 */
	public Object listPost(int tab, String topic, int scope, String userId, int index, int count, String keyword) {
		Object result = null;
		String path = SERVER + "54422482bf9f9bfe151726fd859671aa";
		addParam("tab", "" + tab);
		addParam("tp", topic);
		addParam("sc", scope + "");
		addParam("ui", userId);
		addParam("idx", index + "");
		addParam("ct", count + "");
		addParam("kw", keyword + "");
		result = p(path);
		return result;
	}

	public Object postDetail(String postid, String userId, String addVisit, String postOwnerId) {
		Object result = null;
		String path = SERVER + "2bc2d481af654189919a4b4512ff8967";
		addParam("pi", postid);
		addParam("ui", userId);
		addParam("ad", addVisit);
		addParam("oi", postOwnerId);
		result = p(path);
		return result;
	}

	public Object addFavPost(String userId, String postId, String isAdd) {
		Object result = null;
		String path = SERVER + "17f3c89524da049611d599ed98dc11ea";
		addParam("ui", userId);
		addParam("pi", postId);
		addParam("add", isAdd);
		result = p(path);
		return result;
	}

	public Object commentPost(String userId, String quoteContent, String quoteId, String content, String postid, String isNoname) {
		Object result = null;
		String path = SERVER + "e72dbea60ea1dbbc8cc7354c6395bf23";
		addParam("ui", userId);
		addParam("qc", quoteContent);
		addParam("qi", quoteId);
		addParam("ct", content);
		addParam("pi", postid);
		addParam("nn", isNoname);
		result = p(path);
		return result;
	}

	public Object listComment(String postId, int index, int count) {
		Object result = null;
		String path = SERVER + "3540cd55ee6794701e2cdcbc7df6b0a4";
		addParam("pi", postId);
		addParam("idx", index + "");
		addParam("ct", count + "");
		result = p(path);
		return result;
	}

	public Object getUserNewMsgCount(String userId) {
		Object result = null;
		String path = SERVER + "bce959b7b59813738643d3efbfbea236";
		addParam("ui", userId);
		result = p(path);
		return result;
	}

	/**
	 * 意见反馈
	 */
	public Object addFeedback(String userId, String content, String tel) {
		Object result = null;
		String path = SERVER + "84d447e5a7f3b16a749f68c959795087";
		addParam("ui", userId);
		addParam("ct", content);
		addParam("tel", tel);
		result = p(path);
		return result;
	}

	public Object updateUserLabel(String label, String id) {
		Object result = null;
		String path = SERVER + "06666848a73bee6f28376236b28fd922";
		addParam("label", label);
		addParam("ui", id);
		result = p(path);
		return result;
	}

	public Object updateUserInfo(String name, String college, int gender, String city, String head, int entrYear, String label, String sign, String pwd, String birth, String QQ, String email, String tel, String addr, String dept, String realname, String id) {
		Object result = null;
		String path = SERVER + "de54b56dfa9b7f718528e0f595b64949";

		if (name == null) {
			name = "";
		}
		if (college == null) {
			college = "";
		}
		if (city == null) {
			city = "";
		}
		if (head == null) {
			head = "";
		}
		if (label == null) {
			label = "";
		}
		if (sign == null) {
			sign = "";
		}
		if (email == null) {
			email = "";
		}
		if (addr == null) {
			addr = "";
		}
		if (dept == null) {
			dept = "";
		}
		if (realname == null) {
			realname = "";
		}
		if (id == null) {
			id = "";
		}
		if (tel == null) {
			tel = "";
		}
		if (QQ == null) {
			QQ = "";
		}
		if (pwd == null) {
			pwd = "";
		}
		if (birth == null) {
			birth = "";
		}
		addParam("nm", name);
		addParam("cn", college);
		addParam("gd", gender + "");
		addParam("city", city);
		addParam("head", replace(head));
		addParam("eny", entrYear + "");
		addParam("lab", replace(label));
		addParam("sign", replace(sign));
		addParam("pwd", pwd);
		addParam("bir", birth);
		addParam("qq", QQ);
		addParam("email", email);
		addParam("tel", tel);
		addParam("addr", addr);
		addParam("dept", dept);
		addParam("realn", realname);
		addParam("id", id);
		result = p(path);
		return result;
	}

	/**
	 * 获取服务器最新版本的信息
	 */
	public Object getNewestVersion() {
		Object result = "";
		String path = SERVER + "down/version.txt";
		try {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5 * 1000);
			conn.setRequestMethod("GET");
			conn.connect();
			if (conn.getResponseCode() == 200) {
				InputStreamReader isr = new InputStreamReader(conn.getInputStream());
				BufferedReader br = new BufferedReader(isr);
				String temp = null;
				while ((temp = br.readLine()) != null) {
					result = result + temp;
				}
				isr.close();
			}
		} catch (Exception e) {
			Log.i("Network", "version file not found");
		}
		return result;
	}

	public Object listMsg(String userId, int index, int count) {
		Object object = null;
		String path = SERVER + "21fc001fbbdbff342e3f7f1ac6221ddf";
		addParam("ui", userId);
		addParam("idx", index + "");
		addParam("ct", count + "");
		object = p(path);
		return object;
	}

	public Object listUserPost(String userId, String myUserId, int index, int count) {
		Object object = null;
		String path = SERVER + "031684ae9147d61843537fc86ae2ec7a";
		addParam("ui", userId);
		addParam("mi", myUserId);
		addParam("idx", index + "");
		addParam("ct", count + "");
		object = p(path);
		return object;
	}

	public Object delMyPost(String userId, String postId) {
		Object object = null;
		String path = SERVER + "ad254889e2b37817ed7f64e73d0262c1";
		addParam("mi", userId);
		addParam("pi", postId);
		object = p(path);
		return object;
	}

	public Object listUserFav(String userId, String myUserId, int index, int count) {
		Object object = null;
		String path = SERVER + "51d630691692cd00f33234bfbe9a5774";
		addParam("ui", userId);
		addParam("mi", myUserId);
		addParam("idx", index + "");
		addParam("ct", count + "");
		object = p(path);
		return object;
	}

	public Object getCount(String userId, String myUserId, String type) {
		Object object = null;
		String path = SERVER + "705487a2edbee69d52d15a08521a1704";
		addParam("ui", userId);
		addParam("mi", myUserId);
		addParam("tp", type);
		object = p(path);
		return object;
	}

	public Object getUserInfo(String userId) {
		Object object = null;
		String path = SERVER + "02d8c4f1988886f9d6be9aa3f5699124";
		addParam("ui", userId);
		object = p(path);
		return object;
	}

	public Object getUserGrade(String userId) {
		Object object = null;
		String path = SERVER + "54982c03a974ecb7e79730ad41b71bee";
		addParam("ui", userId);
		object = p(path);
		return object;
	}

	public Object getUserIntro(String userId) {
		Object object = null;
		String path = SERVER + "6e719b43e7a5a7bf07e8c145cbe0aa23";
		addParam("ui", userId);
		object = p(path);
		return object;
	}

	/**
	 * 上传图片
	 */
	public void uploadImg(File imgFile, String dir, String fileName) {
		try {
			BCSCredentials credentials = new BCSCredentials(BAK, BSK);
			BaiduBCS baiduBCS = new BaiduBCS(credentials, BH);
			baiduBCS.setDefaultEncoding("UTF-8"); // Default UTF-8
			PutObjectRequest request = new PutObjectRequest(bucket, "/" + dir + "/" + fileName, imgFile);
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentType("image/jpeg");
			request.setMetadata(metadata);
			BaiduBCSResponse<ObjectMetadata> response = baiduBCS.putObject(request);
			response.getResult();
		} catch (Exception e) {
			e.printStackTrace();
			Log.i("Network", "图片出错");
		}
	}

	public static String getUploadImgURL(String userId, String fileName) {
		String path = "http://" + BH + "/" + bucket + "/" + userId + "%2F" + fileName + "?response-content-disposition=attachment;filename*=utf8''" + fileName + "&response-cache-control=private";
		return path;
	}

	public static String getShareImgURL(String userId, String fileName) {
		String path = "http://" + BH + "/" + bucket + "/" + userId + "%2F" + fileName;
		return path;
	}

	private String replace(String original) {
		if (original != null) {
			original = original.replace(",", "[/com/]");
			original = original.replace(":", "[/cot/]");
			original = original.replace(" ", "[/bla/]");
			original = original.replace("\n", "[/lin/]");
			original = original.replace("&", "[/and/]");
		}
		return original;
	}

	/**
	 * 数据转换成对象
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object convert(Object data, Class target) {
		return new Gson().fromJson(data.toString(), target);
	}
}
