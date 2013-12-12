package com.xynxs.main.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.xynxs.main.PostDetailActivity;
import com.xynxs.main.R;
import com.xynxs.main.util.StringUtil;

public class CommentDialog extends Dialog implements OnClickListener {
	private PostDetailActivity postActivity;

	private EditText contentEt;

	private CheckBox isNonameBox;

	private TextView commentBtn;

	private String quoteUserId;
	private String quotePostContent;

	private String commentPostId;
	
	private boolean isSubmitting = false;

	@SuppressWarnings("deprecation")
	public CommentDialog(PostDetailActivity activity, String commentPostId, String quoteUserId, String quotePostContent) {
		super(activity, R.style.BottomLightsDialog);
		this.quoteUserId = quoteUserId;
		this.quotePostContent = quotePostContent;
		this.postActivity = activity;
		this.commentPostId = commentPostId;
		LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.dialog_comment, null);
		addContentView(layout, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		setContentView(layout);
		setCancelable(true);
		setCanceledOnTouchOutside(true);
		Window win = getWindow();
		win.setGravity(Gravity.BOTTOM);

		WindowManager.LayoutParams lp = win.getAttributes();
		lp.width = (activity.getScreenWidth()); // 设置宽度

		contentEt = (EditText) layout.findViewById(R.id.comment_et);
		isNonameBox = (CheckBox) layout.findViewById(R.id.comment_noname_cb);

		commentBtn = (TextView) layout.findViewById(R.id.comment_btn);

		if (StringUtil.isEmpty(quotePostContent)) {
			commentBtn.setText("评论");
			contentEt.setHint("输入评论内容");
		} else {
			commentBtn.setText("回复");
			contentEt.setHint("输入回复内容");
		}

		commentBtn.setOnClickListener(this);

		onWindowAttributesChanged(lp);

		findViewById(R.id.comment_dialog_root_layout).setOnClickListener(this);
		
		setViewEditable();
	}

	@Override
	public void onClick(View v) {

		hideInputKeybord(contentEt);

		int id = v.getId();
		if (id == R.id.comment_btn) {
			commitComment();
		}
	}

	private void commitComment() {
		String type = commentBtn.getText().toString();
		String content = contentEt.getText().toString();
		boolean isNoname = isNonameBox.isChecked();

		if (content == null || content.equals("")) {
			if (postActivity != null) {
				postActivity.toast(type + "内容不能为空");
			}
		} else {

			if (content.length() < 2 && content.length() > 150) {
				if (postActivity != null) {
					postActivity.toast(type + "内容2~150字");
				}
			} else {
				commitComment(content, content, type, content, isNoname);
			}
		}
	}

	
	private long commentTime = 0;
	
	
	private void commitComment(String postId, String content, String quoteId, String quoteContent, boolean isNoname){
		commentTime = System.currentTimeMillis();
		setViewNoEdit();
	}
	
	
	@Override
	public void cancel() {
		if(isSubmitting){
			long now = System.currentTimeMillis();
			if(now-commentTime>(15*1000)){
				postActivity.toast("提交超时");
				setViewEditable();
			}
			return;
		}
		super.cancel();
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){
			if(isSubmitting){
				long now = System.currentTimeMillis();
				if(now-commentTime>(15*1000)){
					postActivity.toast("提交超时");
					setViewEditable();
				}
				return false;
			}
		}
		return true;
	}
	
	
	
	private void setViewNoEdit(){
		isSubmitting = true;
		contentEt.setEnabled(false);
		contentEt.setFocusable(false);
		contentEt.setClickable(false);
		
		findViewById(R.id.comment_noname_layout).setVisibility(View.INVISIBLE);
		findViewById(R.id.comment_submitting_layout).setVisibility(View.VISIBLE);
		commentBtn.setVisibility(View.GONE);
		commentBtn.setClickable(false);
	}
	
	private void setViewEditable(){
		isSubmitting = false;
		contentEt.setEnabled(true);
		contentEt.setFocusable(true);
		contentEt.setClickable(true);
		
		findViewById(R.id.comment_noname_layout).setVisibility(View.VISIBLE);
		findViewById(R.id.comment_submitting_layout).setVisibility(View.INVISIBLE);
		commentBtn.setVisibility(View.VISIBLE);
		commentBtn.setClickable(true);
	}
	
	
	
	
	
	public void setContent(String quoteId, String quoteContent, String content) {
		this.quoteUserId = quoteId;
		this.quotePostContent = quoteContent;
		contentEt.setText(content);
		if (quoteContent == null || quoteContent.equals("")) {
			commentBtn.setText("评论");
			contentEt.setHint("输入评论内容");
		} else {
			commentBtn.setText("回复");
			contentEt.setHint("输入回复内容");
		}
	}

	@Override
	public void show() {
		findViewById(R.id.comment_dialog_nouse).requestFocus();
		super.show();
	}

	/**
	 * 隐藏弹出的键盘
	 */
	public void hideInputKeybord(EditText et) {
		findViewById(R.id.comment_dialog_nouse).requestFocus();
		InputMethodManager imm = (InputMethodManager) postActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
	}

}
