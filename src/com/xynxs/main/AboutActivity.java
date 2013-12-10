package com.xynxs.main;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.xynxs.main.dialog.UpdateDialog;
import com.xynxs.main.task.BaseTask;
import com.xynxs.main.util.Const;

public class AboutActivity extends BaseActivity implements OnClickListener{

	private TextView currentVersion;
	private TextView newVersion;
	private TextView aboutApp;
	
	
	private int versionCode = 0;
	private String versionName = "";
	private String versionInfo = "";
	private String versionUrl = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		versionCode = getDataInt(Const.APP_SERVER_VERSION);
	
		versionName = getDataString(Const.APP_SERVER_NAME);
		versionInfo = getDataString(Const.APP_DESCRIPTION);
		versionUrl = getDataString(Const.APP_SERVER_URL);
		
		setContentView(R.layout.activity_about);

		currentVersion = (TextView) findViewById(R.id.current_version_tv);
		newVersion = (TextView) findViewById(R.id.new_version_tv);
		aboutApp = (TextView) findViewById(R.id.about_app_tv);

		findViewById(R.id.title_bar_left_btn).setOnClickListener(this);
		findViewById(R.id.version_btn).setOnClickListener(this);

		String app_name = getResources().getString(R.string.app_name);
		String com_name = getResources().getString(R.string.company_name);
		String ab_text = getResources().getString(R.string.about_app_text);

		ab_text = ab_text.replace("#APP_NAME", app_name);
		ab_text = ab_text.replace("#CMP_NAME", com_name);

		String cVersionname = getVersionName();

		currentVersion.setText("版本:" + cVersionname);
		if (getVersionCode()>=versionCode) {
			newVersion.setText("已是最新版本");
		} else {
			newVersion.setText("可更新至" + versionName);
		}

		aboutApp.setText(ab_text);

		super.onCreate(savedInstanceState);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.title_bar_left_btn) {
			finish();
		} else {
			if (getVersionCode()>=versionCode) {
				toast("当前已经是最新版");
			} else {
				// 更新到最新版
				new UpdateDialog(this, versionName, versionUrl, versionInfo).show();
			}
		}
	}

}
