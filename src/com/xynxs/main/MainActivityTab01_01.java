package com.xynxs.main;

import java.util.ArrayList;
import java.util.List;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xynxs.main.bean.Post;
import com.xynxs.main.component.PostListAdapter;

import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

public class MainActivityTab01_01 implements OnClickListener, OnRefreshListener<ListView>{

	private MainActivity act;
	
	private View root;
	
	private PullToRefreshListView listView;
	
	public MainActivityTab01_01(MainActivity act, View root){
		this.act = act;
		this.root = root;
		listView = (PullToRefreshListView)root.findViewById(R.id.pull_refresh_list);
		listView.setOnRefreshListener(this);
		
		//加载上次的数据
		loadLastData();
	}

	
	private void loadLastData(){
		List<Post> list = new ArrayList<Post>();
		list.add(new Post());
		listView.setAdapter(new PostListAdapter(act, R.layout.list_no_data, list));
	}
	
	
	@Override
	public void onClick(View v) {
		
	}

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				listView.onRefreshComplete();
			}
		}, 2000);
	}
}
