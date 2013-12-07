package com.xynxs.main.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import com.xynxs.main.MainActivity;
import com.xynxs.main.R;

/**
 * 升级对话框
 */
public class UpdateDialog extends Dialog implements android.view.View.OnClickListener {

	private View rootView;

	private String url;

	private MainActivity act;

	public UpdateDialog(MainActivity activity, String versionName, final String url, String description) {
		super(activity, R.style.Dialog);
		this.url = url;
		this.act = activity;
		LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.dialog_update, null);
		addContentView(layout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		setContentView(layout);

		setCancelable(true);
		setCanceledOnTouchOutside(true);
		this.rootView = layout;

		((TextView) rootView.findViewById(R.id.dialog_message)).setText(description.replace(";", "\n"));
		((TextView) rootView.findViewById(R.id.dialog_title)).setText("更新版本" + versionName);

		Button exitBtn = ((Button) rootView.findViewById(R.id.positiveButton));
		exitBtn.setText("以后再说");
		Button downBtn = ((Button) rootView.findViewById(R.id.negativeButton));
		downBtn.setText("马上下载");
		
		exitBtn.setOnClickListener(this);
		downBtn.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		// 以后再说
		if (id == R.id.positiveButton) {
			dismiss();
		}
		// 下载最新版
		else {
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse(url));
			act.startActivity(i);
		}
	}
}
