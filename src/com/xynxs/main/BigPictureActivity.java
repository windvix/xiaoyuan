package com.xynxs.main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.FloatMath;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xynxs.main.component.ImageViewTouchListener;
import com.xynxs.main.component.LoadingCircleView;
import com.xynxs.main.component.ZoomImageView;
import com.xynxs.main.task.BaseTask;
import com.xynxs.main.util.Const;

@SuppressLint({ "HandlerLeak", "FloatMath" })
public class BigPictureActivity extends BaseActivity implements OnClickListener{

	LoadingCircleView circle;

	private String url;
	private String filePath;

	private LoadImgTask task;

	private Handler handler;

	private ImageView bitImgView;

	private HttpURLConnection conn = null;

	private InputStream inStream = null;

	private FileOutputStream outStream = null;

	private ImageViewTouchListener touch;

	private ImageView minImg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		setContentView(R.layout.activity_bigpicture);

		url = getIntent().getStringExtra(Const.BIG_PIC_URL);
		filePath = getAppDownloadDir()+getIntent().getStringExtra(Const.BIG_PIC_FILE_NAME);		
		circle = (LoadingCircleView) findViewById(R.id.big_pic_loading);
		circle.setOnClickListener(this);
		bitImgView = (ImageView) findViewById(R.id.big_pic_img);

		minImg = (ImageView)findViewById(R.id.big_pic_noimg);
		
		super.onCreate(savedInstanceState);

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				int m = msg.what;
				if (m == 1) {
					if (!isStop) {
						circle.setVisibility(View.VISIBLE);
					}
				} else if (m == 50) {
					if (!isStop) {
						circle.setProgress(currentPer);
					}
				} else if (m == 100) {
					circle.setProgress(100);
				} else if (m == -1) {
					if (task != null) {
						task.stopTask();
						isStop = true;
					}
				}
			}
		};
		
		String minFileName = getIntent().getStringExtra(Const.MIN_PIC_FILE_NAME);
		Bitmap minBitmap = readBitmapAutoSize(getAppDownloadDir()+minFileName, minImg.getWidth(), minImg.getHeight());
		
		if(minBitmap!=null){
			minImg.setImageBitmap(minBitmap);
		}
		
		minImg.setVisibility(View.VISIBLE);
		bitImgView.setVisibility(View.INVISIBLE);
		
		task = new LoadImgTask(this, url, filePath);
	}

	private int currentPer = 0;

	@Override
	public void onClick(View v) {
		back();
	}

	private long downloadSize = 0;
	private long fileSize = 0;

	private boolean isStop = false;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			back();
		}
		return false;
	}

	public void back() {
		try {
			if (outStream != null) {
				outStream.close();
			}

			if(inStream!=null){
				inStream.close();
			}
			if (conn != null) {
				conn.disconnect();
			}
		} catch (Exception e) {

		}
		if(task!=null){
			task.stopTask();
		}
		finish();
	}

	private void showImg(Bitmap bitmap) {
		circle.setClickable(false);
		circle.setVisibility(View.GONE);
		if (bitmap != null) {
			if(minImg!=null){
				minImg.setVisibility(View.GONE);
			}
			if (bitImgView != null) {
				bitImgView.setVisibility(View.VISIBLE);
				touch = new ImageViewTouchListener(this) {
					
					@Override
					public void touchUp() {
						
					}
				};
				touch.initMatrix(bitImgView, bitmap);
			}
		} else {
			toast("大图加载失败");
			if (minImg != null) {
				minImg.setVisibility(View.VISIBLE);
				minImg.setOnClickListener(this);
			}
		}
	}

	private class LoadImgTask extends BaseTask {
		private String url;
		private String filePath;
		private Bitmap bitmap;

		private  int SIZE = 1024;
		
		public LoadImgTask(BaseActivity activity, String url, String filePath) {
			super(activity);
			this.url = url;
			this.filePath = filePath;
			SIZE = (int)(activity.getScreenHeight()*1.5);
			startTask();
		}

		@Override
		protected void doInBackground() {
			if (url != null && !isCancelled()) {
				File file = new File(filePath);
				if (file.exists()) {
					bitmap = getActivity().readBitmapAutoSize(filePath, SIZE, SIZE);
					if (bitmap == null) {
						getImage(url, filePath);
					} else {
						sendMessage(100);
					}
				} else {
					getImage(url, filePath);
				}
				if (bitmap == null) {
					bitmap = getActivity().readBitmapAutoSize(filePath, SIZE, SIZE);
				}
			}
			sendMessage(100);
		}

		@Override
		protected void onPostExecute() {
			showImg(bitmap);
		}

		private void readStream(InputStream inStream, String filePath) {
			if (!isCancelled()) {
				try {
					outStream = new FileOutputStream(filePath);
					byte[] buffer = new byte[1024];
					int len = -1;
					sendMessage(1);
					while ((len = inStream.read(buffer)) != -1) {
						if (!isCancelled()) {
							outStream.write(buffer, 0, len);
							downloadSize = downloadSize + len;
							long per = (long) ((1.0 * downloadSize / fileSize) * 100);
							if (per > currentPer) {
								currentPer = (int) per;
								sendMessage(50);
							}
						} else {
							len = -1;
							sendMessage(-1);
						}
					}
					outStream.close();
					inStream.close();
					sendMessage(100);
				} catch (Exception e) {

				}
			}
		}

		private void getImage(String urlStr, String filePath) {
			if (!isCancelled()) {
				try {
					URL url = new URL(urlStr);
					conn = (HttpURLConnection) url.openConnection();
					conn.setConnectTimeout(5 * 1000);
					conn.setRequestMethod("GET");
					inStream = conn.getInputStream();
					fileSize = conn.getContentLength();
					File file = new File(filePath);
					if(file.exists()){
						file.delete();
					}
					file.createNewFile();
					System.out.println("文件总大小：" + fileSize);
					if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
						readStream(inStream, filePath);
					}
				} catch (Exception e) {

				}
			}
		}

	}

	/**
	 * 给hand发送消息
	 * 
	 * @param what
	 */
	private void sendMessage(int what) {
		Message m = new Message();
		m.what = what;
		handler.sendMessage(m);
	}

}
