package com.xynxs.main.task;

import com.xynxs.main.BaseActivity;
import com.xynxs.main.FeedbackActivity;

public class FeedbackTask extends BaseTask{

	private String userId;
	
	private String content;
	private String tel;
	
	
	private String result;
	
	public FeedbackTask(BaseActivity activity, String content, String tel) {
		super(activity);
		userId = getActivity().getUser().getId();
		this.content = content;
		this.tel = tel;
	}

	@Override
	protected void doInBackground() {
		if(!isCancelled()){
			result = (String)getHelper().addFeedback(userId, content, tel);
		}
	}

	@Override
	protected void onPostExecute() {
		FeedbackActivity act = (FeedbackActivity)getActivity();
		act.submitResult(result);
	}

}
