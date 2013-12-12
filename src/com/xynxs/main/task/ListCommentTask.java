package com.xynxs.main.task;

import com.xynxs.main.BaseActivity;
import com.xynxs.main.PostDetailActivity;

public class ListCommentTask extends BaseTask{

	private String postId;
	private int index;
	private int count;
	
	private String result;
	
	public ListCommentTask(BaseActivity activity, String postId, int index, int count) {
		super(activity);
		this.postId = postId;
		this.index = index;
		this.count = count;
	}

	@Override
	protected void doInBackground() {
		result = (String)getHelper().listComment(postId, index, count);
	}

	@Override
	protected void onPostExecute() {
		PostDetailActivity act = (PostDetailActivity)getActivity();
		act.getCommentResult(result);
	}
}
