package com.xynxs.main.task;

import com.xynxs.main.BaseActivity;
import com.xynxs.main.dialog.CommentDialog;

public class CommentTask extends BaseTask{
	
	
	private String userId;
	private String quoteContent;
	private String quoteId;
	private String content;
	private String postid;
	private String isNoname;
	
	private CommentDialog dialog;
	
	
	private String result;
	
	public CommentTask(CommentDialog dialog,String userId, String quoteContent, String quoteId, String content, String postid, boolean isNoname) {
		super(dialog.getActivity());
		this.dialog = dialog;
		this.userId = userId;
		this.quoteContent = quoteContent;
		this.quoteId = quoteId;
		this.content = content;
		this.postid = postid;
		if(isNoname){
			this.isNoname = "true";
		}else{
			this.isNoname = "";
		}
	}

	@Override
	protected void doInBackground() {
		if(!isCancelled() && !userId.equals("") && !postid.equals("") && !content.equals("")){
			result = (String)getHelper().commentPost(userId, quoteContent, quoteId, content, postid, isNoname);
		}
	}

	@Override
	protected void onPostExecute() {
		dialog.submitResult(result);
	}
}
