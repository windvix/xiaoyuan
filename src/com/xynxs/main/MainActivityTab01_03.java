package com.xynxs.main;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnPullEventListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.State;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xynxs.main.adapter.PostListAdapter;
import com.xynxs.main.bean.Post;
import com.xynxs.main.task.ListPostTask;
import com.xynxs.main.util.Const;
import com.xynxs.main.util.StringUtil;

import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivityTab01_03 implements OnClickListener, OnRefreshListener<ListView> {

	private MainActivity act;

	private View root;

	private PullToRefreshListView listView;
	
	private TextView topicTv;

	private int index = 0;
	
	private static final int COUNT = 10;

	private static final String TAB = "3";

	private ListPostTask task;

	private MainActivityTab01 main;

	private PostListAdapter adapter;

	public MainActivityTab01_03(MainActivityTab01 main, View root,TextView topicTv) {
		this.main = main;
		this.act = main.getActivity();
		this.root = root;
		this.topicTv = topicTv;
		listView = (PullToRefreshListView) root.findViewById(R.id.pull_refresh_list);
		listView.setOnRefreshListener(this);

		// 加载上次的数据
		loadLastData();
		listView.setOnPullEventListener(new OnPullEventListener<ListView>() {
			@Override
			public void onPullEvent(PullToRefreshBase<ListView> refreshView, State state, Mode direction) {
				refreshView.findViewById(R.id.pull_to_refresh_sub_text).setVisibility(View.VISIBLE);
			}
		});
	}

	/**
	 * 刷新当前列表
	 */
	public void refresh() {
		listView.setRefreshing();
		if (task != null) {
			task.stopTask();
		}
		task = new ListPostTask(main, TAB, topicTv.getText().toString(), "", 0, COUNT, "");
		task.startTask();
	}

	/**
	 * 刷新结果
	 */
	public void onRefreshResult(String data) {
		listView.onRefreshComplete();
		if (StringUtil.isEmpty(data)) {
			act.toast("网络不给力，请稍后再试");
		} else {
			List<Post> postList = convertPostList(data);
			boolean hasMore = false;
			if (postList.size() >= COUNT) {
				hasMore = true;
			}
			postList.add(new Post());
			adapter = new PostListAdapter(act, R.layout.list_no_data, postList, topicTv.getText().toString(), hasMore);
			listView.setAdapter(adapter);
			index = 0;
			savePostList(postList);
		}
	}

	private void savePostList(List<Post> postList) {
		List<String> strList = new ArrayList<String>();
		for (Post post : postList) {
			String one = new Gson().toJson(post, Post.class);
			strList.add(one);
		}
		String value = new Gson().toJson(strList, ArrayList.class);
		act.saveDataString(Const.POST_LIST_KEY + TAB, value);
		act.saveDataLong(Const.LAST_REFRESH_TIME_KEY + TAB, System.currentTimeMillis());
	}

	@SuppressWarnings("unchecked")
	private List<Post> convertPostList(String data) {
		List<Post> postLst = new ArrayList<Post>();
		if (data.toString().startsWith("FAIL") || data.toString().equals("[]")) {
		} else {
			List<String> strList = (List<String>) act.convert(data, ArrayList.class);
			for (String oneStr : strList) {
				Post post = (Post) act.convert(oneStr, Post.class);
				postLst.add(post);
			}
		}
		return postLst;
	}

	@SuppressWarnings("unchecked")
	private void loadLastData() {
		String val = act.getDataString(Const.POST_LIST_KEY + TAB);
		long lastRefreshTime = act.getDataLong(Const.LAST_REFRESH_TIME_KEY + TAB);
		List<Post> postList = new ArrayList<Post>();
		if (StringUtil.isEmpty(val)) {
			postList.add(new Post());
		} else {
			List<String> resultList = new Gson().fromJson(val, ArrayList.class);
			for (String str : resultList) {
				Post post = new Gson().fromJson(str, Post.class);
				postList.add(post);
			}
		}
		adapter = new PostListAdapter(act, R.layout.list_no_data, postList, topicTv.getText().toString(), false);
		listView.setAdapter(adapter);
		setRefreshTime(lastRefreshTime);
	}

	private void setRefreshTime(long time) {
		if (listView != null) {
			TextView tv = (TextView) listView.findViewById(R.id.pull_to_refresh_sub_text);
			if (tv != null) {
				if (time <= 100) {
					tv.setText("上次刷新 未知");
				} else {
					tv.setText("上次刷新 " + StringUtil.getDateStr(time));
				}
			}

		}
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		refresh();
	}
}
