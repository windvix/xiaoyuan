package com.xynxs.main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import com.xynxs.main.bean.User;
import com.xynxs.main.dialog.GenderSelectDialog;
import com.xynxs.main.dialog.LoadingDialog;
import com.xynxs.main.dialog.PictureSelectDialog;
import com.xynxs.main.task.LoadHeadImgTask;
import com.xynxs.main.task.UpdateUserInfoTask;
import com.xynxs.main.util.Const;
import com.xynxs.main.util.LevelUtil;
import com.xynxs.main.util.StringUtil;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 个人资料界面
 */
public class MyInfoActivity extends BaseActivity implements OnClickListener {

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

	private LoadHeadImgTask imgTask;

	private LoadingDialog submitLoading;

	private boolean pictureSelected;

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
		userInfoScore = (TextView) findViewById(R.id.user_info_score);

		hideInputKeybord(userInfoNick);

		initData();

		userInfoGender.setOnClickListener(this);
		userInfoCollege.setOnClickListener(this);
		userInfoEntrYear.setOnClickListener(this);
		userInfoBirth.setOnClickListener(this);
		userImgBtn.setOnClickListener(this);
		findViewById(R.id.title_bar_right_btn).setOnClickListener(this);
		findViewById(R.id.title_bar_left_btn).setOnClickListener(this);

		super.onCreate(savedInstanceState);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		// 城市
		city = getUser().getCity_name();

