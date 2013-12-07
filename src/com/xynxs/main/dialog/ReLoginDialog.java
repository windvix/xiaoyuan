package com.xynxs.main.dialog;

import com.xynxs.main.LoginActivity;
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
 * 重新登录对话框
 */
public class ReLoginDialog extends Dialog implements android.view.View.OnClickListener {

	private View rootView;


	private MainActivity act;

	public ReLoginDialog(MainActivity activity) {
		super(activity, R.style.DimDialog);
		this.act = activity;
		LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.dialog_msg, null);
		addContentView(layout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		setContentView(layout);

		setCancelable(false);
		setCanceledOnTouchOutside(false);
		this.rootView = layout;

		((TextView) rootView.findViewById(R.id.dialog_message)).setText("账号过期，请重新登录");
		((TextView) rootView.findViewById(R.id.dialog_title)).setText("重新登录");

		Button btn = ((Button) rootView.findViewById(R.id.confirmBtn));
		btn.setText("确定");

		btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		//点击重新登录
		if (id == R.id.confirmBtn) {
			Intent loginIntent = new Intent(act, LoginActivity.class);
			act.startActivity(loginIntent);
			act.finish();
		} 
	}
}
