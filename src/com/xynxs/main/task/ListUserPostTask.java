package com.xynxs.main.task;

import com.xynxs.main.component.ListUserPostHelper;

public class ListUserPostTask extends BaseTask{
	private String userId;

	private String myUserId;

	private int index;
	private int count;
	
	
	private String result;
	
	private ListUserPostHelper helper;

	public ListUserPostTask(ListUserPostHelper helper, String userId, int index, int count) {
		super(helper.getActivity());
		this.userId = userId;
		this.myUserId = getActivity().getUser().getId();
		this.helper = helper;
		this.index = index;
		this.count = count;
	}

	@Override
	protected void doInBackground() {
		result = (String)getHelper().listUserPost(userId, myUserId, index, count);
	}

	@Override
	protected void onPostExecute() {	
		boolean isAppend = false;
		if(index>0){
			isAppend = true;
		}
		helper.getPostResult(result, isAppend);
	}
}
