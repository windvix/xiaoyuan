package com.xynxs.main.task;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.xynxs.main.BaseActivity;

/**
 * 加载个人头像
 */
public class LoadHeadImgTask extends BaseTask{

	private ImageView view;
	private String url;
	private Bitmap bitmap;
	
	public LoadHeadImgTask(BaseActivity activity, ImageView headView, String url) {
		super(activity);
		this.view = headView;
		this.url = url;
		startTask();
	}

	@Override
	protected void doInBackground() {
		if (view != null && url != null && view.getTag() != null && !isCancelled()){
			String localImgFileName = view.getTag().toString();				
			String localImgPath = getActivity().getAppDownloadDir()+localImgFileName;
			File file = new File(localImgPath);
			try{					
				//图片存在，尝试读取。。
				if(file.exists()){
					bitmap = getActivity().readBitmapAutoSize(localImgPath, view.getWidth(), view.getHeight());
					//读取失败，从网络中获取
					if(bitmap==null){
						//删除文件再创建文件
						file.delete();
						file.createNewFile();
						bitmap = byteToBitmap(getImage(url));
						saveFile(bitmap, file);
					}
				}
				//创建文件，从网络中下载图片
				else{
					file.createNewFile();
					bitmap = byteToBitmap(getImage(url));
					saveFile(bitmap, file);
				}
			}catch(Exception e){
				
			}
		}
	}

	
	
	@Override
	protected void onPostExecute() {
		if(bitmap!=null && view!=null){
			view.setImageBitmap(bitmap);
		}
	}
	
	
	private byte[] getImage(String path) {
		if (!isCancelled()) {
			try {
				URL url = new URL(path);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(5 * 1000);
				conn.setRequestMethod("GET");
				InputStream inStream = conn.getInputStream();
				if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
					return readStream(inStream);
				}
			} catch (Exception e) {

			}
		}
		return null;
	}
	
	/**
	 */
	private byte[] readStream(InputStream inStream) {
		if (!isCancelled()) {
			try {
				ByteArrayOutputStream outStream = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = inStream.read(buffer)) != -1 && !isCancelled()) {
					if (!isCancelled()) {
						outStream.write(buffer, 0, len);
					} else {
						len = -1;
					}
				}
				outStream.close();
				inStream.close();
				return outStream.toByteArray();
			} catch (Exception e) {

			}
		}
		return null;
	}

	/**
	 * 保存文件
	 */
	private void saveFile(Bitmap bm, File file) {
		if (!isCancelled()) {
			try {
				if (bm == null) {
					file.delete();
					return;
				}
				BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
				bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
				bos.flush();
				bos.close();
			} catch (Exception e) {

			}
		}
	}

	private Bitmap byteToBitmap(byte[] data) {
		if (data != null) {
			return BitmapFactory.decodeByteArray(data, 0, data.length);// bitmap
		}
		return null;
	}
	

}
