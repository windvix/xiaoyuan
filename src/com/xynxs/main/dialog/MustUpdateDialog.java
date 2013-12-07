package com.xynxs.main.dialog;

import com.xynxs.main.MainActivity;
import com.xynxs.main.R;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;


/**
 * 必须升级对话框
 */
public class MustUpdateDialog extends Dialog implements android.view.View.OnClickListener {

	private View rootView;

	private String url;

	private MainActivity act;

	public MustUpdateDialog(MainActivity activity, String versionName, final String url) {
		super(activity, R.style.DimDialog);
		this.url = url;
		this.act = activity;
		LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.dialog_confirm, null);
		addContentView(layout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		setContentView(layout);

		setCancelable(false);
		setCanceledOnTouchOutside(false);
		this.rootView = layout;

		((TextView) rootView.findViewById(R.id.dialog_message)).setText("此版本过旧，无法使用，请更新至最新版" + versionName);
		((TextView) rootView.findViewById(R.id.dialog_title)).setText("更新提示");

		Button exitBtn = ((Button) rootView.findViewById(R.id.positiveButton));
		exitBtn.setText("退出");
		Button downBtn = ((Button) rootView.findViewById(R.id.negativeButton));
		downBtn.setText("马上下载");
		
		exitBtn.setOnClickListener(this);
		downBtn.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		//退出程序
		if (id == R.id.positiveButton) {
			dismiss();
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			act.startActivity(intent);
			act.finish();
			System.exit(0);// 使虚拟机停止运行并退出程序
		} 
		//下载最新版
		else {
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse(url));
			act.startActivity(i);
		}
	}
}
