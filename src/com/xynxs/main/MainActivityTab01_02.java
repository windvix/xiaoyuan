package com.xynxs.main;

import com.xynxs.main.component.PostListAdapterHelper;
import com.xynxs.main.task.ListPostTask;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivityTab01_02 extends PostListAdapterHelper{

	
	private Button scopeBtn;

	private Button timeBtn;

	private static final String aTAB = "2";


	public MainActivityTab01_02(MainActivityTab01 main, View root, Button scopeBtn, TextView topicTv, Button timeBtn) {
		
		super(main,root, aTAB, topicTv);
		this.scopeBtn = scopeBtn;
		this.timeBtn = timeBtn;
	}

	/**
	 * 刷新当前列表
	 */
	@Override
	public void refresh() {
		listView.setRefreshing();
		if (task != null) {
			task.stopTask();
		}
		task = new ListPostTask(main, aTAB, topicTv.getText().toString(), scopeBtn.getText().toString(), 0, COUNT, timeBtn.getText().toString());
		task.startTask();
	}

	@Override
	public ListPostTask nextPage(int index) {
		stopAllTask();
		task = new ListPostTask(main, aTAB, topicTv.getText().toString(), scopeBtn.getText().toString(), index, COUNT, timeBtn.getText().toString());
		task.startTask();
		
		return task;
	}
}
