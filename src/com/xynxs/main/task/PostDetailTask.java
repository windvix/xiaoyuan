package com.xynxs.main.task;

import com.xynxs.main.PostDetailActivity;
import com.xynxs.main.util.StringUtil;

public class PostDetailTask extends BaseTask {

	private String postId;
	private String postOwnerId;
	private String myUserId;
	private String addVisited = "";

	private String result;
	
	private static final String VISIT_KEY = "VISIT_";

	public PostDetailTask(PostDetailActivity activity, String postId, String postOwnerId) {
		super(activity);
		this.postId = postId;
		this.myUserId = getActivity().getUser().getId();
		
		String val = getActivity().getDataString(VISIT_KEY+postId);
		if(StringUtil.isEmpty(val)){
			addVisited = "add";
		}else{
			addVisited = "";
		}
		
		
		this.postOwnerId = postOwnerId;
	}

	@Override
	protected void doInBackground() {
		if (postId != null) {
			result = (String) getHelper().postDetail(postId, myUserId, addVisited, postOwnerId);
		}
	}

	@Override
	protected void onPostExecute() {
		PostDetailActivity act = (PostDetailActivity) getActivity();
		if(!StringUtil.isEmpty(result)){
			if(!result.startsWith("FAIL")){
				act.saveDataString(VISIT_KEY+postId, result);
			}
		}
		act.getPostDetailResult(result);
	}

}
