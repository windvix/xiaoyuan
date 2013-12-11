package com.xynxs.main.task;

import com.xynxs.main.component.ListPostTaskHelper;

public class ListPostTask extends BaseTask{

	private ListPostTaskHelper main;
	
	private int tab;
	
	private String topic;
	
	private String userId;
	
	private int scope;
	
	
	private int index;
	
	private int count;
	
	private String keyword;
	
	private String result;
	
	public ListPostTask(ListPostTaskHelper main, String tab,String topic, String scope, int index, int count, String keyword) {
		super(main.getActivity());
		userId = getActivity().getUser().getId();
		this.main = main;
		this.tab = Integer.parseInt(tab);
		this.topic = topic;
		this.scope = 0;
		if (scope.contains("国")) {
			this.scope = 0;
		} else if (scope.contains("市")) {
			this.scope = 1;
		} else {
			this.scope = 3;
		}
		this.index  =  index;
		this.count = count;
		this.keyword = keyword;
	}

	@Override
	protected void doInBackground() {
		if(!isCancelled()){
			result = (String)getHelper().listPost(tab, topic, scope, userId, index, count, keyword);
		}
	}

	@Override
	protected void onPostExecute() {
		if(!isCancelled()){
			main.setPostList(result);
		}
	}

}
