package com.xynxs.main.component;

import java.util.ArrayList;
import java.util.List;

import android.test.MoreAsserts;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnPullEventListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.State;
import com.xynxs.main.MainActivity;
import com.xynxs.main.MainActivityTab01;
import com.xynxs.main.R;
import com.xynxs.main.adapter.PostListAdapter;
import com.xynxs.main.bean.Post;
import com.xynxs.main.task.ListPostTask;
import com.xynxs.main.util.Const;
import com.xynxs.main.util.StringUtil;

public abstract class PostListAdapterHelper implements OnRefreshListener<ListView> {

	protected MainActivityTab01 main;
	
	protected MainActivity act;
	
	
	protected PullToRefreshListView listView;
	
	protected ListPostTask task;
	
	protected TextView topicTv;
	
	protected View root;
	
	private String TAB;
	
	protected PostListAdapter adapter;
	
	protected static final int COUNT = 10;

	protected List<Post> postList;
	
	
	public PostListAdapterHelper(MainActivityTab01 main, View root, String tab, TextView topicTv){
		this.main = main;
		this.root = root;
		this.TAB = tab;
		this.topicTv = topicTv;
		this.act = main.getActivity();
		listView = (PullToRefreshListView) root.findViewById(R.id.pull_refresh_list);
		listView.setOnRefreshListener(this);
		
		
		listView.setOnPullEventListener(new OnPullEventListener<ListView>() {
			@Override
			public void onPullEvent(PullToRefreshBase<ListView> refreshView, State state, Mode direction) {
				refreshView.findViewById(R.id.pull_to_refresh_sub_text).setVisibility(View.VISIBLE);
			}
		});

		// 加载上次的数据
		loadLastData();
	}
	
	public abstract ListPostTask nextPage(int index);
	
	public abstract void refresh();
	
	
	/**
	 * 刷新结果
	 */
	public void onRefreshResult(String data) {
		listView.onRefreshComplete();
		if (StringUtil.isEmpty(data)) {
			act.toast("网络不给力，请稍后再试");
		} else {
			postList = convertPostList(data);
			adapter = new PostListAdapter(this, R.layout.list_no_data, postList, topicTv.getText().toString(), COUNT);
			listView.setAdapter(adapter);
			savePostList(postList);
		}
	}
	
	
	protected void savePostList(List<Post> postList) {
		List<String> strList = new ArrayList<String>();
		for (Post post : postList) {
			String one = new Gson().toJson(post, Post.class);
			strList.add(one);
		}
		String value = new Gson().toJson(strList, ArrayList.class);
		act.saveDataString(Const.POST_LIST_KEY + TAB, value);
		act.saveDataLong(Const.LAST_REFRESH_TIME_KEY + TAB, System.currentTimeMillis());
	}
	
	public MainActivity getActivity(){
		return act;
	}
	
	@SuppressWarnings("unchecked")
	public List<Post> convertPostList(String data) {
		List<Post> postLst = new ArrayList<Post>();
		if (data.toString().startsWith("FAIL") || data.toString().equals("[]")) {
		} else {
			List<String> strList = (List<String>) act.convert(data, ArrayList.class);
			for (String oneStr : strList) {
				Post post = (Post) act.convert(oneStr, Post.class);
				postLst.add(post);
			}
		}
		//增加一个空的在后面，为了有加载更多视图
		postLst.add(new Post());
		return postLst;
	}
	
	
	@SuppressWarnings("unchecked")
	protected void loadLastData() {
		String val = act.getDataString(Const.POST_LIST_KEY + TAB);
		long lastRefreshTime = act.getDataLong(Const.LAST_REFRESH_TIME_KEY + TAB);
		postList = new ArrayList<Post>();
		if (StringUtil.isEmpty(val)) {
			postList.add(new Post());
		} else {
			List<String> resultList = new Gson().fromJson(val, ArrayList.class);
			for (String str : resultList) {
				Post post = new Gson().fromJson(str, Post.class);
				postList.add(post);
			}
		}
		adapter = new PostListAdapter(this, R.layout.list_no_data, postList, topicTv.getText().toString(), COUNT);
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
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		refresh();
	}
	
	public void stopAllTask(){
		if(task!=null){
			task.cancelTask();
		}
		listView.onRefreshComplete();
	}
}
