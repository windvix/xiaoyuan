package com.xynxs.main;

import com.xynxs.main.dialog.ConfirmDialog;

import android.content.Intent;
import android.sax.StartElementListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MainActivityTab04 implements OnClickListener {

	private MainActivity act;

	private View rootView;

	public MainActivityTab04(MainActivity activity, View view04) {
		this.rootView = view04;
		this.act = activity;

		rootView.findViewById(R.id.bottom_tab_04_btn_about).setOnClickListener(this);
		rootView.findViewById(R.id.bottom_tab_04_btn_clean).setOnClickListener(this);
		rootView.findViewById(R.id.bottom_tab_04_btn_exit).setOnClickListener(this);
		rootView.findViewById(R.id.bottom_tab_04_btn_fq).setOnClickListener(this);
		rootView.findViewById(R.id.bottom_tab_04_btn_pi).setOnClickListener(this);

		((TextView) rootView.findViewById(R.id.bottom_tab_04_username_tv)).setText(act.getUser().getName());
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if(id==R.id.bottom_tab_04_btn_about){
			
		}
		if(id==R.id.bottom_tab_04_btn_clean){
			
		}
		if(id==R.id.bottom_tab_04_btn_exit){
			final ConfirmDialog confirm = new ConfirmDialog(act, "退出", "取消", "确定退出当前账号吗？", "请选择");
			confirm.getPositiveBtn().setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					
					
					Intent intent = new Intent(act.getApplicationContext(), LoginActivity.class);
					act.cleanAllData();
					act.toast("请重新登录");
					confirm.dismiss();
					act.finish();
					act.startActivity(intent);
				}
			});
			confirm.getNegativeBtn().setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					confirm.dismiss();
				}
			});
			confirm.show();
		}
		if(id==R.id.bottom_tab_04_btn_fq){
			
		}
		if(id==R.id.bottom_tab_04_btn_pi){
			Intent intent = new Intent(act.getApplicationContext(), MyInfoActivity.class);
			act.startActivity(intent);
		}
	}
}
