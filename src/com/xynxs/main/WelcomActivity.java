package com.xynxs.main;

import com.xynxs.main.util.Const;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class WelcomActivity extends BaseActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(hasShownGuide()){
			setContentView(R.layout.activity_welcome);
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					Intent intent = null;
					if(hasLogin()){
						intent = new Intent(getApplicationContext(), MainActivity.class);
					}else{
						intent = new Intent(getApplicationContext(), LoginActivity.class);
					}
					finish();
					startActivity(intent);
				}
			}, 1500);
			
		}else{
			finish();
			Intent guide = new Intent(getApplicationContext(), GuideActivity.class);
			startActivity(guide);
		}
	}
	
	
	
	
	//是否已显示引导界面
	private boolean hasShownGuide(){
		boolean hasShown = false;
		int saveVer = getDataInt(Const.VERSION_KEY);
		if(saveVer>=getVersionCode()){
			hasShown = true;
		}
		return hasShown;
	}
	
	

}
