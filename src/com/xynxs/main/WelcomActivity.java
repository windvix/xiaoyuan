package com.xynxs.main;

import com.xynxs.main.util.Const;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


/**
 * 欢迎界面
 */
public class WelcomActivity extends BaseActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//非第一次使用
		if(hasShownGuide()){
			setContentView(R.layout.activity_welcome);
			
			//欢迎界面1.5秒
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					Intent intent = null;
					
					//已登录则进入主界面
					if(hasLogin()){
						intent = new Intent(getApplicationContext(), MainActivity.class);
					}
					//未登录进入登录界面
					else{
						intent = new Intent(getApplicationContext(), LoginActivity.class);
					}
					finish();
					startActivity(intent);
				}
			}, 1500);
			
		}
		//第一次使用
		else{
			//清空旧版本中的所有数据
			cleanAllData();
			finish();
			//创建文件下载目录
			Intent guide = new Intent(getApplicationContext(), GuideActivity.class);
			startActivity(guide);
		}
	}
	
	
	
	
	
	
	
	
	/**
	 * 判断是否已经显示了引导界面
	 */
	private boolean hasShownGuide(){
		boolean hasShown = false;
		int saveVer = getDataInt(Const.VERSION_KEY);
		if(saveVer>=getVersionCode()){
			hasShown = true;
		}
		return hasShown;
	}
	
	

}
