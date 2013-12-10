package com.xynxs.main;

import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.xynxs.main.dialog.LoadingDialog;
import com.xynxs.main.task.FeedbackTask;
import com.xynxs.main.util.StringUtil;

public class FeedbackActivity extends BaseActivity implements OnClickListener{
	
	private EditText et;
	
	private EditText tel;
	
	
	private LoadingDialog submitLoading;
	
	private FeedbackTask task;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		setContentView(R.layout.activity_feedback);
		
		et = (EditText)findViewById(R.id.feedback_et);
		tel = (EditText)findViewById(R.id.feedback_tel);
		findViewById(R.id.feedback_nouse).requestFocus();

		findViewById(R.id.title_bar_left_btn).setOnClickListener(this);
		findViewById(R.id.title_bar_right_btn).setOnClickListener(this);
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		if(id==R.id.title_bar_right_btn){
			String content = et.getText().toString();
			content = content.trim();
			if(content.equals("") || content==null){
				toast("反馈内容为空");
			}else{
				task = new FeedbackTask(FeedbackActivity.this, content, tel.getText().toString());
				submitLoading = new LoadingDialog(this, task, "正在提交");
				submitLoading.show();
			}
		}else{
			finish();
		}
	}

	
	@Override
	protected void onDestroy() {
		if(task!=null){
			task.stopTask();
		}
		super.onDestroy();
	}

	public void submitResult(String result){
		if(StringUtil.isEmpty(result)){
			toast("网络不给力，请稍后再试");
		}else{
			if(result.startsWith("FAIL")){
				toast("网络不给力，请稍后再试："+result);
			}else{
				toast("提交成功，感谢您的意见");
				finish();
			}
		}
	}
	
}
