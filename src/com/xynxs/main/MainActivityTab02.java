package com.xynxs.main;

import java.util.ArrayList;
import java.util.List;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.xynxs.main.bean.Reply;
import com.xynxs.main.component.ReplyListAdapter;

import android.os.AsyncTask;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivityTab02 {

	private MainActivity act;

	private View rootView;
	
	private PullToRefreshListView mPullRefreshListView;
	private ReplyListAdapter mAdapter;
	
	private List<Reply> list;
	
	public MainActivityTab02(MainActivity activity, View view02) {
		this.rootView = view02;
		this.act = activity;

		mPullRefreshListView = (PullToRefreshListView) rootView.findViewById(R.id.pull_refresh_list);

		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(act.getApplicationContext(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

				// Do work to refresh the list here.
				new GetDataTask().execute();
			}
		});

		mPullRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
			@Override
			public void onLastItemVisible() {
				Toast.makeText(act, "已经到最底了", Toast.LENGTH_SHORT).show();
			}
		});

		ListView actualListView = mPullRefreshListView.getRefreshableView();

		act.registerForContextMenu(actualListView);

		
		list = new ArrayList<Reply>();
		
		for(int i=0;i<10;i++){
			list.add(new Reply());
		}
		
		mAdapter = new ReplyListAdapter(act, R.layout.post_msg, list);
		// You can also just use setListAdapter(mAdapter) or
		// mPullRefreshListView.setAdapter(mAdapter)
		actualListView.setAdapter(mAdapter);
	}

	private class GetDataTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
			}
			return null;
		}
		
		
		@Override
		protected void onPostExecute(Void result) {
			list.add(new Reply());
			mAdapter.notifyDataSetChanged();
			mPullRefreshListView.onRefreshComplete();
		}
	}
}
