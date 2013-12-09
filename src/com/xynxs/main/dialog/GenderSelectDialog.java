package com.xynxs.main.dialog;

import com.xynxs.main.R;
import com.xynxs.main.MyInfoActivity;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

public class GenderSelectDialog extends Dialog implements android.view.View.OnClickListener {

	private MyInfoActivity activity;

	public GenderSelectDialog(MyInfoActivity activity) {
		super(activity, R.style.BottomLightsDialog);
		this.activity = activity;

		LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.dialog_bottom_select, null);

		addContentView(layout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		setContentView(layout);

		layout.findViewById(R.id.bottom_dialog_btn_01).setOnClickListener(this);
		layout.findViewById(R.id.bottom_dialog_btn_02).setOnClickListener(this);
		layout.findViewById(R.id.bottom_dialog_btn_cancel).setOnClickListener(this);

		((TextView)layout.findViewById(R.id.bottom_dialog_btn_01)).setText("男生");
		((TextView)layout.findViewById(R.id.bottom_dialog_btn_02)).setText("女生");
		
		Window win = getWindow();
		win.setGravity(Gravity.BOTTOM);
		WindowManager.LayoutParams lp = win.getAttributes();

		lp.width = activity.getScreenWidth();

		onWindowAttributesChanged(lp);

		
		setCancelable(true);
		setCanceledOnTouchOutside(true);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		//选择了男
		if (id == R.id.bottom_dialog_btn_01) {
			activity.setGender("男");
		}
		//选择了女
		if (id == R.id.bottom_dialog_btn_02) {
			activity.setGender("女");
		}
		if (id == R.id.bottom_dialog_btn_cancel) {
			
		}
		dismiss();
	}

}
