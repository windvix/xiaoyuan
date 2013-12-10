package com.xynxs.main.dialog;

import com.xynxs.main.MainActivity;
import com.xynxs.main.MainActivityTab01;
import com.xynxs.main.R;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

public class SearchDialog extends Dialog implements OnClickListener {

	private TextView searchBtn;
	private EditText searchEt;
	private TextView noUse;

	private MainActivity activity;

	private MainActivityTab01 main;

	@SuppressWarnings("deprecation")
	public SearchDialog(MainActivityTab01 main) {
		super(main.getActivity(), R.style.DimDialog);
		this.main = main;
		this.activity = main.getActivity();
		LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.dialog_search, null);
		addContentView(layout, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		setContentView(layout);
		setCancelable(true);
		setCanceledOnTouchOutside(true);
		Window win = getWindow();
		win.setGravity(Gravity.TOP);
		WindowManager.LayoutParams lp = win.getAttributes();
		lp.width = (activity.getScreenWidth()); // 设置宽度

		int barHeight = activity.getResources().getDimensionPixelSize(R.dimen.TITLE_BAR_HEIGHT);
		lp.y = barHeight;

		onWindowAttributesChanged(lp);

		searchBtn = (TextView) layout.findViewById(R.id.search_btn);
		searchBtn.setOnClickListener(this);
		searchEt = (EditText) layout.findViewById(R.id.search_et);
		noUse = (TextView) layout.findViewById(R.id.search_nouse_tv);
	}

	@Override
	public void onClick(View v) {
		hideInputKeybord();
		String text = searchEt.getText().toString();
		if (text.equals("")) {
			activity.toast("请输入关键字");
		} else {
			main.searchKeyword(text);
			dismiss();
		}
	}

	/**
	 * 隐藏弹出的键盘
	 */
	private void hideInputKeybord() {
		InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(searchEt.getWindowToken(), 0);
	}

	@Override
	public void show() {
		searchEt.setText("");
		noUse.requestFocus();
		super.show();
	}
}
