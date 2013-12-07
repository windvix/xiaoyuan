package com.xynxs.main;

import com.xynxs.main.task.BaseTask;
import com.xynxs.main.util.Const;
import com.xynxs.main.util.LevelUtil;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * 个人资料界面
 */
public class MyInfoActivity extends BaseActivity {

	private ImageView userImg;
	private TextView userImgBtn;
	private TextView userInfoAccount;
	private TextView userInfoLevel;
	private EditText userInfoNick;
	private TextView userInfoGender;
	private EditText userInfoRealname;
	private TextView userInfoCollege;
	private TextView userInfoEntrYear;
	private TextView userInfoBirth;
	private EditText userInfoQQ;
	private EditText userInfoIntro;
	private TextView userInfoLabel;
	private TextView userInfoScore;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		setContentView(R.layout.activity_user_info);
		
		findViewById(R.id.title_bar_left_btn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		findViewById(R.id.user_info_nouse).requestFocus();
		
		
		userImg = (ImageView) findViewById(R.id.user_info_img);
		userImgBtn = (TextView) findViewById(R.id.user_info_img_btn);
		userInfoAccount = (TextView) findViewById(R.id.user_info_account);
		userInfoLevel = (TextView) findViewById(R.id.user_info_level);
		userInfoNick = (EditText) findViewById(R.id.user_info_nick);
		userInfoGender = (TextView) findViewById(R.id.user_info_gender);
		userInfoCollege = (TextView) findViewById(R.id.user_info_college);
		userInfoEntrYear = (TextView) findViewById(R.id.user_info_entran_year);
		userInfoBirth = (TextView) findViewById(R.id.user_info_birth);
		userInfoQQ = (EditText) findViewById(R.id.user_info_QQ);
		userInfoIntro = (EditText) findViewById(R.id.user_info_intro);
		userInfoLabel = (TextView) findViewById(R.id.user_info_label);
		userInfoRealname = (EditText) findViewById(R.id.user_info_realn);
		userInfoScore = (TextView)findViewById(R.id.user_info_score);
		
		
		hideInputKeybord(userInfoNick);
		
		initData(false);
		
		super.onCreate(savedInstanceState);
	}
	
	
	/**
	 * 
	 */
	private void initData(boolean forceRefreshImg) {
		userImg.setTag(getUser().getId() + Const.MIN_JPG);
		userInfoAccount.setText(getUser().getRegisterDate() + "");
		
		long score = getUser().getScore();
		userInfoLevel.setText(LevelUtil.getLevelNameByScore(score)+"("+LevelUtil.getLevel(score)+"级)");
		userInfoScore.setText(score+"分");
		
		
		userInfoNick.setText(getUser().getName());
		
		int gender = getUser().getGender();
		if (gender == 2) {
			userInfoGender.setText("女");
		} else if (gender == 1) {
			userInfoGender.setText("男");
		}
		
		userInfoCollege.setText(getUser().getCollege_name());
		userInfoEntrYear.setText(getUser().getEntranceYear() + "");
		
		
		String birth = getUser().getBirthDay();
		if (birth == null) {
			birth = "";
		}
		userInfoBirth.setText(birth);
		if (!birth.equals("")) {
			if (birth.length() == 8) {
				userInfoBirth.setText(birth.substring(0, 4) + "-" + birth.substring(4, 6) + "-" + birth.substring(6, 8));
			}
		}
		
		setLabel(getUser().getLabel());
		
		if(imgTask!=null){
			imgTask.stopTask();
		}
		
		//imgTask = new LoadHeadImgTask(this, userImg);
		
		userInfoRealname.setText(getUser().getRealName());
		userInfoQQ.setText(getUser().getQQ());
		userInfoIntro.setText(getUser().getSign());
		
	}
	
	
	private void setLabel(String label){
		String temp = label.replace("[/-/]", "，");
		if (temp.length() > 0 && (temp.lastIndexOf("，") == temp.length() - 1)) {
			userInfoLabel.setText(temp.substring(0, temp.lastIndexOf("，")));
		} else {
			userInfoLabel.setText(temp);
		}
	}
	
	
	/**
	 * 隐藏弹出的键盘
	 */
	private void hideInputKeybord(EditText et) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
	}
	
	private LoadHeadImgTask imgTask;
	
	private class LoadHeadImgTask extends BaseTask{

		private ImageView view;
		private boolean forceRefresh = false;
		private String url;
		private Bitmap bitmap;
		
		public LoadHeadImgTask(BaseActivity activity, ImageView headView, boolean forceRefresh, String url) {
			super(activity);
			this.view = headView;
			this.forceRefresh = forceRefresh;
			this.url = url;
			startTask();
		}

		@Override
		protected void doInBackground() {
			if (view != null && url != null && view.getTag() != null && !isForceStop()){
			
			}
		}

		@Override
		public void stopTask() {
			
			super.stopTask();
		}
		
		@Override
		protected void onPostExecute() {
			
		}
	}
}