		// 用户头像
		userImg.setTag(getUser().getId() + Const.MIN_JPG);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				imgTask = new LoadHeadImgTask(MyInfoActivity.this, userImg, getUser().getHead_img());
			}
		}, 500);
		// 获取用户ID
		userInfoAccount.setText(getUser().getRegisterDate() + "");

		long score = getUser().getScore();
		// 用户等级及称呼
		userInfoLevel.setText(LevelUtil.getLevelNameByScore(score) + "(" + LevelUtil.getLevel(score) + "级)");

		// 积分
		userInfoScore.setText(score + "分");

		// 用户昵称
		userInfoNick.setText(getUser().getName());

		// 用户性别
		int gender = getUser().getGender();
		if (gender == 2) {
			userInfoGender.setText("女");
		} else if (gender == 1) {
			userInfoGender.setText("男");
		}

		// 用户的CO
		userInfoCollege.setText(getUser().getCollege_name());
		//
		userInfoEntrYear.setText(getUser().getEntranceYear() + "");

		// 用户生日
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
		// 用户标签
		setLabel(getUser().getLabel());

		// 用户真实名称
		userInfoRealname.setText(getUser().getRealName());

		// 用户QQ
		userInfoQQ.setText(getUser().getQQ());

		// 用户自我介绍
		userInfoIntro.setText(getUser().getSign());
	}

	/**
	 * 设置用户标签
	 */
	private void setLabel(String label) {
		String temp = label.replace("[/-/]", "，");
		if (temp.length() > 0 && (temp.lastIndexOf("，") == temp.length() - 1)) {
			userInfoLabel.setText(temp.substring(0, temp.lastIndexOf("，")));
		} else {
			userInfoLabel.setText(temp);
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		// 提交按钮
		if (id == R.id.title_bar_right_btn) {
			submitBtnClick();
		} else if (id == R.id.title_bar_left_btn) {
			finish();
		} else if (id == R.id.user_info_gender) {
			new GenderSelectDialog(this).show();
		} else if(id==R.id.user_info_college){
			showCollegeSelect();
		} else if(id==R.id.user_info_entran_year){
			showYearSelect();
		}else if(id==R.id.user_info_birth){
			showBirthSelect();
		}else if(id==R.id.user_info_img_btn){
			new PictureSelectDialog(this).show();
		}
	}
	
	
	private void showBirthSelect(){
		User user = getUser();
		String bir = user.getBirthDay();
		int temY = user.getEntranceYear()-19;
		int temM = 0;
		int temD = 1;
		if (bir == null || bir.equals("") || bir.length() != 8) {
		} else {
			
			temY = Integer.parseInt(bir.substring(0, 4));
			temM = Integer.parseInt(bir.substring(4, 6)) - 1;
			temD = Integer.parseInt(bir.substring(6, 8));
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(temY, temM, temD);
		
		DatePickerDialog picker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker dp, int year, int month, int dayOfMonth) {
				// et.setText("您选择了：" + year + "年" + (month+1) + "月" +
				// dayOfMonth + "日");
				String y = year + "";
				String m = (month + 1) + "";
				String d = dayOfMonth + "";
				if (m.length() == 1) {
					m = "0" + m;
				}
				if (d.length() == 1) {
					d = "0" + d;
				}
				userInfoBirth.setText(y + "-" + m + "-" + d);
			}
			
		}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
		picker.show();
	}
	
	
	private static final int COLLEGE_RESULT = 11;
	private static final int YEAR_RESULT = 23;
	
	/**
	 * 选择大学
	 */
	private void showCollegeSelect(){
		Intent intent = new Intent(this, SelectProvActivity.class);
		intent.putExtra(Const.COLLEGE_NAME_KEY, userInfoCollege.getText().toString());
		intent.putExtra(Const.CITY_NAME_KEY, city);
		startActivityForResult(intent, COLLEGE_RESULT);
	}
	
	//显示年份选择
	private void showYearSelect(){
		Intent intent = new Intent(this, SelectYearActivity.class);
		intent.putExtra(Const.ENTR_YEAR_KEY, Integer.parseInt(userInfoEntrYear.getText().toString()));
		startActivityForResult(intent, YEAR_RESULT);
	}
	
	private static final int CUT_PHOTO_REQUEST_CODE = 2117;
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//大学选择结果
		if(requestCode==COLLEGE_RESULT){
			String college = data.getStringExtra(Const.COLLEGE_NAME_KEY);
			userInfoCollege.setText(college);
			city = data.getStringExtra(Const.CITY_NAME_KEY);
		}
		//年份选择结果
		else if(requestCode==YEAR_RESULT){
			int entrYear = data.getIntExtra(Const.ENTR_YEAR_KEY, 0);
			userInfoEntrYear.setText(entrYear+"");
		}		
		// 在相册中选择了图片
		else if (requestCode == Const.PIC_SELECT_CODE && data != null) {
			Uri imgUri = data.getData();
			Intent cutIntent = new Intent("com.android.camera.action.CROP");
			cutIntent.setDataAndType(imgUri, "image/*");
			cutIntent.putExtra("crop", "true");
			cutIntent.putExtra("aspectX", 1);
			cutIntent.putExtra("aspectY", 1);
			cutIntent.putExtra("outputX", 100);
			cutIntent.putExtra("outputY", 100);
			cutIntent.putExtra("noFaceDetection", true);
			cutIntent.putExtra("return-data", true);
			startActivityForResult(cutIntent, CUT_PHOTO_REQUEST_CODE);
		}
		// 拍照
		if (requestCode == Const.PIC_TAKE_CODE) {
			File file = new File(getAppDownloadDir() + Const.TAKE_PIC_FILE_NAME);
			if (file.length() > 1) {
				Uri imgUri = Uri.fromFile(file);
				Intent cutIntent = new Intent("com.android.camera.action.CROP");
				cutIntent.setDataAndType(imgUri, "image/*");
				cutIntent.putExtra("crop", "true");
				cutIntent.putExtra("aspectX", 1);
				cutIntent.putExtra("aspectY", 1);
				cutIntent.putExtra("outputX", 100);
				cutIntent.putExtra("outputY", 100);
				cutIntent.putExtra("noFaceDetection", true);
				cutIntent.putExtra("return-data", true);
				startActivityForResult(cutIntent, CUT_PHOTO_REQUEST_CODE);
			} else {
				try {
					file.delete();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		//裁剪图片
		else if (requestCode == CUT_PHOTO_REQUEST_CODE && data != null) {
			Bitmap bm = data.getParcelableExtra("data");
			if (bm != null) {
				saveBitmap(getUser().getId() + Const.TEMP_JPG, bm);
				userImg.setImageBitmap(bm);
				userImg.invalidate();
				pictureSelected = true;
			}
		}
	}
	
	
	
	private String city;


	/**
	 * 提交按钮
	 */
	private void submitBtnClick() {
		findViewById(R.id.user_info_nouse).requestFocus();
		if (!isChange()) {
			toast("资料没有变化");
			return;
		}
		String nick = userInfoNick.getText().toString().trim();
		String birth = "";
		String college = "";
		String QQ = "";
		String intro = "";
		int entrYear = 100;
		int gender = 1;

		String label = "";
		String realName = "";
		User user = getUser();
		if (nick.equals("")) {
			toast("昵称不能为空");
		} else {
			if (nick.length() > 10) {
				toast("昵称要求1~10字");
			} else {
				birth = userInfoBirth.getText().toString().replace("-", "");
				college = userInfoCollege.getText().toString();
				entrYear = Integer.parseInt(userInfoEntrYear.getText().toString());
				QQ = userInfoQQ.getText().toString();
				intro = userInfoIntro.getText().toString();
				label = userInfoLabel.getText().toString();
				label = label.trim();
				label = label.replace(" ", "[/-/]");
				label = label.replace(",", "[/-/]");
				label = label.replace(";", "[/-/]");
				label = label.replace("，", "[/-/]");
				label = label.replace("、", "[/-/]");
				realName = userInfoRealname.getText().toString();
				String sex = userInfoGender.getText().toString().trim();
				if (sex.equals("男")) {
					gender = 1;
				} else {
					gender = 2;
				}

				// 设置所有的属性
				user.setName(nick);
				user.setBirthDay(birth);
				user.setCollege_name(college);
				user.setEntranceYear(entrYear);
				user.setGender(gender);
				user.setQQ(QQ);
				user.setSign(intro);
				user.setLabel(label);
				user.setCity_name(city);
				user.setRealName(realName);

				if (realName.length() > 7) {
					toast("姓名7个字以内");
				} else {

					if (label.replace("[/-/]", ";").split(";").length > 10) {
						toast("最多设置10个感兴趣的标签");
					} else {

						if (intro.length() > 50) {
							toast("个人说明不能超过50字");
						} else {
							if (QQ.length() > 13) {
								toast("QQ长度13字以内");
							} else {
								submitLoading = new LoadingDialog(this, new UpdateUserInfoTask(this, pictureSelected, user), "正在提交");
								submitLoading.show();
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 设置性别
	 */
	public void setGender(String gender) {
		userInfoGender.setText(gender);
	}

	/**
	 * 提交数据返回的结果
	 */
	public void submitResult(String result) {
		submitLoading.dismiss();
		if (StringUtil.isEmpty(result)) {
			toast("网络不给力，请稍后再试");
		} else {
			if (result.startsWith("FAIL")) {
				toast(result);
			} else {
				saveUser(result);
				if (pictureSelected) {
					// 删除本地用户头像
					deleteUserHeadImg();
				}
				pictureSelected = false;
				toast("保存成功");
				finish();
			}
		}
	}

	// 删除本地用户头像
	private void deleteUserHeadImg() {
		File min = new File(getAppDownloadDir() + getUser().getId() + Const.MIN_JPG);
		File max = new File(getAppDownloadDir() + getUser().getId() + Const.MAX_JPG);
		try {
			min.delete();
			max.delete();
		} catch (Exception e) {

		}
	}

	@Override
	protected void onDestroy() {
		if (imgTask != null) {
			imgTask.stopTask();
		}
		super.onDestroy();
	}

	

	/**
	 * 信息是否发生了变化
	 */
	private boolean isChange() {
		boolean isChange = false;
		if (pictureSelected) {
			isChange = true;
		} else {
			User user = getUser();
			String nick = userInfoNick.getText().toString();
			String birth = userInfoBirth.getText().toString().replace("-", "");
			String college = userInfoCollege.getText().toString();;
			String city = this.city;
			int entrYear = Integer.parseInt(userInfoEntrYear.getText().toString());
			String QQ = userInfoQQ.getText().toString();
			String intro = userInfoIntro.getText().toString();
			String label = userInfoLabel.getText().toString();
			label = label.trim();
			label = label.replace(" ", "[/-/]");
			label = label.replace(",", "[/-/]");
			label = label.replace(";", "[/-/]");
			label = label.replace("，", "[/-/]");
			label = label.replace("、", "[/-/]");
			String realName = userInfoRealname.getText().toString();
			String sex = userInfoGender.getText().toString().trim();
			int gender = 1;
			if (sex.equals("男")) {
				gender = 1;
			} else {
				gender = 2;
			}
			
			if(!nick.equals(user.getName())){
				isChange = true;
			}
			if(!birth.equals(user.getBirthDay())){
				isChange = true;
			}
			if(!college.equals(user.getCollege_name())){
				isChange = true;
			}
			if(!city.equals(user.getCity_name())){
				isChange = true;
			}
			if(entrYear!=user.getEntranceYear()){
				isChange = true;
			}
			if(!QQ.equals(user.getQQ())){
				isChange = true;
			}
			if(!intro.equals(user.getSign())){
				isChange = true;
			}
			if(!label.equals(user.getLabel())){
				isChange = true;
			}
			if(!realName.equals(user.getRealName())){
				isChange = true;
			}
			if(gender!=user.getGender()){
				isChange = true;
			}
		}
		return isChange;
	}
	
	/**
	 * 隐藏弹出的键盘
	 */
	private void hideInputKeybord(EditText et) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
	}
	
	
	/**
	 * 保存图片到下载目录
	 */
	private File saveBitmap(String fileName, Bitmap mBitmap) {
		File file = new File(getAppDownloadDir() + fileName);
		try {
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();
			FileOutputStream fOut = null;
			fOut = new FileOutputStream(file);
			mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
			fOut.flush();
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}
}
