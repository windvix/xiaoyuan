package com.xynxs.main.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
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

import com.google.gson.Gson;
import com.xynxs.main.BaseActivity;
import com.xynxs.main.PostDetailActivity;
import com.xynxs.main.R;
import com.xynxs.main.bean.User;
import com.xynxs.main.component.CommentHelper;
import com.xynxs.main.task.CommentTask;
import com.xynxs.main.util.LevelUtil;
import com.xynxs.main.util.StringUtil;

public class CommentDialog extends Dialog implements OnClickListener {
	private BaseActivity act;

	private EditText contentEt;

	private CheckBox isNonameBox;

	private TextView commentBtn;

	private String quoteUserId;
	private String quotePostContent;

	private String commentPostId;
	
	private boolean isSubmitting = false;
	
	
	private CommentTask task;
	
	private CommentHelper helper;

	@SuppressWarnings("deprecation")
	public CommentDialog(CommentHelper helper, String commentPostId, String quoteUserId, String quotePostContent) {
		super(helper.getActivity(), R.style.BottomLightsDialog);
		this.helper = helper;
		this.quoteUserId = quoteUserId;
		this.quotePostContent = quotePostContent;
		this.act = helper.getActivity();
		this.commentPostId = commentPostId;
		LayoutInflater inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.dialog_comment, null);
		addContentView(layout, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		setContentView(layout);
		setCancelable(true);
		setCanceledOnTouchOutside(true);
		Window win = getWindow();
		win.setGravity(Gravity.BOTTOM);

		WindowManager.LayoutParams lp = win.getAttributes();
		lp.width = (act.getScreenWidth()); // 设置宽度

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
			if (act != null) {
				act.toast(type + "内容不能为空");
			}
		} else {
			if (content.length() < 2 && content.length() > 150) {
				if (act != null) {
					act.toast(type + "内容2~150字");
				}
			} else {
				commitComment(commentPostId, content, quoteUserId, quotePostContent, isNoname);
			}
		}
	}

	
	
	private long commentTime = 0;
	
	
	private void commitComment(String postId, String content, String quoteId, String quoteContent, boolean isNoname){
		commentTime = System.currentTimeMillis();
		setViewNoEdit();
		if(task!=null){
			task.stopTask();
		}
		task = new CommentTask(this, act.getUser().getId(), quoteContent, quoteId, content, postId, isNoname);
		task.startTask();
	}
	
	
	
	public void submitResult(String data){
		setViewEditable();
		if(StringUtil.isEmpty(data)){
			act.toast("网络不给力，请稍后再试");
		}else{
			if(data.startsWith("FAIL")){
				act.toast("网络不给力，请稍后再试,"+data);
			}else{
				long score = -1;
				try{
					score = Long.parseLong(data.toString());
				}catch(Exception e){
					
				}
				User user = act.getUser();
				if(score>-1 && user!=null){
					if(score>user.getScore()){
						act.showUserToast("提交成功：+"+(score-user.getScore())+"分");

						long newScore = score;
						long oldScore = user.getScore();
						
						user.setScore(score);
						act.saveUser(new Gson().toJson(user));
						
						String name1 = LevelUtil.getLevelNameByScore(newScore);
						final String name2 = LevelUtil.getLevelNameByScore(oldScore);

						if (!name1.equals(name2)) {
							new Handler().postDelayed(new Runnable() {
								@Override
								public void run() {
									act.showUserToast("升级为：" + name2);
								}
							}, 2500);
						}
						
					}else{
						act.toast("提交成功");
					}
				}else{
					act.toast("提交成功");
				}
				contentEt.setText("");
				isNonameBox.setChecked(false);
				dismiss();
				helper.commentSuccess();
			}
		}
	}
	
	public BaseActivity getActivity(){
		return act;
	}
	
	
	@Override
	public void cancel() {
		if(isSubmitting){
			long now = System.currentTimeMillis();
			if(now-commentTime>(15*1000)){
				act.toast("提交超时");
				if(task!=null){
					task.stopTask();
				}
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
					act.toast("提交超时");
					if(task!=null){
						task.stopTask();
					}
					setViewEditable();
				}
				return false;
			}
		}
		return true;
	}
	
	@Override
	public void dismiss() {
		if(task!=null){
			task.stopTask();
		}
		super.dismiss();
	}
	
	private void setViewNoEdit(){
		isSubmitting = true;
		contentEt.setEnabled(false);
		contentEt.invalidate();
		findViewById(R.id.comment_noname_layout).setVisibility(View.INVISIBLE);
		findViewById(R.id.comment_submitting_layout).setVisibility(View.VISIBLE);
		commentBtn.setVisibility(View.GONE);
		commentBtn.setClickable(false);
	}
	
	private void setViewEditable(){
		isSubmitting = false;
		contentEt.setEnabled(true);
		contentEt.invalidate();
		findViewById(R.id.comment_noname_layout).setVisibility(View.VISIBLE);
		findViewById(R.id.comment_submitting_layout).setVisibility(View.INVISIBLE);
		commentBtn.setVisibility(View.VISIBLE);
		commentBtn.setClickable(true);
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
		InputMethodManager imm = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
	}

}
