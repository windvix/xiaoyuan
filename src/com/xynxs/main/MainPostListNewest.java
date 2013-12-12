package com.xynxs.main;

import com.xynxs.main.component.PostListAdapterHelper;
import com.xynxs.main.task.ListPostTask;
import com.xynxs.main.util.Const;

import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainPostListNewest extends PostListAdapterHelper {

	private Button scopeBtn;

	private static final String aTAB = "1";

	private String keyword;

	public MainPostListNewest(MainActivityTab01 main, View root, Button scopeBtn, TextView topicTv) {
		super(main, root, aTAB, topicTv);
		this.scopeBtn = scopeBtn;

		
		
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				
				long lastUpdate =  act.getDataLong(Const.LAST_REFRESH_TIME_KEY + aTAB);
				
				if(System.currentTimeMillis()-lastUpdate>(Const.REFRESH_PERIOD)){
					listView.setRefreshing();
				}
			}
		}, 2000);
	}

	/**
	 * 刷新当前列表
	 */
	public void refresh(String keyword) {
		this.keyword = keyword;
		listView.setRefreshing();
		if (task != null) {
			task.stopTask();
		}
		task = new ListPostTask(main, aTAB, topicTv.getText().toString(), scopeBtn.getText().toString(), 0, COUNT, keyword);
		task.startTask();
	}

	@Override
	public ListPostTask nextPage(int index) {
		stopAllTask();
		task = new ListPostTask(adapter, aTAB, topicTv.getText().toString(), scopeBtn.getText().toString(), index, COUNT, keyword);		
		task.startTask();
		return task;
	}

	@Override
	public void refresh() {
		refresh("");
	}
}
