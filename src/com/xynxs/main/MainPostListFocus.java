package com.xynxs.main;

import com.xynxs.main.component.PostListAdapterHelper;
import com.xynxs.main.task.ListPostTask;


import android.os.Handler;
import android.view.View;
import android.widget.TextView;

public class MainPostListFocus extends PostListAdapterHelper{


	private static final String aTAB = "3";

	public MainPostListFocus(MainActivityTab01 main, View root,TextView topicTv) {
		super(main, root, aTAB, topicTv);
		
		
		
	}

	/**
	 * 刷新当前列表
	 */
	public void refresh() {
		listView.setRefreshing();
		if (task != null) {
			task.stopTask();
		}
		task = new ListPostTask(main, aTAB, topicTv.getText().toString(), "", 0, COUNT, "");
		task.startTask();
	}

	@Override
	public ListPostTask nextPage(int index) {
		stopAllTask();
		task = new ListPostTask(main, aTAB, topicTv.getText().toString(), "", index, COUNT, "");
		task.startTask();
		return task;
	}

}
