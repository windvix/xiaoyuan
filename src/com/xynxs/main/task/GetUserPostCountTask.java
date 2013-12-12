package com.xynxs.main.task;

import com.xynxs.main.component.ListUserPostHelper;

public class GetUserPostCountTask extends BaseTask{


	private String userId;
	
	private String result;
	
	
	private ListUserPostHelper helper;
	
	public GetUserPostCountTask(ListUserPostHelper helper, String userId) {
		super(helper.getActivity());
		this.helper = helper;
		this.userId = userId;
	}


	@Override
	protected void doInBackground() {
		result = (String)getHelper().getCount(userId, getActivity().getUser().getId(), "post");
	}


	@Override
	protected void onPostExecute() {
		helper.getPostCountResult(result);
	}
}
